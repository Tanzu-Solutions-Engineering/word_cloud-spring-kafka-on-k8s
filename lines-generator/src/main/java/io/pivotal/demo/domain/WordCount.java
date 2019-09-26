package io.pivotal.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region(name="Words_Count")
public class WordCount {

	@Id
	private String word;
	private long wordCount;

	@Override
	public String toString() {
		return "WordCount{" +
				"word='" + word + '\'' +
				", count=" + wordCount +
				'}';
	}

	public WordCount() {
	}

	public WordCount(String word, long count) {
		this.word = word;
		this.wordCount = count;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public long getWordCount() {
		return wordCount;
	}

	public void setWordCount(long count) {
		this.wordCount = count;
	}
}