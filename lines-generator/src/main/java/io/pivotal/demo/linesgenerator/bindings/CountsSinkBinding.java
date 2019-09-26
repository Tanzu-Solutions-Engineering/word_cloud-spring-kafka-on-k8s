package io.pivotal.demo.linesgenerator.bindings;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface CountsSinkBinding {
	static final String COUNTS_IN = "COUNTS_IN";

	@Input(COUNTS_IN)
	SubscribableChannel countsIn();
}
