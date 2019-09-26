package io.pivotal.demo.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

@Region(name="Words_Count_Windowed")
public class WordCountWindowed {

	public WordCountWindowedCompsiteKey getWordCountCompsiteKey() {
		return wordCountWindowedCompsiteKey;
	}

	public void setWordCountCompsiteKey(WordCountWindowedCompsiteKey wordCountCompsiteKey) {
		this.wordCountWindowedCompsiteKey = wordCountCompsiteKey;
	}

	@Id
	WordCountWindowedCompsiteKey wordCountWindowedCompsiteKey;

	private long count;



	private Date end;

	@Override
	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		final StringBuffer sb = new StringBuffer("WordCountWindowed{");
		sb.append("word='").append(wordCountWindowedCompsiteKey.getWord()).append('\'');
		sb.append(", count=").append(count);
		sb.append(", start=").append(formatter.format(wordCountWindowedCompsiteKey.getStart()));
		sb.append(", end=").append(formatter.format(end));
		sb.append('}');
		return sb.toString();
	}

	public WordCountWindowed() {

	}

	public WordCountWindowed(WordCountWindowedCompsiteKey wordCountWindowedCompsiteKey, long count, Date end) {
		this.wordCountWindowedCompsiteKey = wordCountWindowedCompsiteKey;
		this.count = count;
		this.end = end;
	}


//	WordCount(String word, long count, Date start, Date end) {
//		this.word = word;
//		this.count = count;
//		this.start = start;
//		this.end = end;
//	}
//
//	public String getWord() {
//		return word;
//	}
//
//	public void setWord(String word) {
//		this.word = word;
//	}
//
//	public Date getStart() {
//		return start;
//	}
//
//	public void setStart(Date start) {
//		this.start = start;
//	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}



	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
}

