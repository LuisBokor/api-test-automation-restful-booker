package com.seunome.api.services;

import com.seunome.api.factories.AuthRequestFactory;
import com.seunome.api.models.AuthRequest;
import com.seunome.api.specifications.ResponseSpecs;
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
