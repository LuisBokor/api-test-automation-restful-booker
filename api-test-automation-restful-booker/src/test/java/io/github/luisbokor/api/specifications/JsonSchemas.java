package io.github.luisbokor.api.specifications;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JsonSchemas {

  public static final String AUTH_RESPONSE = "schemas/auth-response-schema.json";
  public static final String BOOKING = "schemas/booking-schema.json";
  public static final String CREATE_BOOKING_RESPONSE =
      "schemas/create-booking-response-schema.json";
  public static final String BOOKING_IDS = "schemas/booking-id-schema.json";
}
