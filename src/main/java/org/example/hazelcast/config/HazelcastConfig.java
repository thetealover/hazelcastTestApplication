package org.example.hazelcast.config;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;
import static java.util.concurrent.TimeUnit.SECONDS;
import static javax.cache.Caching.getCachingProvider;

import com.hazelcast.cache.HazelcastExpiryPolicy;
import com.hazelcast.cache.ICache;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import javax.cache.spi.CachingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {
  public static final String ENDPOINT_CALL_COUNT_MAP = "endpointCallCountMap";
  public static final String ENDPOINT_CALL_COUNT_CACHE = "endpointCallCountCache";
  private final HazelcastInstance hazelcastInstance;
  private final Integer defaultCreateDurationAmount;
  private final Integer defaultAccessDurationAmount;
  private final Integer defaultUpdateDurationAmount;

  public HazelcastConfig(
      @Value("${hazelcast-test-application.cache.duration.create}")
          final Integer defaultCreateDurationAmount,
      @Value("${hazelcast-test-application.cache.duration.access}")
          final Integer defaultAccessDurationAmount,
      @Value("${hazelcast-test-application.cache.duration.update}")
          final Integer defaultUpdateDurationAmount) {
    this.hazelcastInstance = newHazelcastInstance();
    this.defaultCreateDurationAmount = defaultCreateDurationAmount;
    this.defaultAccessDurationAmount = defaultAccessDurationAmount;
    this.defaultUpdateDurationAmount = defaultUpdateDurationAmount;
  }

  @Bean
  public HazelcastInstance hazelcastInstance() {
    return this.hazelcastInstance;
  }

  @Bean
  public IMap<String, Integer> hazelcastEndpointCallCountMap() {
    return hazelcastInstance.getMap(ENDPOINT_CALL_COUNT_MAP);
  }

  @Bean
  public ICache<String, Integer> hazelcastEndpointCallCountCache() {
    final CachingProvider cachingProvider = getCachingProvider();
    final CacheManager cacheManager = cachingProvider.getCacheManager();
    final MutableConfiguration<String, Integer> configuration = new MutableConfiguration<>();

    return (ICache<String, Integer>)
        cacheManager.createCache(ENDPOINT_CALL_COUNT_CACHE, configuration);
  }

  @Bean
  public HazelcastExpiryPolicy defaultHazelcastExpiryPolicy() {
    return new HazelcastExpiryPolicy(
        defaultCreateDurationAmount,
        defaultAccessDurationAmount,
        defaultUpdateDurationAmount,
        SECONDS);
  }
}
