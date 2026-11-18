package com.seunome.api.tests;

import com.seunome.api.models.Booking;
import com.seunome.api.models.BookingResponse;
import com.seunome.api.services.AuthService;
import com.seunome.api.services.BookingService;
import com.seunome.api.specifications.ResponseSpecs;

abstract class BaseTest {

  protected static final int NON_EXISTENT_BOOKING_ID = 999_999_999;

  protected final AuthService authService = new AuthService();
  protected final BookingService bookingService = new BookingService();

  protected BookingResponse createBooking(Booking booking) {
    return bookingService
        .create(booking)
        .then()
        .spec(ResponseSpecs.ok())
        .extract()
        .as(BookingResponse.class);
  }
}
