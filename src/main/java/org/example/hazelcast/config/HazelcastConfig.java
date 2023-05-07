package org.example.hazelcast.config;

import static com.hazelcast.core.Hazelcast.newHazelcastInstance;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {
  public static final String ENDPOINT_CALL_COUNT_MAP = "endpointCallCountMap";
  private final HazelcastInstance hazelcastInstance;

  public HazelcastConfig() {
    this.hazelcastInstance = newHazelcastInstance();
  }

  @Bean
  public HazelcastInstance hazelcastInstance() {
    return this.hazelcastInstance;
  }

  @Bean
  public IMap<String, Integer> hazelcastEndpointCallCountMap() {
    return hazelcastInstance.getMap(ENDPOINT_CALL_COUNT_MAP);
  }
}
