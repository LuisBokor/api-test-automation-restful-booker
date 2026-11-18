package com.seunome.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Resolves settings in this order: system property, environment variable, config.properties.
 * Example: -Dbase.url=... or BASE_URL=...
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Configuration {

  private static final String CONFIG_FILE = "config.properties";
  private static final String DEMO_API_URL = "https://restful-booker.herokuapp.com";
  private static final Properties FILE_PROPERTIES = load();

  public static String baseUrl() {
    String baseUrl =
        firstConfigured(
            "base.url",
            System.getProperty("base.url"),
            System.getenv("BASE_URL"),
            System.getProperty("test.base.url"),
            FILE_PROPERTIES.getProperty("base.url"));
    if ("prod".equalsIgnoreCase(System.getProperty("test.environment"))
        && !DEMO_API_URL.equalsIgnoreCase(baseUrl)
        && !Boolean.getBoolean("allow.destructive.prod")) {
      throw new IllegalStateException(
          "The test suite creates and deletes bookings. Set -Dallow.destructive.prod=true only "
              + "when destructive tests are explicitly approved for the production target.");
    }
    return baseUrl;
  }

  public static String username() {
    return get("auth.username");
  }

  public static String password() {
    return get("auth.password");
  }

  public static int connectionTimeoutMs() {
    return positiveInteger("http.connection.timeout.ms", 5_000);
  }

  public static int socketTimeoutMs() {
    return positiveInteger("http.socket.timeout.ms", 10_000);
  }

  public static int retryMaxAttempts() {
    return positiveInteger("http.retry.max.attempts", 2);
  }

  public static long retryDelayMs() {
    return positiveLong("http.retry.delay.ms", 250);
  }

  private static String get(String key) {
    return firstConfigured(
        key,
        System.getProperty(key),
        System.getenv(key.toUpperCase().replace('.', '_')),
        FILE_PROPERTIES.getProperty(key));
  }

  private static String firstConfigured(String key, String... values) {
    for (String value : values) {
      if (value != null && !value.isBlank()) {
        return value.trim();
      }
    }
    throw new IllegalStateException("Missing configuration: " + key);
  }

  private static int positiveInteger(String key, int defaultValue) {
    String value = configuredOrDefault(key, defaultValue);
    try {
      int parsed = Integer.parseInt(value);
      if (parsed > 0) {
        return parsed;
      }
    } catch (NumberFormatException exception) {
      throw new IllegalArgumentException(
          "Invalid positive integer for " + key + ": " + value, exception);
    }
    throw new IllegalArgumentException("Configuration " + key + " must be greater than zero");
  }

  private static long positiveLong(String key, long defaultValue) {
    String value = configuredOrDefault(key, defaultValue);
    try {
      long parsed = Long.parseLong(value);
      if (parsed > 0) {
        return parsed;
      }
    } catch (NumberFormatException exception) {
      throw new IllegalArgumentException(
          "Invalid positive integer for " + key + ": " + value, exception);
    }
    throw new IllegalArgumentException("Configuration " + key + " must be greater than zero");
  }

  private static String configuredOrDefault(String key, long defaultValue) {
    String systemValue = System.getProperty(key);
    String environmentValue = System.getenv(key.toUpperCase().replace('.', '_'));
    String fileValue = FILE_PROPERTIES.getProperty(key);
    if (systemValue != null && !systemValue.isBlank()) {
      return systemValue.trim();
    }
    if (environmentValue != null && !environmentValue.isBlank()) {
      return environmentValue.trim();
    }
    if (fileValue != null && !fileValue.isBlank()) {
      return fileValue.trim();
    }
    return Long.toString(defaultValue);
  }

  private static Properties load() {
    try (InputStream input =
        Configuration.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
      if (input == null) {
        throw new IllegalStateException(CONFIG_FILE + " not found on classpath");
      }
      Properties properties = new Properties();
      properties.load(input);
      return properties;
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
