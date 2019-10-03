package io.pivotal.demo

import io.pivotal.demo.functions.WindowedWordCountFunction
import io.pivotal.demo.repositories.WordCountRepo
import io.pivotal.demo.repositories.WordCountWindowedRepo
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import kotlin.streams.asSequence

data class Word(val text: String, val weight: Long)

class CompareWords {
    companion object : Comparator<Word> {
        override fun compare(a: Word, b: Word): Int = when {
            a.weight != b.weight -> b.weight.toInt() - a.weight.toInt()
            else -> b.weight.toInt() - a.weight.toInt()
        }
    }
}

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

    @GetMapping("/words/10S")
    fun words(): List<Word> {
        val allwords = mutableListOf<Word>()
        for (w in wcRepo.findAll()){
            allwords.add(Word(w.word!!, w.wordCount))
        }

        if (allwords.size == 0){
            allwords.add(Word("NO STREAM DATA", 10))
        }

        return allwords.shuffled().take(20)
    }

    @GetMapping("/words/60S")
    fun wordsWindowedLast60seconds(): List<Word> {
        val allwords = mutableListOf<Word>()
        val wordsHashMap = functionExecutor.computeWindowedWordCount(System.currentTimeMillis() - 60000,
                System.currentTimeMillis()).get(0) as HashMap<String, Long>

        for (entry in wordsHashMap.entries)
        {
            allwords.add(Word(entry.key!!, entry.value))
        }

        if (allwords.size == 0){
            allwords.add(Word("NO STREAM DATA", 10))
        }

        return allwords.sortedWith(CompareWords).take(20)
    }

    @GetMapping("/words/2-5M")
    fun wordsWindowed2to5minutes(): List<Word> {
        val allwords = mutableListOf<Word>()
        val wordsHashMap = functionExecutor.computeWindowedWordCount(System.currentTimeMillis() - 300000,
                System.currentTimeMillis() - 120000).get(0) as HashMap<String, Long>

        for (entry in wordsHashMap.entries)
        {
            allwords.add(Word(entry.key!!, entry.value))
        }

        if (allwords.size == 0){
            allwords.add(Word("NO STREAM DATA", 10))
        }

        return allwords.sortedWith(CompareWords).take(20)
    }

//    @GetMapping("/computewordcount/{startTime}/{endTime}")
//    fun computeWordCount(@PathVariable startTime: Long?,
//                         @PathVariable endTime: Long?): List<*> {
//        return functionExecutor.computeWindowedWordCount(startTime, endTime)
//    }


    constructor(wcRepo: WordCountRepo, wcwRepo: WordCountWindowedRepo, functionExecutor: WindowedWordCountFunction) : this() {
        this.wcRepo = wcRepo
        this.wcwRepo = wcwRepo
        this.functionExecutor = functionExecutor
    }
}
