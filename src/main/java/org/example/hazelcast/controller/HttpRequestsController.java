package org.example.hazelcast.controller;

import static java.time.ZonedDateTime.now;
import static org.springframework.http.HttpStatus.OK;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HttpRequestsController {

  private int endpointCallCount = 0;

  @GetMapping("/")
  public ResponseEntity<ResponseBody> handleRequest(@RequestParam("msg") @NonNull String msg) {
    this.endpointCallCount++;
    final ResponseBody responseBody =
        ResponseBody.builder()
            .endpointCallCount(endpointCallCount)
            .timestamp(now())
            .message(msg)
            .build();

    log.info("Successfully handled HTTP request, body={}", responseBody);
    return new ResponseEntity<>(responseBody, OK);
  }

  @Data
  @Builder
  static class ResponseBody {
    private ZonedDateTime timestamp;
    private Integer endpointCallCount;
    private String message;
  }
}
