package io.github.luisbokor.api.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.github.luisbokor.api.factories.AuthRequestFactory;
import io.github.luisbokor.api.specifications.JsonSchemas;
import io.github.luisbokor.api.specifications.ResponseSpecs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("Restful Booker")
@Feature("Authentication")
class AuthTest extends BaseTest {

  @Test
  @Tag("smoke")
  void shouldReturnTokenForValidCredentials() {
    authService
        .createToken(AuthRequestFactory.validCredentials())
        .then()
        .spec(ResponseSpecs.ok())
        .body(matchesJsonSchemaInClasspath(JsonSchemas.AUTH_RESPONSE));
  }
}
