package com.seunome.api.config;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApiMessages {

  public static final String BAD_CREDENTIALS = "Bad credentials";
  public static final String FORBIDDEN = "Forbidden";
  public static final String NOT_FOUND = "Not Found";
  public static final String METHOD_NOT_ALLOWED = "Method Not Allowed";
  public static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
}
