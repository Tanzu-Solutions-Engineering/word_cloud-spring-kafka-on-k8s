package io.pivotal.demo.bindings;

import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.kstream.internals.TimeWindow;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;

public interface LinesProcessorBinding {
	static final String LINES_IN = "LINES_IN";
	static final String COUNTS_OUT = "COUNTS_OUT";

	@Input(LINES_IN)
	KStream<Bytes, String> linesIn();

	
	String WIN_WORD_STORE = "CountsWindowStore";
}
