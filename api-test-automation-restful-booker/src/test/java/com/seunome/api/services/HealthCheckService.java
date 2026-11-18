package com.seunome.api.services;

import io.qameta.allure.Step;
import io.restassured.response.Response;

public class HealthCheckService extends BaseService {

  @Step("Ping API")
  public Response ping() {
    return retryGet(() -> request().get("/ping"));
  }
}
