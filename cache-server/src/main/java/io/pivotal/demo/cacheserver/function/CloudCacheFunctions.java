package io.pivotal.demo.cacheserver.function;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.geode.pdx.PdxInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.stereotype.Component;

import io.pivotal.demo.cacheserver.repo.WordCountWindowedRepo;



@Component
public class CloudCacheFunctions {
	
	@Autowired
	WordCountWindowedRepo wordCountWindowedRepo;
	
	@GemfireFunction(id = "ComputeWindowedWordCount")
	public Map<String, Long> computeWindowedWordCount(Long startTimeInMillis, Long endTimeInMillis) {
		
		List<PdxInstance> wordsWithCountInWindow = wordCountWindowedRepo.
				getWordCountInTimeWindowOrderByWord(startTimeInMillis, endTimeInMillis);
		
		Map<String, Long> results = new HashMap<String, Long>();
		for (PdxInstance wordObject : wordsWithCountInWindow) {
			
			String word = (String) wordObject.getField("word");
			Long count = (Long) wordObject.getField("wordCountValue");
			
			if (results.containsKey(word)) {
				Long countTmp = results.get(word);
				count = count + countTmp;
				results.put(word, count);
			} else {
				results.put(word, count);
			}
		}
		
		return results;
	}

}
