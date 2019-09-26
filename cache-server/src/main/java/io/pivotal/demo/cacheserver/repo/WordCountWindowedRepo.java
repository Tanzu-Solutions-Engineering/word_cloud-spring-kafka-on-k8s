package io.pivotal.demo.cacheserver.repo;

import java.util.List;

import org.apache.geode.pdx.PdxInstance;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.stereotype.Repository;

import io.pivotal.demo.domain.WordCountWindowed;
import io.pivotal.demo.domain.WordCountWindowedCompsiteKey;

@Repository
public interface WordCountWindowedRepo extends GemfireRepository<WordCountWindowed, WordCountWindowedCompsiteKey> {
	
	@Query("SELECT wordObject FROM /Words_Count_Windowed wordObject "
			+ "where wordObject.startTimeInLong >= $1 AND wordObject.endTimeInLong < $2 ORDER BY wordObject.word")
	List<PdxInstance> getWordCountInTimeWindowOrderByWord(Long startTimeInLong, Long endTimeInLong);
}
