package io.pivotal.demo.domain

import java.util.Date
import java.util.Objects

class WordCountWindowedCompsiteKey {

    var word: String? = null
    var start: Date? = null

    constructor(word: String, start: Date) {
        this.word = word
        this.start = start
    }

    constructor() {}

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as WordCountWindowedCompsiteKey?
        return word == that!!.word && start == that.start
    }

    override fun toString(): String {
        return "WordCountWindowedCompsiteKey{" +
                "word='" + word + '\''.toString() +
                ", start=" + start +
                '}'.toString()
    }

    override fun hashCode(): Int {
        return Objects.hash(word, start)
    }
}
