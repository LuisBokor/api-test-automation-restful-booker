package io.github.luisbokor.api.factories;

import io.github.luisbokor.api.config.Configuration;
import io.github.luisbokor.api.models.AuthRequest;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AuthRequestFactory {

  private static final String INVALID_VALUE = "invalid";

  public static AuthRequest validCredentials() {
    return new AuthRequest(Configuration.username(), Configuration.password());
  }

  public static AuthRequest invalidUsername() {
    return new AuthRequest(INVALID_VALUE, Configuration.password());
  }

  public static AuthRequest invalidPassword() {
    return new AuthRequest(Configuration.username(), INVALID_VALUE);
  }

  public static AuthRequest emptyUsername() {
    return new AuthRequest("", Configuration.password());
  }

  public static AuthRequest emptyPassword() {
    return new AuthRequest(Configuration.username(), "");
  }

  public static AuthRequest emptyPayload() {
    return new AuthRequest(null, null);
  }
}
