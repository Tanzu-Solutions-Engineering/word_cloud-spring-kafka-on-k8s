package io.pivotal.demo;

import java.util.Arrays;
import java.util.Calendar;

import io.pivotal.demo.domain.WordCountWindowedCompsiteKey;
import io.pivotal.demo.bindings.LinesProcessorBinding;

import io.pivotal.demo.domain.WordCountWindowed;
import io.pivotal.demo.repositories.WordCountWindowedRepo;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class WindowedWordcountApplication {

	public static void main(String[] args) {
		SpringApplication.run(WindowedWordcountApplication.class, args);
	}

	@Component
	@EnableBinding(LinesProcessorBinding.class)
	public static class LinesProcessor {
		private final Logger log = LoggerFactory.getLogger(getClass());
		static final String ALPHA_NUMERIC = "^[a-zA-Z0-9]+$";
		private WordCountWindowedRepo wcwRepo;
		public static final int WINDOW_SIZE_MS = 30000;

		public LinesProcessor(WordCountWindowedRepo wcwRepo) {
			this.wcwRepo = wcwRepo;
		}

		WordCountWindowed createWordCountWindowed(String word, Long count) {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MILLISECOND, -WINDOW_SIZE_MS);
			Long startTimeInLong = calendar.getTimeInMillis();
			WordCountWindowedCompsiteKey key = new WordCountWindowedCompsiteKey(word, calendar.getTime());
			calendar.add(Calendar.MILLISECOND, WINDOW_SIZE_MS);
			Long endTimeInLong = calendar.getTimeInMillis();
			return new WordCountWindowed(key, count, calendar.getTime(), startTimeInLong, endTimeInLong);
		}

		@StreamListener(LinesProcessorBinding.LINES_IN)
		@SendTo(LinesProcessorBinding.COUNTS_OUT)
		public KStream<?, WordCountWindowed> countWords(KStream<Object, String> lines) {
			KStream<?, WordCountWindowed> windowedCountStream = lines
					.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\b")))
					.filter((key, value) -> (value.matches(ALPHA_NUMERIC)))
					.map((key, value) -> new KeyValue<>(value, value))
					.groupByKey(Serialized.with(Serdes.String(), Serdes.String()))
					.windowedBy(TimeWindows.of(WINDOW_SIZE_MS))
					.count(Materialized.as("WindowedWordCount"))
					.toStream()
					.map((key, value) -> new KeyValue<>(null, createWordCountWindowed(key.key(), value)))
					;

			windowedCountStream
					.foreach((key, wcw) -> {
						log.info(wcw.toString());

						WordCountWindowed wcwSaved = wcwRepo.save(wcw);

						if (wcwSaved != null)
							log.info("Cache write successful: " + wcwSaved);
						else
							log.error("Write to cache failed for: " + wcw);
					});

			return windowedCountStream;
		}
	}
}
