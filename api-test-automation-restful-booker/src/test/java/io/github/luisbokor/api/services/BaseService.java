package io.github.luisbokor.api.services;

import io.github.luisbokor.api.config.Configuration;
import io.github.luisbokor.api.specifications.RequestSpecs;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.util.Set;
import java.util.function.Supplier;

abstract class BaseService {

  private static final Set<Integer> RETRYABLE_STATUS_CODES = Set.of(429, 502, 503, 504);

  protected RequestSpecification request() {
    return RestAssured.given().spec(RequestSpecs.defaultSpec());
  }

  protected Response retryGet(Supplier<Response> getRequest) {
    int maxAttempts = Configuration.retryMaxAttempts();
    RuntimeException lastException = null;

    for (int attempt = 1; attempt <= maxAttempts; attempt++) {
      try {
        Response response = getRequest.get();
        if (attempt == maxAttempts || !RETRYABLE_STATUS_CODES.contains(response.statusCode())) {
          return response;
        }
      } catch (RuntimeException exception) {
        lastException = exception;
        if (attempt == maxAttempts) {
          throw exception;
        }
      }
      waitBeforeRetry();
    }

    if (lastException != null) {
      throw lastException;
    }
    throw new IllegalStateException("GET retry loop completed without a response");
  }

  private void waitBeforeRetry() {
    try {
      Thread.sleep(Configuration.retryDelayMs());
    } catch (InterruptedException exception) {
      Thread.currentThread().interrupt();
      throw new IllegalStateException("Interrupted while waiting to retry API request", exception);
    }
  }
}
