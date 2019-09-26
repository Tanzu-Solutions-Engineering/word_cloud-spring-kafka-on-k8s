package io.pivotal.demo.linesgenerator.bindings;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface LinesSourceBinding {

	static final String LINES_OUT = "LINES_OUT";

	@Output(LINES_OUT)
	MessageChannel linesOut();
}
