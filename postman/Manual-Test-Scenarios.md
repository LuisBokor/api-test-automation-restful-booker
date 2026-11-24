# Manual Test Scenarios

Manual equivalents of the automated suite. Each scenario can be executed with the Postman collection in this folder (`RestfulBooker.postman_collection.json`) and the environment `RestfulBooker.postman_environment.json`.

**Base URL:** `https://restful-booker.herokuapp.com`

**Conventions**

- `{token}` is obtained in TC-AUTH-01 and sent as the header `Cookie: token={token}`.
- `{bookingId}` is obtained in TC-CRUD-01.
- Send `Content-Type: application/json` and `Accept: application/json` on requests with a body. The API answers `418` when `Accept` lists several media types, so keep it literal.
- Expected results describe the **observed behavior** of the API, including behavior that deviates from REST conventions.

**Execution order:** Health Check → Authentication → Booking CRUD → Negative → Security.

## Health Check

| ID | Scenario | Steps | Expected result | Automated in |
|---|---|---|---|---|
| TC-HEALTH-01 | API is available | `GET /ping` | `201`, body `Created` | `HealthCheckTest.shouldReportApiIsUp` |

## Authentication

| ID | Scenario | Steps | Expected result | Automated in |
|---|---|---|---|---|
| TC-AUTH-01 | Token for valid credentials | `POST /auth` with `{"username":"admin","password":"password123"}` | `200`, body has a non-empty `token` and no other field (matches `auth-response-schema.json`) | `AuthTest.shouldReturnTokenForValidCredentials` |

## Booking CRUD

| ID | Scenario | Steps | Expected result | Automated in |
|---|---|---|---|---|
| TC-CRUD-01 | Create booking | `POST /booking` with a valid payload | `200`, `bookingid` is a positive integer, `booking` equals the payload (matches `create-booking-response-schema.json`) | `BookingTest.shouldCreateBooking` |
| TC-CRUD-02 | Get booking by id | `GET /booking/{bookingId}` | `200`, body equals the created booking (matches `booking-schema.json`) | `BookingTest.shouldGetCreatedBookingById` |
| TC-CRUD-03 | List booking ids | `GET /booking` | `200`, array of `{"bookingid": n}` containing `{bookingId}` (matches `booking-id-schema.json`) | `BookingTest.shouldListCreatedBookingId` |
| TC-CRUD-04 | Update booking | `PUT /booking/{bookingId}` with a new full payload and token | `200`, body equals the new payload | `BookingTest.shouldUpdateBooking` |
| TC-CRUD-05 | Partial update | `PATCH /booking/{bookingId}` with only `firstname` and `lastname` and token | `200`, both fields changed, `totalprice` unchanged | `BookingTest.shouldPartiallyUpdateBooking` |
| TC-CRUD-06 | Delete booking | `DELETE /booking/{bookingId}` with token, then `GET /booking/{bookingId}` | Delete: `201`, body `Created`. Get: `404` | `BookingTest.shouldDeleteBooking` |

## Negative: Authentication

Every scenario below uses `POST /auth`.

| ID | Scenario | Payload | Expected result | Automated in |
|---|---|---|---|---|
| TC-AUTHNEG-01 | Invalid username | `{"username":"invalid","password":"password123"}` | `200`, `{"reason":"Bad credentials"}`, no `token` | `AuthNegativeTest` (parameterized) |
| TC-AUTHNEG-02 | Invalid password | `{"username":"admin","password":"invalid"}` | same as above | `AuthNegativeTest` |
| TC-AUTHNEG-03 | Empty username | `{"username":"","password":"password123"}` | same as above | `AuthNegativeTest` |
| TC-AUTHNEG-04 | Empty password | `{"username":"admin","password":""}` | same as above | `AuthNegativeTest` |
| TC-AUTHNEG-05 | Empty payload | `{}` | same as above | `AuthNegativeTest` |

> The API reports authentication failure with HTTP `200` instead of `401`.

## Negative: Booking

| ID | Scenario | Steps | Expected result | Automated in |
|---|---|---|---|---|
| TC-BOOKNEG-01 | Get non-existent booking | `GET /booking/999999999` | `404`, body `Not Found` | `BookingNegativeTest.shouldReturnNotFoundForNonExistentBooking` |
| TC-BOOKNEG-02 | Update non-existent booking | `PUT /booking/999999999` with valid payload and valid token | `405`, body `Method Not Allowed` | `BookingNegativeTest.shouldNotAllowUpdateOfNonExistentBooking` |
| TC-BOOKNEG-03 | Partial update non-existent booking | `PATCH /booking/999999999` with valid token | `405`, body `Method Not Allowed` | `BookingNegativeTest.shouldNotAllowPartialUpdateOfNonExistentBooking` |
| TC-BOOKNEG-04 | Delete non-existent booking | `DELETE /booking/999999999` with valid token | `405`, body `Method Not Allowed` | `BookingNegativeTest.shouldNotAllowDeleteOfNonExistentBooking` |
| TC-BOOKNEG-05 | Create without `firstname` | `POST /booking` omitting `firstname` | `500`, body `Internal Server Error` | `BookingNegativeTest.shouldFailToCreateBookingWithInvalidPayload` |
| TC-BOOKNEG-06 | Create without `lastname` | `POST /booking` omitting `lastname` | `500`, body `Internal Server Error` | `BookingNegativeTest.shouldFailToCreateBookingWithInvalidPayload` |
| TC-BOOKNEG-07 | Create with empty payload | `POST /booking` with `{}` | `500`, body `Internal Server Error` | `BookingNegativeTest.shouldFailToCreateBookingWithInvalidPayload` |
| TC-BOOKNEG-08 | Checkout before checkin | `POST /booking` with `checkout` earlier than `checkin` | `200`, dates stored as sent (known defect: no date validation) | `BookingNegativeTest.shouldAcceptBookingWithCheckoutBeforeCheckin` |

> For an unknown id with a valid token, PUT, PATCH and DELETE return `405` rather than `404`.

## Security

Run each scenario against an existing `{bookingId}`. "Without token" means no `Cookie` header; "invalid token" means `Cookie: token=invalid-token`.

| ID | Scenario | Steps | Expected result | Automated in |
|---|---|---|---|---|
| TC-SEC-01 | PUT without token | `PUT /booking/{bookingId}` | `403`, body `Forbidden` | `BookingSecurityTest.shouldForbidUpdateWithoutValidToken` |
| TC-SEC-02 | PUT with invalid token | same, with invalid token | `403`, body `Forbidden` | `BookingSecurityTest.shouldForbidUpdateWithoutValidToken` |
| TC-SEC-03 | PATCH without token | `PATCH /booking/{bookingId}` | `403`, body `Forbidden` | `BookingSecurityTest.shouldForbidPartialUpdateWithoutValidToken` |
| TC-SEC-04 | PATCH with invalid token | same, with invalid token | `403`, body `Forbidden` | `BookingSecurityTest.shouldForbidPartialUpdateWithoutValidToken` |
| TC-SEC-05 | DELETE without token | `DELETE /booking/{bookingId}`, then `GET /booking/{bookingId}` | Delete: `403`, body `Forbidden`. Get: `200` (booking still exists) | `BookingSecurityTest.shouldForbidDeleteWithoutValidToken` |
| TC-SEC-06 | DELETE with invalid token | same, with invalid token | same as above | `BookingSecurityTest.shouldForbidDeleteWithoutValidToken` |

> In the Postman collection the Security folder runs after Delete Booking. The `403` responses still hold because the API validates the token before looking up the booking. The existence check in TC-SEC-05 and TC-SEC-06 is covered only by the automated suite.

## Traceability summary

| Area | Manual scenarios | Automated executions |
|---|---|---|
| Health Check | 1 | 1 |
| Authentication | 1 | 1 |
| Booking CRUD | 6 | 6 |
| Negative: Authentication | 5 | 5 |
| Negative: Booking | 8 | 8 |
| Security | 6 | 6 |
| **Total** | **27** | **27** |
