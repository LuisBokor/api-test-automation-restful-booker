package com.seunome.api.services;

import com.seunome.api.specifications.RequestSpecs;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

abstract class BaseService {

  protected RequestSpecification request() {
    return RestAssured.given().spec(RequestSpecs.defaultSpec());
  }
}
