package io.github.luisbokor.api.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

import io.github.luisbokor.api.models.Booking;
import io.github.luisbokor.api.models.BookingResponse;
import io.github.luisbokor.api.services.AuthService;
import io.github.luisbokor.api.services.BookingService;
import io.github.luisbokor.api.specifications.JsonSchemas;
import io.github.luisbokor.api.specifications.ResponseSpecs;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;

abstract class BaseTest {

  private static final System.Logger LOGGER = System.getLogger(BaseTest.class.getName());

  protected static final int NON_EXISTENT_BOOKING_ID = 999_999_999;

  protected final AuthService authService = new AuthService();
  protected final BookingService bookingService = new BookingService();
  private final List<Integer> createdBookingIds = new ArrayList<>();
  private String authenticatedToken;

  protected BookingResponse createBooking(Booking booking) {
    Response response = bookingService.create(booking);
    response.then().spec(ResponseSpecs.ok());
    int bookingId = response.path("bookingid");
    if (bookingId > 0) {
      createdBookingIds.add(bookingId);
    }
    response.then().body(matchesJsonSchemaInClasspath(JsonSchemas.CREATE_BOOKING_RESPONSE));
    return response.as(BookingResponse.class);
  }

  protected String authToken() {
    if (authenticatedToken == null) {
      authenticatedToken = authService.validToken();
    }
    return authenticatedToken;
  }

  protected void forgetBooking(int bookingId) {
    createdBookingIds.remove(Integer.valueOf(bookingId));
  }

  @AfterEach
  void cleanUpCreatedBookings() {
    if (createdBookingIds.isEmpty()) {
      return;
    }

    String token;
    try {
      token = authToken();
    } catch (RuntimeException exception) {
      LOGGER.log(
          System.Logger.Level.ERROR, "Could not authenticate for booking cleanup", exception);
      throw new AssertionError("Could not authenticate for booking cleanup", exception);
    }
    List<String> failures = new ArrayList<>();
    for (int bookingId : createdBookingIds) {
      try {
        Response response = bookingService.delete(bookingId, token);
        if (response.statusCode() == 201) {
          LOGGER.log(System.Logger.Level.INFO, "Removed test booking {0}", bookingId);
        } else {
          String failure =
              "Could not remove test booking %d: HTTP %d %s"
                  .formatted(bookingId, response.statusCode(), response.asString());
          failures.add(failure);
          LOGGER.log(System.Logger.Level.ERROR, failure);
        }
      } catch (RuntimeException exception) {
        String failure =
            "Could not remove test booking %d: %s".formatted(bookingId, exception.getMessage());
        failures.add(failure);
        LOGGER.log(System.Logger.Level.ERROR, failure, exception);
      }
    }
    createdBookingIds.clear();

    if (!failures.isEmpty()) {
      throw new AssertionError("Booking cleanup failed: " + String.join("; ", failures));
    }
  }
}
