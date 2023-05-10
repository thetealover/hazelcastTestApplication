package org.example.hazelcast.controller;

import static java.time.ZonedDateTime.now;
import static java.util.Optional.ofNullable;

import com.hazelcast.cache.HazelcastExpiryPolicy;
import com.hazelcast.cache.ICache;
import com.hazelcast.map.IMap;
import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HttpRequestsController {
  private static final String ENDPOINT_CALL_COUNT = "endpointCallCount";
  private final IMap<String, Integer> hazelcastEndpointCallCountMap;
  private final ICache<String, Integer> hazelcastEndpointCallCountCache;
  private final HazelcastExpiryPolicy defaultHazelcastExpiryPolicy;

  @GetMapping("/cached")
  public ResponseDto handleRequestWIthCache(
      @RequestParam(value = "msg", required = false) final String msg) {
    Integer endpointCallCount =
        ofNullable(hazelcastEndpointCallCountCache.get(ENDPOINT_CALL_COUNT)).orElse(0);
    endpointCallCount++;
    hazelcastEndpointCallCountCache.put(
        ENDPOINT_CALL_COUNT, endpointCallCount, defaultHazelcastExpiryPolicy);

    final ResponseDto response =
        ResponseDto.builder()
            .endpointCallCount(endpointCallCount)
            .timestamp(now())
            .message(msg)
            .build();

    log.info("Successfully handled HTTP request using embedded cache, body={}", response);
    return response;
  }

  @GetMapping("/shared")
  public ResponseDto handleRequestWithSharedMap(
      @RequestParam(value = "msg", required = false) final String msg) {
    Integer endpointCallCount =
        ofNullable(hazelcastEndpointCallCountMap.get(ENDPOINT_CALL_COUNT)).orElse(0);
    endpointCallCount++;
    hazelcastEndpointCallCountMap.put(ENDPOINT_CALL_COUNT, endpointCallCount);

    final ResponseDto response =
        ResponseDto.builder()
            .endpointCallCount(endpointCallCount)
            .timestamp(now())
            .message(msg)
            .build();

    log.info("Successfully handled HTTP request using shared IMap, body={}", response);
    return response;
  }

  @Data
  @Builder
  static class ResponseDto {
    private ZonedDateTime timestamp;
    private Integer endpointCallCount;
    private String message;
  }
}
