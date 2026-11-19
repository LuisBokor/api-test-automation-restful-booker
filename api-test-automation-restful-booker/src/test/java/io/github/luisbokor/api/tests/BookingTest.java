package io.github.luisbokor.api.tests;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;

import io.github.luisbokor.api.factories.BookingFactory;
import io.github.luisbokor.api.models.Booking;
import io.github.luisbokor.api.models.BookingId;
import io.github.luisbokor.api.models.BookingResponse;
import io.github.luisbokor.api.specifications.JsonSchemas;
import io.github.luisbokor.api.specifications.ResponseSpecs;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Epic("Restful Booker")
@Feature("Booking")
class BookingTest extends BaseTest {

  private String token;

  @BeforeEach
  void authenticate() {
    token = authToken();
  }

  @Test
  @Tag("smoke")
  void shouldCreateBooking() {
    Booking booking = BookingFactory.validBooking();

    BookingResponse created = createBooking(booking);

    assertThat(created.bookingid()).isPositive();
    assertThat(created.booking()).isEqualTo(booking);
  }

  @Test
  @Tag("regression")
  void shouldGetCreatedBookingById() {
    Booking booking = BookingFactory.validBooking();
    int id = createBooking(booking).bookingid();

    Booking retrieved =
        bookingService
            .getById(id)
            .then()
            .spec(ResponseSpecs.ok())
            .body(matchesJsonSchemaInClasspath(JsonSchemas.BOOKING))
            .extract()
            .as(Booking.class);

    assertThat(retrieved).isEqualTo(booking);
  }

  @Test
  @Tag("regression")
  void shouldListCreatedBookingId() {
    int id = createBooking(BookingFactory.validBooking()).bookingid();

    List<BookingId> ids =
        bookingService
            .list()
            .then()
            .spec(ResponseSpecs.ok())
            .body(matchesJsonSchemaInClasspath(JsonSchemas.BOOKING_IDS))
            .extract()
            .jsonPath()
            .getList(".", BookingId.class);

    assertThat(ids).contains(new BookingId(id));
  }

  @Test
  @Tag("regression")
  void shouldUpdateBooking() {
    int id = createBooking(BookingFactory.validBooking()).bookingid();
    Booking updated = BookingFactory.validBooking();

    Booking result =
        bookingService
            .update(id, updated, token)
            .then()
            .spec(ResponseSpecs.ok())
            .extract()
            .as(Booking.class);

    assertThat(result).isEqualTo(updated);
  }

  @Test
  @Tag("regression")
  void shouldPartiallyUpdateBooking() {
    Booking original = BookingFactory.validBooking();
    int id = createBooking(original).bookingid();
    Booking changes = BookingFactory.partialUpdate();

    Booking result =
        bookingService
            .partialUpdate(id, changes, token)
            .then()
            .spec(ResponseSpecs.ok())
            .extract()
            .as(Booking.class);

    assertThat(result.firstname()).isEqualTo(changes.firstname());
    assertThat(result.lastname()).isEqualTo(changes.lastname());
    assertThat(result.totalprice()).isEqualTo(original.totalprice());
  }

  @Test
  @Tag("regression")
  void shouldDeleteBooking() {
    int id = createBooking(BookingFactory.validBooking()).bookingid();

    bookingService.delete(id, token).then().spec(ResponseSpecs.created());
    forgetBooking(id);

    bookingService.getById(id).then().spec(ResponseSpecs.notFound());
  }
}
