package io.pivotal.demo.functions

import io.pivotal.demo.domain.WordCount
import io.pivotal.demo.domain.WordCountWindowed
import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnRegion

@OnRegion(region = "Words_Count_Windowed")
interface WindowedWordCountFunction {

    @FunctionId("ComputeWindowedWordCount")
    fun computeWindowedWordCount(startTimeInMillis: Long?, endTimeInMillis: Long?): HashMap<String, Long>
}
