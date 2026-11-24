package io.github.luisbokor.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

/** Null fields are omitted, which allows the same model to be used for partial updates. */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public record Booking(
    String firstname,
    String lastname,
    Integer totalprice,
    Boolean depositpaid,
    BookingDates bookingdates,
    String additionalneeds) {}
