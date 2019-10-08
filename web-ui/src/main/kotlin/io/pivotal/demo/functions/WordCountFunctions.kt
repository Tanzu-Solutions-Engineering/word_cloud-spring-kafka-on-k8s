package io.pivotal.demo.functions

import org.springframework.data.gemfire.function.annotation.FunctionId
import org.springframework.data.gemfire.function.annotation.OnServer

@OnServer
interface WordCountFunctions {

    @FunctionId("ComputeWindowedWordCount")
    fun computeWindowedWordCount(startTimeInMillis: Long?, endTimeInMillis: Long?): Map<String, Long>

    @FunctionId("ComputeWordCount")
    fun computeWordCount(): Map<String, Long>
}
