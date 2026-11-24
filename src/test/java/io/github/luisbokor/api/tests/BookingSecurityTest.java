package io.github.luisbokor.api.tests;

import static org.hamcrest.Matchers.equalTo;

import io.github.luisbokor.api.config.ApiMessages;
import io.github.luisbokor.api.factories.BookingFactory;
import io.github.luisbokor.api.specifications.ResponseSpecs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

/** A null token means the request is sent without the auth cookie. */
@Epic("Restful Booker")
@Feature("Booking security")
@Tag("security")
@Tag("regression")
class BookingSecurityTest extends BaseTest {

  private int bookingId;

  @BeforeEach
  void createBookingToProtect() {
    bookingId = createBooking(BookingFactory.validBooking()).bookingid();
  }

  @ParameterizedTest(name = "token: {0}")
  @NullSource
  @ValueSource(strings = "invalid-token")
  void shouldForbidUpdateWithoutValidToken(String token) {
    bookingService
        .update(bookingId, BookingFactory.validBooking(), token)
        .then()
        .spec(ResponseSpecs.forbidden())
        .body(equalTo(ApiMessages.FORBIDDEN));
  }

  @ParameterizedTest(name = "token: {0}")
  @NullSource
  @ValueSource(strings = "invalid-token")
  void shouldForbidPartialUpdateWithoutValidToken(String token) {
    bookingService
        .partialUpdate(bookingId, BookingFactory.partialUpdate(), token)
        .then()
        .spec(ResponseSpecs.forbidden())
        .body(equalTo(ApiMessages.FORBIDDEN));
  }

  @ParameterizedTest(name = "token: {0}")
  @NullSource
  @ValueSource(strings = "invalid-token")
  void shouldForbidDeleteWithoutValidToken(String token) {
    bookingService
        .delete(bookingId, token)
        .then()
        .spec(ResponseSpecs.forbidden())
        .body(equalTo(ApiMessages.FORBIDDEN));

    bookingService.getById(bookingId).then().spec(ResponseSpecs.ok());
  }
}
