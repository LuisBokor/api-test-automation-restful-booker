package com.seunome.api.specifications;

import static org.hamcrest.Matchers.lessThan;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ResponseSpecs {

  private static final int OK = 200;
  private static final int CREATED = 201;
  private static final int BAD_REQUEST = 400;
  private static final int FORBIDDEN = 403;
  private static final int NOT_FOUND = 404;
  private static final int METHOD_NOT_ALLOWED = 405;
  private static final int INTERNAL_SERVER_ERROR = 500;
  private static final long MAX_RESPONSE_TIME_MS = 10_000;

  public static ResponseSpecification ok() {
    return base(OK).expectContentType(ContentType.JSON).build();
  }

  public static ResponseSpecification created() {
    return plainText(CREATED);
  }

  public static ResponseSpecification badRequest() {
    return plainText(BAD_REQUEST);
  }

  public static ResponseSpecification forbidden() {
    return plainText(FORBIDDEN);
  }

  public static ResponseSpecification notFound() {
    return plainText(NOT_FOUND);
  }

  public static ResponseSpecification methodNotAllowed() {
    return plainText(METHOD_NOT_ALLOWED);
  }

  public static ResponseSpecification internalServerError() {
    return plainText(INTERNAL_SERVER_ERROR);
  }

  private static ResponseSpecification plainText(int status) {
    return base(status).expectContentType(ContentType.TEXT).build();
  }

  private static ResponseSpecBuilder base(int status) {
    return new ResponseSpecBuilder()
        .expectStatusCode(status)
        .expectResponseTime(lessThan(MAX_RESPONSE_TIME_MS), TimeUnit.MILLISECONDS);
  }
}
