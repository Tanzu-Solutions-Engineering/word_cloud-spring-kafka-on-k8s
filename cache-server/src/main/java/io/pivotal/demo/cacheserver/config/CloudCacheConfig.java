package io.pivotal.demo.cacheserver.config;

import org.apache.geode.cache.EvictionAttributes;
import org.apache.geode.cache.ExpirationAction;
import org.apache.geode.cache.ExpirationAttributes;
import org.apache.geode.cache.GemFireCache;
import org.apache.geode.cache.RegionShortcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.PartitionedRegionFactoryBean;
import org.springframework.data.gemfire.config.annotation.CacheServerApplication;
import org.springframework.data.gemfire.config.annotation.EnableLocator;
import org.springframework.data.gemfire.config.annotation.EnableManager;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctions;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

import io.pivotal.demo.cacheserver.repo.WordCountWindowedRepo;

@CacheServerApplication(name = "CloudCacheServerApp")
@EnableLocator
@EnableManager
@EnablePdx(readSerialized = true)
@EnableGemfireFunctions
@EnableGemfireRepositories(basePackageClasses = WordCountWindowedRepo.class)
@Configuration
public class CloudCacheConfig {
	
	@Bean("Words_Count")
	public PartitionedRegionFactoryBean<Object, Object> nameRegion(GemFireCache gemfireCache) {

		PartitionedRegionFactoryBean<Object, Object> wordsCountRegion = new PartitionedRegionFactoryBean<>();

		wordsCountRegion.setCache(gemfireCache);
		wordsCountRegion.setClose(false);
		wordsCountRegion.setPersistent(false);
		wordsCountRegion.setStatisticsEnabled(true);
		wordsCountRegion.setEntryTimeToLive(new ExpirationAttributes(11, ExpirationAction.DESTROY));

		return wordsCountRegion;
	}
	
	@Bean("Words_Count_Windowed")
	public PartitionedRegionFactoryBean<Object, Object> pizzaRegion(GemFireCache gemfireCache) {

		PartitionedRegionFactoryBean<Object, Object> wordsCountWindowedRegion = 
				new PartitionedRegionFactoryBean<>();

		wordsCountWindowedRegion.setCache(gemfireCache);
		wordsCountWindowedRegion.setClose(false);
		wordsCountWindowedRegion.setPersistent(false);
		wordsCountWindowedRegion.setShortcut(RegionShortcut.PARTITION_REDUNDANT_HEAP_LRU);
		wordsCountWindowedRegion.setEvictionAttributes(
				EvictionAttributes.createLRUHeapAttributes());

		return wordsCountWindowedRegion;
	}

}
