package io.pivotal.demo.linesgenerator;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.pivotal.demo.linesgenerator.bindings.CountsSinkBinding;
import io.pivotal.demo.linesgenerator.bindings.LinesSourceBinding;
import io.pivotal.demo.linesgenerator.bindings.WindowedCountsSinkBinding;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class LinesGeneratorApplication {

	public static void main(String[] args) {
		SpringApplication.run(LinesGeneratorApplication.class, args);
	}

	@Component
	@EnableBinding(LinesSourceBinding.class)
	public static class LinesGenerator implements ApplicationRunner {
		private final Logger log = LoggerFactory.getLogger(getClass());

		private final MessageChannel linesOut;
		public LinesGenerator(LinesSourceBinding binding) {
			linesOut = binding.linesOut();
		}

		@Override
		public void run(ApplicationArguments args) {

			Random random = new Random();

			ArrayList<String> urls = new ArrayList<String>();
			try {
				Document doc = Jsoup.connect("http://spring.io/blog").get();
				log.info(doc.title());
				Elements blogs = doc.select("article a");
				for (Element blog : blogs) {
					urls.add(blog.absUrl("href"));
				}
			}
			catch (Exception e){
				log.error("HTML scraper failed...");
				log.error(e.getMessage());
			}

			Runnable runnable = () -> {
				try {
				Document doc = Jsoup.connect(urls.get(random.nextInt(urls.size()-1))).get();
				Elements paragraphs = doc.select("p");

				for (Element paragraph : paragraphs) {
					    String line = Jsoup.parse(paragraph.toString()).text();
						Message<String> message = MessageBuilder
								.withPayload(line)
								.setHeader(KafkaHeaders.MESSAGE_KEY, line.getBytes())
								.build();
						linesOut.send(message);
						log.info("Sent - " + line);
					}

				} catch (Exception e) {
					log.error(e.getMessage());
				}
			};
			Executors.newScheduledThreadPool(1).scheduleAtFixedRate(runnable, 1, 2, TimeUnit.SECONDS);
		}

	}

	@Component
	@EnableBinding(CountsSinkBinding.class)
	public static class CountDisplay {
		private final Logger log = LoggerFactory.getLogger(getClass());

		@StreamListener(CountsSinkBinding.COUNTS_IN)
		public void showCounts(Object wc) {
			log.info("WordCount received: " + wc);
		}
	}

	@Component
	@EnableBinding(WindowedCountsSinkBinding.class)
	public static class WindowedCountDisplay {
		private final Logger log = LoggerFactory.getLogger(getClass());

		@StreamListener(WindowedCountsSinkBinding.WINDOWED_COUNTS_IN)
		public void showCounts(Object wcw) {
			log.info("WordCountWindowed received: " + wcw);
		}
	}

}
