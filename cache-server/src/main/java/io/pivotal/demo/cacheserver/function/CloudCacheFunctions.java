package io.pivotal.demo.cacheserver.function;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.pivotal.demo.cacheserver.repo.WordCountRepo;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.function.annotation.GemfireFunction;
import org.springframework.stereotype.Component;
import io.pivotal.demo.cacheserver.repo.WordCountWindowedRepo;

@Component
public class CloudCacheFunctions {
	
	@Autowired
	WordCountWindowedRepo wordCountWindowedRepo;

	@Autowired
	WordCountRepo wordCountRepo;
	
	@GemfireFunction(id = "ComputeWindowedWordCount")
	public Map<String, Long> computeWindowedWordCount(Long startTimeInMillis, Long endTimeInMillis) {

		List<PdxInstance> wordsWithCountInWindow = wordCountWindowedRepo.
				getWordCountInTimeWindowOrderByWord(startTimeInMillis, endTimeInMillis);
		
		Map<String, Long> results = new HashMap<String, Long>();
		for (PdxInstance wordObject : wordsWithCountInWindow) {
			
			String word = (String) wordObject.getField("word");
			Long count = (Long) wordObject.getField("wordCountValue");

			if (!isCommonWord(word)){
				if (results.containsKey(word)) {
					Long countTmp = results.get(word);
					count = count + countTmp;
					results.put(word, count);
				} else {
					results.put(word, count);
				}
			}
		}
		
		return results;
	}

	@GemfireFunction(id = "ComputeWordCount")
	public Map<String, Long> computeWordCount() {

		List<PdxInstance> wordCounts = wordCountRepo.findTop200();
		Map<String, Long> results = new HashMap<String, Long>();
		for (PdxInstance wordObject : wordCounts) {

			String word = (String) wordObject.getField("word");
			Long count = (Long) wordObject.getField("wordCount");

			if (!isCommonWord(word)) {
				results.put(word,count);
			}
		}

		return results;
	}

	public boolean isCommonWord(String word) {
		String[] commonWords = {"the", "be", "to", "of", "and", "a", "in", "that", "have", "I", "it", "for", "not", "on",
				"with", "he", "as", "you", "do", "at", "this", "but", "his", "by", "from", "they", "we",
				"say", "her", "she", "or", "an", "will", "my", "one", "all", "would", "there", "their",
				"what", "so", "up", "out", "if", "about", "who", "get", "which", "go", "me", "when",
				"make", "can", "like", "time", "no", "just", "him", "know", "take", "people", "into",
				"year", "your", "good", "some", "could", "them", "see", "other", "than", "then", "now",
				"look", "only", "come", "its", "over", "think", "also", "back", "after", "use", "two",
				"how", "our", "work", "first", "well", "way", "even", "new", "want", "because", "any",
				"these", "give", "day", "most", "us", "is", "0", "s", "2", "1", "has", "are"};

		for (String cw : commonWords) {
			if (word.equalsIgnoreCase(cw)) {
				return true;
			}
		}

		return false;
	}

}
