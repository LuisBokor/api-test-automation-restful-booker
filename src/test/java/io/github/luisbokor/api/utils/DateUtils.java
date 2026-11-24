package io.github.luisbokor.api.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class DateUtils {

  private static final DateTimeFormatter API_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;

  public static String format(LocalDate date) {
    return API_FORMAT.format(date);
  }
}
