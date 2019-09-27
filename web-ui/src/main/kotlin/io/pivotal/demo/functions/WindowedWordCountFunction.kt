package io.pivotal.demo.functions

import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion

@OnRegion(region = "Words_Count_Windowed")
interface WindowedWordCountFunction {

    @FunctionId("ComputeWindowedWordCount")
    fun computeWindowedWordCount(startTimeInMillis: Long?, endTimeInMillis: Long?): List<*>
}
