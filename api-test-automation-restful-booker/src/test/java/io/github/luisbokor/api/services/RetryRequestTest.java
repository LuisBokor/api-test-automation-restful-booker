package io.github.luisbokor.api.services;

import static org.assertj.core.api.Assertions.assertThat;

import com.sun.net.httpserver.HttpServer;
import io.restassured.response.Response;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

@ResourceLock(Resources.SYSTEM_PROPERTIES)
class RetryRequestTest {

  private static final String RETRY_ATTEMPTS_PROPERTY = "http.retry.max.attempts";
  private static final String RETRY_DELAY_PROPERTY = "http.retry.delay.ms";

  private final AtomicInteger requestCount = new AtomicInteger();
  private HttpServer server;
  private String previousAttempts;
  private String previousDelay;

  @BeforeEach
  void startServer() throws IOException {
    previousAttempts = System.getProperty(RETRY_ATTEMPTS_PROPERTY);
    previousDelay = System.getProperty(RETRY_DELAY_PROPERTY);
    System.setProperty(RETRY_ATTEMPTS_PROPERTY, "2");
    System.setProperty(RETRY_DELAY_PROPERTY, "1");

    server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
    server.createContext(
        "/retry",
        exchange -> {
          int status = requestCount.incrementAndGet() == 1 ? 503 : 200;
          byte[] response = "result".getBytes(StandardCharsets.UTF_8);
          exchange.sendResponseHeaders(status, response.length);
          try (var responseBody = exchange.getResponseBody()) {
            responseBody.write(response);
          }
        });
    server.start();
  }

  @AfterEach
  void stopServer() {
    server.stop(0);
    restoreProperty(RETRY_ATTEMPTS_PROPERTY, previousAttempts);
    restoreProperty(RETRY_DELAY_PROPERTY, previousDelay);
  }

  @Test
  void shouldRetryTransientGetResponse() {
    Response response = new TestService().getRetrying(baseUrl());

    assertThat(response.statusCode()).isEqualTo(200);
    assertThat(requestCount).hasValue(2);
  }

  @Test
  void shouldNotRetryPostRequest() {
    Response response = new TestService().postWithoutRetry(baseUrl());

    assertThat(response.statusCode()).isEqualTo(503);
    assertThat(requestCount).hasValue(1);
  }

  private String baseUrl() {
    return "http://localhost:" + server.getAddress().getPort() + "/retry";
  }

  private static void restoreProperty(String key, String previousValue) {
    if (previousValue == null) {
      System.clearProperty(key);
    } else {
      System.setProperty(key, previousValue);
    }
  }

  private static class TestService extends BaseService {

    private Response getRetrying(String url) {
      return retryGet(() -> request().get(url));
    }

    private Response postWithoutRetry(String url) {
      return request().post(url);
    }
  }
}
