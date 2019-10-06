package io.pivotal.demo.config;


import io.pivotal.demo.domain.WordCount;
//import io.pivotal.demo.repositories.WordCountRepo;

import org.springframework.context.annotation.Configuration;
//import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
//import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@Configuration
//@EnableGemfireRepositories(basePackageClasses = WordCountRepo.class)
//@EnableEntityDefinedRegions(basePackageClasses = WordCount.class)
public class CacheConfig {
}
