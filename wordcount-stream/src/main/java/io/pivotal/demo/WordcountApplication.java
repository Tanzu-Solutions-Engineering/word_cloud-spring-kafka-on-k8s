package io.pivotal.demo;

import java.util.Arrays;

import io.pivotal.demo.bindings.LinesProcessorBinding;
import io.pivotal.demo.domain.WordCount;
import io.pivotal.demo.repositories.WordCountRepo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class WordcountApplication {

	public static void main(String[] args) {
		SpringApplication.run(WordcountApplication.class, args);
	}

	@Component
	@EnableBinding(LinesProcessorBinding.class)
	public static class LinesProcessor {
		private final Logger log = LoggerFactory.getLogger(getClass());
		static final String ALPHA_NUMERIC = "^[a-zA-Z0-9]+$";
		private WordCountRepo wcRepo;

		public LinesProcessor(WordCountRepo wcRepo) {
			this.wcRepo = wcRepo;
		}


		@StreamListener(LinesProcessorBinding.LINES_IN)
		@SendTo(LinesProcessorBinding.COUNTS_OUT)
		public KStream<?, WordCount> countWords(KStream<Object, String> lines) {
			KStream<?, WordCount> countStream = lines
					.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\b")))
					.filter((key, value) -> (value.matches(ALPHA_NUMERIC)))
					.map((key, value) -> new KeyValue<>(value, value))
					.groupByKey(Serialized.with(Serdes.String(), Serdes.String()))
					.count(Materialized.as("AggregateCount"))
					.toStream()
					.map((w, c) -> new KeyValue<>(null,
							new WordCount(w, c)));

			countStream
					.foreach((key, wc) -> {
						log.info("WordCount - " + wc);
						WordCount wcSaved = wcRepo.save(wc);
						if (wcSaved != null)
							log.info("Cache write successful: " + wcSaved);
						else
							log.error("Write to cache failed for: " + wc.getWord());
					});

			return countStream;
		}
	}

}

