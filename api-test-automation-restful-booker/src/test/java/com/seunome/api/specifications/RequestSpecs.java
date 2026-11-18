package com.seunome.api.specifications;

import com.seunome.api.config.Configuration;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.http.params.CoreConnectionPNames;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestSpecs {

  public static RequestSpecification defaultSpec() {
    return new RequestSpecBuilder()
        .setBaseUri(Configuration.baseUrl())
        .setContentType(ContentType.JSON)
        .setAccept("application/json")
        .setConfig(
            RestAssuredConfig.config()
                .httpClient(
                    HttpClientConfig.httpClientConfig()
                        .setParam(
                            CoreConnectionPNames.CONNECTION_TIMEOUT,
                            Configuration.connectionTimeoutMs())
                        .setParam(
                            CoreConnectionPNames.SO_TIMEOUT, Configuration.socketTimeoutMs())))
        .addFilter(new AllureRestAssured())
        .log(LogDetail.URI)
        .build();
  }
}
