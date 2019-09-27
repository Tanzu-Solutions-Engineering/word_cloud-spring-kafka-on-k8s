package io.pivotal.demo.repositories

import io.pivotal.demo.domain.WordCount
import org.springframework.data.gemfire.repository.GemfireRepository
import org.springframework.stereotype.Repository

// Repo for WordCount
@Repository
interface WordCountRepo : GemfireRepository<WordCount, String>
