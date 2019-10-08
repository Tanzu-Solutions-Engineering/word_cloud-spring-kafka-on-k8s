package io.pivotal.demo.cacheserver.repo;


import io.pivotal.demo.domain.WordCount;
import org.apache.geode.pdx.PdxInstance;
import org.springframework.data.gemfire.repository.GemfireRepository;
import org.springframework.data.gemfire.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordCountRepo extends GemfireRepository <WordCount, String> {

    @Query("SELECT * FROM /Words_Count wordObject order by wordObject.wordCount desc limit 200")
    List<PdxInstance> findTop200();
}