package com.seunome.api.services;

import com.seunome.api.models.Booking;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class BookingService extends BaseService {

  private static final String BOOKING = "/booking";
  private static final String BOOKING_BY_ID = "/booking/{id}";

  @Step("Create booking")
  public Response create(Booking booking) {
    return request().body(booking).post(BOOKING);
  }

  @Step("List booking ids")
  public Response list() {
    return retryGet(() -> request().get(BOOKING));
  }

  @Step("Get booking {id}")
  public Response getById(int id) {
    return retryGet(() -> request().get(BOOKING_BY_ID, id));
  }

  @Step("Update booking {id}")
  public Response update(int id, Booking booking, String token) {
    return authenticated(token).body(booking).put(BOOKING_BY_ID, id);
  }

  @Step("Partially update booking {id}")
  public Response partialUpdate(int id, Booking booking, String token) {
    return authenticated(token).body(booking).patch(BOOKING_BY_ID, id);
  }

  @Step("Delete booking {id}")
  public Response delete(int id, String token) {
    return authenticated(token).delete(BOOKING_BY_ID, id);
  }

  /** A null token sends the request without the auth cookie. */
  private RequestSpecification authenticated(String token) {
    return token == null ? request() : request().cookie("token", token);
  }
}
