package io.pivotal.demo

import io.pivotal.demo.domain.WordCount
import io.pivotal.demo.domain.WordCountWindowed
import io.pivotal.demo.domain.WordCountWindowedCompsiteKey
import io.pivotal.demo.functions.WindowedWordCountFunction
import io.pivotal.demo.repositories.WordCountRepo
import io.pivotal.demo.repositories.WordCountWindowedRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.streams.asSequence

data class Word(val text: String, val weight: Long)

@RestController
class WordCloudAPI(
    @Value("\${words.text.length:10}") val textLength: Int = 10,
    @Value("\${words.frame.size:100}") val frameSize: Int = 100) {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    @Autowired
    lateinit var wcRepo: WordCountRepo

    @Autowired
    lateinit var wcwRepo: WordCountWindowedRepo

    @Autowired
    lateinit var functionExecutor: WindowedWordCountFunction

    @GetMapping("/words")
    fun words(): List<Word> {
        val words = mutableListOf<Word>()
        for (w in wcRepo.findAll()){
            words.add(Word(w.word!!, w.wordCount))
        }

        if (words.size == 0){
            words.add(Word("NO STREAM DATA", 10))
        }

        return words
    }

    @GetMapping("/listwcw")
    fun listWCW(): Iterable<WordCountWindowed> {
        return wcwRepo.findAll()
    }

    @GetMapping("/computewordcount/{startTime}/{endTime}")
    fun computeWordCount(@PathVariable startTime: Long?,
                         @PathVariable endTime: Long?): List<*> {
        return functionExecutor.computeWindowedWordCount(startTime, endTime)
    }

    private fun randomWord(): String {
        return Random().ints((3 until textLength).random().toLong(), 0, charPool.size)
                .asSequence()
                .map(charPool::get)
                .joinToString("")
    }

    fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start

    constructor(wcRepo: WordCountRepo, wcwRepo: WordCountWindowedRepo, functionExecutor: WindowedWordCountFunction) : this() {
        this.wcRepo = wcRepo
        this.wcwRepo = wcwRepo
        this.functionExecutor = functionExecutor
    }
}