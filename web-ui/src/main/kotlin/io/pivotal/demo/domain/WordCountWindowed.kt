package io.pivotal.demo.domain

import org.springframework.data.annotation.Id
import org.springframework.data.gemfire.mapping.annotation.Region

import java.text.SimpleDateFormat
import java.util.Date

@Region(name = "Words_Count_Windowed")
class WordCountWindowed {

    @Id
    var wordCountCompsiteKey: WordCountWindowedCompsiteKey? = null

    var wordCountValue: Long = 0

    var end: Date? = null

    var endTimeInLong: Long? = null

    var startTimeInLong: Long? = null

    var word: String? = null

    override fun toString(): String {
        val formatter = SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
        val sb = StringBuffer("WordCountWindowed{")
        sb.append("word='").append(wordCountCompsiteKey?.word).append('\'')
        sb.append(", count=").append(wordCountValue)
        sb.append(", start=").append(formatter.format(wordCountCompsiteKey?.start))
        sb.append(", end=").append(formatter.format(end))
        sb.append('}')
        return sb.toString()
    }

    constructor(wordCountWindowedCompsiteKey: WordCountWindowedCompsiteKey?, count: Long,
                end: Date, startTimeInLong: Long?, endTimeInLong: Long?) {
        this.wordCountCompsiteKey = wordCountWindowedCompsiteKey
        this.wordCountValue = count
        this.end = end
        this.endTimeInLong = endTimeInLong
        this.startTimeInLong = startTimeInLong
        this.word = this.wordCountCompsiteKey?.word
    }

   constructor(){

   }
}

