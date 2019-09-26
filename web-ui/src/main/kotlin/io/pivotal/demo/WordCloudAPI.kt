package io.pivotal.demo

import org.springframework.beans.factory.annotation.Value
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.Random
import kotlin.streams.asSequence

data class Word(val text: String, val weight: Int)

@RestController
class WordCloudAPI(
    @Value("\${words.text.length:10}") val textLength: Int = 10,
    @Value("\${words.frame.size:100}") val frameSize: Int = 100) {
    private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    @GetMapping("/words")
    fun words(): List<Word> {
        val words = mutableListOf<Word>()
        for (i in 1..frameSize) {
            words.add(Word(randomWord(), (10 until 100).random()))
        }
        return words
    }

    private fun randomWord(): String {
        return Random().ints((3 until textLength).random().toLong(), 0, charPool.size)
                .asSequence()
                .map(charPool::get)
                .joinToString("")
    }

    fun IntRange.random() = Random().nextInt((endInclusive + 1) - start) + start
}