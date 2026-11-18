package com.seunome.api.factories;

import com.github.javafaker.Faker;
import com.seunome.api.models.Booking;
import com.seunome.api.models.BookingDates;
import com.seunome.api.utils.DateUtils;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BookingFactory {

  private static final Faker FAKER = new Faker();

  public static Booking validBooking() {
    return validBookingBuilder().build();
  }

  public static Booking.BookingBuilder validBookingBuilder() {
    LocalDate checkin = LocalDate.now().plusDays(FAKER.number().numberBetween(1, 30));
    LocalDate checkout = checkin.plusDays(FAKER.number().numberBetween(1, 15));
    return Booking.builder()
        .firstname(FAKER.name().firstName())
        .lastname(FAKER.name().lastName())
        .totalprice(FAKER.number().numberBetween(50, 2000))
        .depositpaid(FAKER.bool().bool())
        .bookingdates(datesBetween(checkin, checkout))
        .additionalneeds(FAKER.options().option("Breakfast", "Late checkout", "Parking", "None"));
  }

  public static Booking withCheckoutBeforeCheckin() {
    LocalDate checkin = LocalDate.now().plusDays(10);
    return validBookingBuilder().bookingdates(datesBetween(checkin, checkin.minusDays(5))).build();
  }

  public static Booking partialUpdate() {
    return Booking.builder()
        .firstname(FAKER.name().firstName())
        .lastname(FAKER.name().lastName())
        .build();
  }

  private static BookingDates datesBetween(LocalDate checkin, LocalDate checkout) {
    return new BookingDates(DateUtils.format(checkin), DateUtils.format(checkout));
  }
}
