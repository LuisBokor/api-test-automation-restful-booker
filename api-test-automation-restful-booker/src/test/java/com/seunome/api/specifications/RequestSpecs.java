package com.seunome.api.specifications;

import com.seunome.api.config.Configuration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestSpecs {

  public static RequestSpecification defaultSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(Configuration.baseUrl())
        .setContentType(ContentType.JSON)
        .setAccept("application/json")
        .addFilter(new AllureRestAssured())
        .log(LogDetail.URI)
        .build();
  }
}
