package io.pivotal.demo.functions;

import java.util.List;

import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnRegion;

@OnRegion(region = "Words_Count_Windowed")
public interface WindowedWordCountFunction {

	@SuppressWarnings("rawtypes")
	@FunctionId("ComputeWindowedWordCount")
	List computeWindowedWordCount(Long startTimeInMillis, Long endTimeInMillis);
}
