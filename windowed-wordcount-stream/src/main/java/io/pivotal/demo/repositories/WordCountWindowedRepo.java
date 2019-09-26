package io.pivotal.demo.repositories;

import io.pivotal.demo.domain.WordCountWindowedCompsiteKey;
import io.pivotal.demo.domain.WordCountWindowed;

import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordCountWindowedRepo extends GemfireRepository<WordCountWindowed, WordCountWindowedCompsiteKey> {
}
