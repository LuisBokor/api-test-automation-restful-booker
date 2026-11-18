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
  private static final Properties FILE_PROPERTIES = load();

  public static String baseUrl() {
    return get("base.url");
  }

  public static String username() {
    return get("auth.username");
  }

  public static String password() {
    return get("auth.password");
  }

  private static String get(String key) {
    String fromSystem = System.getProperty(key);
    if (fromSystem != null) {
      return fromSystem;
    }
    String fromEnvironment = System.getenv(key.toUpperCase().replace('.', '_'));
    if (fromEnvironment != null) {
      return fromEnvironment;
    }
    String fromFile = FILE_PROPERTIES.getProperty(key);
    if (fromFile == null) {
      throw new IllegalStateException("Missing configuration: " + key);
    }
    return fromFile;
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
