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

    @GetMapping("/words/60S")
    fun words(): List<Word> {
        val allwords = mutableListOf<Word>()
        for (w in wcRepo.findAll()){
            if (!commonWordCheck(w.word!!))
                allwords.add(Word(w.word!!, w.wordCount))
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
            if (!commonWordCheck(entry.key!!!!))
                allwords.add(Word(entry.key!!, entry.value))
        }

        if (allwords.size == 0){
            allwords.add(Word("NO STREAM DATA", 10))
        }

        return allwords.sortedWith(CompareWords).take(20)
    }

    fun commonWordCheck(word:String) : Boolean {
        val commonWords = arrayOf("the", "be", "to", "of", "and", "a", "in", "that", "have","I", "it", "for", "not", "on",
                                  "with", "he", "as", "you", "do", "at", "this", "but", "his", "by", "from", "they", "we",
                                  "say", "her", "she", "or", "an", "will", "my", "one", "all", "would", "there", "their",
                                  "what", "so", "up", "out", "if", "about", "who", "get", "which", "go", "me", "when",
                                  "make", "can", "like", "time", "no", "just", "him", "know", "take", "people", "into",
                                  "year", "your", "good", "some", "could", "them", "see", "other", "than", "then", "now",
                                  "look", "only", "come", "its", "over", "think", "also", "back", "after", "use", "two",
                                   "how", "our", "work", "first", "well", "way", "even", "new", "want", "because", "any",
                                   "these", "give", "day", "most", "us", "is", "0", "s", "2", "1", "has", "are")
        if (commonWords.contains(word)){
            return true
        }
        return false
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
