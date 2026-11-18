package com.seunome.api.tests;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

import com.seunome.api.config.ApiMessages;
import com.seunome.api.factories.AuthRequestFactory;
import com.seunome.api.models.AuthRequest;
import com.seunome.api.specifications.ResponseSpecs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.stream.Stream;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Epic("Restful Booker")
@Feature("Authentication")
@Tag("regression")
class AuthNegativeTest extends BaseTest {

  static Stream<Arguments> invalidCredentials() {
    return Stream.of(
        Arguments.of("invalid username", AuthRequestFactory.invalidUsername()),
        Arguments.of("invalid password", AuthRequestFactory.invalidPassword()),
        Arguments.of("empty username", AuthRequestFactory.emptyUsername()),
        Arguments.of("empty password", AuthRequestFactory.emptyPassword()),
        Arguments.of("empty payload", AuthRequestFactory.emptyPayload()));
  }

  // The API reports authentication failures with HTTP 200 and a "reason" field instead of 401
  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidCredentials")
  void shouldRejectInvalidCredentials(String scenario, AuthRequest credentials) {
    authService
        .createToken(credentials)
        .then()
        .spec(ResponseSpecs.ok())
        .body("reason", equalTo(ApiMessages.BAD_CREDENTIALS))
        .body("token", nullValue());
  }
}
