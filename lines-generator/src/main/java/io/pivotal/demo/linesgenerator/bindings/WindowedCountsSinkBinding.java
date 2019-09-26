package io.pivotal.demo.linesgenerator.bindings;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface WindowedCountsSinkBinding {

	static final String WINDOWED_COUNTS_IN = "WINDOWED_COUNTS_IN";

	@Input(WINDOWED_COUNTS_IN)
	SubscribableChannel countsIn();
}
