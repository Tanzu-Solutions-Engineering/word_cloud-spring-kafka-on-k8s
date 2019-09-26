package io.pivotal.demo.linesgenerator;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.pivotal.demo.linesgenerator.bindings.CountsSinkBinding;
import io.pivotal.demo.linesgenerator.bindings.LinesSourceBinding;
import io.pivotal.demo.linesgenerator.bindings.WindowedCountsSinkBinding;
import io.pivotal.demo.domain.WordCount;
import io.pivotal.demo.domain.WordCountWindowed;
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
			String[] lines = new String[]{
					"Pivotal sells Pivotal Cloud Foundry or PCF",
					"PCF is a platform to deploy other platforms such as Pivotal Application Service (PAS), Pivotal Container Service (PKS), RabbitMQ, Pivotal Cloud Cache (PCC), etc",
					"Madhav works at Pivotal as a Platform Architet",
					"Madhav sells PCF with PAS, PKS, RabbitMQ, PCC",
					"PAS is the best platform to deploy cloud native apps",
					"Cloud native apps deliver cloud portability, scalability, disposability"
			};
			Random random = new Random();

			Runnable runnable = () -> {
				String line = lines[random.nextInt(6)];
				Message<String> message = MessageBuilder
						.withPayload(line)
						.setHeader(KafkaHeaders.MESSAGE_KEY, line.getBytes())
						.build();
				try {
					linesOut.send(message);
					log.info("Sent - " + line);
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
		public void showCounts(WordCount wc) {
			log.info("WordCount received: " + wc);
		}
	}

	@Component
	@EnableBinding(WindowedCountsSinkBinding.class)
	public static class WindowedCountDisplay {
		private final Logger log = LoggerFactory.getLogger(getClass());

		@StreamListener(WindowedCountsSinkBinding.WINDOWED_COUNTS_IN)
		public void showCounts(WordCountWindowed wcw) {
			log.info("WordCountWindowed received: " + wcw);
		}
	}

}
