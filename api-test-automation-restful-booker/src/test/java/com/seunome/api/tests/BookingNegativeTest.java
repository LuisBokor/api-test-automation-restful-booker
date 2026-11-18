package com.seunome.api.tests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

import com.seunome.api.config.ApiMessages;
import com.seunome.api.factories.BookingFactory;
import com.seunome.api.models.Booking;
import com.seunome.api.specifications.ResponseSpecs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Epic("Restful Booker")
@Feature("Booking")
@Tag("regression")
class BookingNegativeTest extends BaseTest {

  private String token;

  @BeforeEach
  void authenticate() {
    token = authToken();
  }

  static Stream<Arguments> invalidBookings() {
    return Stream.of(
        Arguments.of(
            "missing firstname", BookingFactory.validBookingBuilder().firstname(null).build()),
        Arguments.of(
            "missing lastname", BookingFactory.validBookingBuilder().lastname(null).build()),
        Arguments.of("empty payload", Booking.builder().build()));
  }

  @Test
  void shouldReturnNotFoundForNonExistentBooking() {
    bookingService
        .getById(NON_EXISTENT_BOOKING_ID)
        .then()
        .spec(ResponseSpecs.notFound())
        .body(equalTo(ApiMessages.NOT_FOUND));
  }

  // For an unknown id the API answers 405 even with a valid token, not 404
  @Test
  void shouldNotAllowUpdateOfNonExistentBooking() {
    bookingService
        .update(NON_EXISTENT_BOOKING_ID, BookingFactory.validBooking(), token)
        .then()
        .spec(ResponseSpecs.methodNotAllowed())
        .body(equalTo(ApiMessages.METHOD_NOT_ALLOWED));
  }

  @Test
  void shouldNotAllowPartialUpdateOfNonExistentBooking() {
    bookingService
        .partialUpdate(NON_EXISTENT_BOOKING_ID, BookingFactory.partialUpdate(), token)
        .then()
        .spec(ResponseSpecs.methodNotAllowed())
        .body(equalTo(ApiMessages.METHOD_NOT_ALLOWED));
  }

  @Test
  void shouldNotAllowDeleteOfNonExistentBooking() {
    bookingService
        .delete(NON_EXISTENT_BOOKING_ID, token)
        .then()
        .spec(ResponseSpecs.methodNotAllowed())
        .body(equalTo(ApiMessages.METHOD_NOT_ALLOWED));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("invalidBookings")
  void shouldFailToCreateBookingWithInvalidPayload(String scenario, Booking booking) {
    bookingService
        .create(booking)
        .then()
        .spec(ResponseSpecs.internalServerError())
        .body(equalTo(ApiMessages.INTERNAL_SERVER_ERROR));
  }

  // Known API defect: no validation that checkout is after checkin
  @Test
  void shouldAcceptBookingWithCheckoutBeforeCheckin() {
    Booking booking = BookingFactory.withCheckoutBeforeCheckin();
    Booking created = createBooking(booking).booking();

    assertThat(created.bookingdates().checkin()).isEqualTo(booking.bookingdates().checkin());
    assertThat(created.bookingdates().checkout()).isEqualTo(booking.bookingdates().checkout());
  }
}
