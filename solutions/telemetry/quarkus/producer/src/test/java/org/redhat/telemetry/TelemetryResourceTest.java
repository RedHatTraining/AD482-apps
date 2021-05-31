package org.redhat.telemetry;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class TelemetryResourceTest {

    @Test
    public void testHelloEndpoint() {
        given()
          .when().get("/telemetry")
          .then()
             .statusCode(200)
             .body(is("Hello RESTEasy"));
    }

}