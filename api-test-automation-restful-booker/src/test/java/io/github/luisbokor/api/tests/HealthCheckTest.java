package io.github.luisbokor.api.tests;

import static org.hamcrest.Matchers.equalTo;

import io.github.luisbokor.api.services.HealthCheckService;
import io.github.luisbokor.api.specifications.ResponseSpecs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("Restful Booker")
@Feature("Health check")
@Tag("smoke")
class HealthCheckTest {

  private final HealthCheckService healthCheckService = new HealthCheckService();

  @Test
  void shouldReportApiIsUp() {
    healthCheckService.ping().then().spec(ResponseSpecs.created()).body(equalTo("Created"));
  }
}
