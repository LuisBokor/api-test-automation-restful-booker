package io.github.luisbokor.api.services;

import io.github.luisbokor.api.factories.AuthRequestFactory;
import io.github.luisbokor.api.models.AuthRequest;
import io.github.luisbokor.api.specifications.ResponseSpecs;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class AuthService extends BaseService {

  @Step("Create auth token")
  public Response createToken(AuthRequest credentials) {
    return request().body(credentials).post("/auth");
  }

  public String validToken() {
    return createToken(AuthRequestFactory.validCredentials())
        .then()
        .spec(ResponseSpecs.ok())
        .extract()
        .path("token");
  }
}
