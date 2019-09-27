package io.pivotal.demo.config

import io.pivotal.demo.domain.WordCount
import io.pivotal.demo.repositories.WordCountRepo
import org.springframework.context.annotation.Configuration
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories


//@EnableGemfireFunctionExecutions(basePackageClasses = WindowedWordCountFunction.class)
@EnableGemfireRepositories(basePackageClasses = arrayOf(WordCountRepo::class))
@EnableEntityDefinedRegions(basePackageClasses = arrayOf(WordCount::class))
@Configuration
class CacheConfig
