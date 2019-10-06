package io.pivotal.demo;

import java.time.Duration;
import java.util.Arrays;
import java.util.Calendar;

import io.pivotal.demo.domain.WordCountWindowedCompsiteKey;
import io.pivotal.demo.bindings.LinesProcessorBinding;

import io.pivotal.demo.domain.WordCountWindowed;
import io.pivotal.demo.repositories.WordCountWindowedRepo;
import reactor.core.publisher.GroupedFlux;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.KGroupedStream;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Serialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.WindowStore;
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
		//private WordCountWindowedRepo wcwRepo;
		public static final int WINDOW_SIZE_MS = 30000;

		/*public LinesProcessor(WordCountWindowedRepo wcwRepo) {
			this.wcwRepo = wcwRepo;
		}*/

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
		//@SendTo(LinesProcessorBinding.COUNTS_OUT)
		public void countWords(KStream<Object, String> lines) {
			/*KStream<?, Long> windowedCountStream = lines
					.flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\b")))
					.filter((key, value) -> (value.matches(ALPHA_NUMERIC)))
					.map((key, value) -> new KeyValue<>(value, value))
					.groupByKey(Serialized.with(Serdes.String(), Serdes.String()))
					.windowedBy(TimeWindows.of(WINDOW_SIZE_MS))
					.count(Materialized.as("WindowedWordCount"))
					.toStream()
					//.map((key, value) -> new KeyValue<>(
					//		key, value))
					//.map((key, value) -> new KeyValue<>(value.getStartTimeInLong(),value))
					;*/
			
			KGroupedStream<String, String> groupedByWord = lines
					  .flatMapValues(value -> Arrays.asList(value.toLowerCase().split("\\b")))
					  .filter((key, value) -> (value.matches(ALPHA_NUMERIC)))
						.map((key, value) -> new KeyValue<>(value, value))
						.groupByKey(Serialized.with(Serdes.String(), Serdes.String()));

					// Create a window state store named "CountsWindowStore" that contains the word counts for every minute
			KTable<Windowed<String>, Long> kTable =	groupedByWord.windowedBy(TimeWindows.of(WINDOW_SIZE_MS))
					  .count(Materialized.<String, Long, WindowStore<Bytes, byte[]>>as
							  ("CountsWindowStore"));

			kTable.toStream().foreach((key, wcw) -> {
						log.info("Key " +  key.toString() + "  Value  " + wcw);
					});

			//return windowedCountStream;*/
		}
	}
}
