package io.pivotal.demo.domain;

import java.util.Date;
import java.util.Objects;

public class WordCountWindowedCompsiteKey {
	public WordCountWindowedCompsiteKey(String word, Date start) {
		this.word = word;
		this.start = start;
	}

	public WordCountWindowedCompsiteKey() {
	}

	private String word;
	private Date start;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Date getStart() {
		return start;
	}

	public void setStart(Date start) {
		this.start = start;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		WordCountWindowedCompsiteKey that = (WordCountWindowedCompsiteKey) o;
		return Objects.equals(word, that.word) &&
				Objects.equals(start, that.start);
	}

	@Override
	public int hashCode() {
		return Objects.hash(word, start);
	}
}
