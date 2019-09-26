package io.pivotal.demo.domain;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.annotation.Region;

// Word count for time windows

//start locator
//configure pdx --read-serialized=true
//start server
//create region --name=Words_Count --type=PARTITION --enable-statistics=true --entry-time-to-live-expiration=60 --entry-time-to-live-expiration-action=DESTROY
//create region --name=Words_Count_Windowed --type=PARTITION_REDUNDANT_HEAP_LRU --eviction-action=local-destroy --eviction-max-memory=7
// describe region --name=Words_Count_Windowed

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

	private long wordCountValue;

	private Date end;
	
	private Long endTimeInLong;
	
	private Long startTimeInLong;
	
	private String word;

	@Override
	public String toString() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
		final StringBuffer sb = new StringBuffer("WordCountWindowed{");
		sb.append("word='").append(wordCountWindowedCompsiteKey.getWord()).append('\'');
		sb.append(", count=").append(wordCountValue);
		sb.append(", start=").append(formatter.format(wordCountWindowedCompsiteKey.getStart()));
		sb.append(", end=").append(formatter.format(end));
		sb.append('}');
		return sb.toString();
	}

	public WordCountWindowed() {

	}

	public WordCountWindowed(WordCountWindowedCompsiteKey wordCountWindowedCompsiteKey, long count, 
			Date end, Long startTimeInLong, Long endTimeInLong) {
		this.wordCountWindowedCompsiteKey = wordCountWindowedCompsiteKey;
		this.wordCountValue = count;
		this.end = end;
		this.endTimeInLong = endTimeInLong;
		this.startTimeInLong = startTimeInLong;
		this.word = this.wordCountWindowedCompsiteKey.getWord();
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
	
	public long getWordCountValue() {
		return wordCountValue;
	}

	public void setWordCountValue(long wordCountValue) {
		this.wordCountValue = wordCountValue;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public Date getEnd() {
		return end;
	}

	public void setEnd(Date end) {
		this.end = end;
	}
	
	public Long getEndTimeInLong() {
		return endTimeInLong;
	}

	public void setEndTimeInLong(Long endTimeInLong) {
		this.endTimeInLong = endTimeInLong;
	}

	public Long getStartTimeInLong() {
		return startTimeInLong;
	}

	public void setStartTimeInLong(Long startTimeInLong) {
		this.startTimeInLong = startTimeInLong;
	}
}

