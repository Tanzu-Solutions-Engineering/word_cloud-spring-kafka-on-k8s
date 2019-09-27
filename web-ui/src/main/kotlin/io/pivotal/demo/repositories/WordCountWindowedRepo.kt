package io.pivotal.demo.repositories

import io.pivotal.demo.domain.WordCountWindowed
import io.pivotal.demo.domain.WordCountWindowedCompsiteKey
import org.springframework.data.gemfire.repository.GemfireRepository
import org.springframework.stereotype.Repository

@Repository
interface WordCountWindowedRepo : GemfireRepository<WordCountWindowed, WordCountWindowedCompsiteKey>
