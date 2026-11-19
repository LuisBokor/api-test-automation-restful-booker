package io.github.luisbokor.api.factories;

import com.github.javafaker.Faker;
import io.github.luisbokor.api.models.Booking;
import io.github.luisbokor.api.models.BookingDates;
import io.github.luisbokor.api.utils.DateUtils;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingFactory {

  public static Booking validBooking() {
    return validBookingBuilder().build();
  }

  public static Booking.BookingBuilder validBookingBuilder() {
    Faker faker = new Faker();
    LocalDate checkin = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
    LocalDate checkout = checkin.plusDays(faker.number().numberBetween(1, 15));
    return Booking.builder()
        .firstname(faker.name().firstName())
        .lastname(faker.name().lastName())
        .totalprice(faker.number().numberBetween(50, 2000))
        .depositpaid(faker.bool().bool())
        .bookingdates(datesBetween(checkin, checkout))
        .additionalneeds(faker.options().option("Breakfast", "Late checkout", "Parking", "None"));
  }

  public static Booking withCheckoutBeforeCheckin() {
    LocalDate checkin = LocalDate.now().plusDays(10);
    return validBookingBuilder().bookingdates(datesBetween(checkin, checkin.minusDays(5))).build();
  }

  public static Booking partialUpdate() {
    Faker faker = new Faker();
    return Booking.builder()
        .firstname(faker.name().firstName())
        .lastname(faker.name().lastName())
        .build();
  }

  private static BookingDates datesBetween(LocalDate checkin, LocalDate checkout) {
    return new BookingDates(DateUtils.format(checkin), DateUtils.format(checkout));
  }
}
