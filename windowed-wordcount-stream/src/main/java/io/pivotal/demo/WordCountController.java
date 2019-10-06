package io.pivotal.demo;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.apache.kafka.streams.state.WindowStore;
import org.apache.kafka.streams.state.WindowStoreIterator;
import org.apache.kafka.streams.state.internals.WindowKeySchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.pivotal.demo.bindings.LinesProcessorBinding;
import io.pivotal.demo.domain.WordCount;
import io.pivotal.demo.domain.WordCountWindowed;

@RestController
@RequestMapping ("/")
public class WordCountController {
	
	
	@Autowired
	private InteractiveQueryService interactiveQueryService;
	
	
	
	@GetMapping ("/windowedWordCount/{interval}")
	public List<WordCount> getWindowedWords(
			@PathVariable("interval") long interval)
	{
	
		return getWordCountByInterval(interval);
		
	
	}

	private List<WordCount> getWordCountByInterval(long interval)
	{
		WordCount wc = null;
		List<WordCount> wordCounts = new ArrayList<WordCount>();
		KeyValueIterator<Windowed<String>,Long> keyValueIterator = null;
		KeyValue<Windowed<String>, Long> keyValue = null;
		ReadOnlyWindowStore<String, Long> windowedWordStore = null;
		long endTime = System.currentTimeMillis();
		long startTime = System.currentTimeMillis() - interval;
		
		try {
			windowedWordStore =
					interactiveQueryService.getQueryableStore(LinesProcessorBinding.WIN_WORD_STORE, 
							QueryableStoreTypes.windowStore());
			
			keyValueIterator = windowedWordStore.fetchAll(startTime, endTime);
			//keyValueIterator = windowedWordStore.all();
			System.out.println("Are there records " + keyValueIterator.hasNext());
			int index = 0;
			while (keyValueIterator.hasNext() )
	  		{
				keyValue = keyValueIterator.next();
				wc = new WordCount();
	  			wc.setWord(keyValue.key.key());
	  			wc.setWordCount(keyValue.value);
	  			wordCounts.add(wc);
	  			
	  		}		
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		return wordCounts;
	}
	
	
	/**
	 * Returns counts for all words received and retained so far.
	 * @return
	 */
	private List<WordCount> getAllWordCounts()
	{
		WordCount wc = null;
		List<WordCount> wordCounts = new ArrayList<WordCount>();
		
		KeyValueIterator<Windowed<String>,Long> keyValueIterator = null;
		KeyValue<Windowed<String>, Long> keyValue = null;
		ReadOnlyWindowStore<String, Long> windowedWordStore = null;
	
		
		try {
			windowedWordStore =
					interactiveQueryService.getQueryableStore(LinesProcessorBinding.WIN_WORD_STORE, 
							QueryableStoreTypes.windowStore());
			
			keyValueIterator = windowedWordStore.all();
			
			System.out.println("Are there records " + keyValueIterator.hasNext());
			int index = 0;
			while (keyValueIterator.hasNext() )
	  		{
				keyValue = keyValueIterator.next();
				wc = new WordCount();
	  			wc.setWord(keyValue.key.key());
	  			wc.setWordCount(keyValue.value);
	  			wordCounts.add(wc);
	  			
	  		}		
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}		
		return wordCounts;
	}
}
