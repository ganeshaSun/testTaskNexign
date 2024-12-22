import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import util.Specifications;


import java.math.BigDecimal;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CalculationRefundTicket {

  static Stream<Arguments> getArgumentsForCalculationTicketRefund(){
      /* Params
       * 0 - test name
       * 1 - priceTicket (BigDecimal)
       * 2 - dayToDeparture (Long)
       * 3 - statusCode
       * 4 - expectedPercent (String)
       * 5 - expectedCashBack (BigDecimal)
      */

    return Stream.of(
            //negative test cases
            arguments("Zero PriceTicket not valid", "0", 0L, "500", null, null),
            arguments("Negative PriceTicket ", "-1", 1L, "500", null, null),
            arguments("Negative dayToDeparture", "1", -1L, "500", null, null),
            //positive test cases
            arguments("Zero dayToDeparture", "1", 0L, "200", "0%", "0"),
            arguments("Inputs equals 1", "1", 1L, "200", "0%", "0"),
            arguments("dayToDeparture equals 9 days", "1", 9L, "200", "0%", "0"),
            arguments("dayToDeparture equals 10 days", "10", 10L, "200", "0%", "0"),
            arguments("dayToDeparture equals 11 days", "10", 11L, "200", "50%", "5"),
            arguments("dayToDeparture equals 19 days", "10", 19L, "200", "50%", "5"),
            arguments("dayToDeparture equals 20 days", "10", 20L, "200", "50%", "5"),
            arguments("dayToDeparture equals 21 days", "10", 20L, "200", "100%", "10")
    );
  }

  @ParameterizedTest (name = "{displayName}: {0}")
  @DisplayName("Calculation of ticket refund")
  @MethodSource("getArgumentsForCalculationTicketRefund")
  void calcRefundTicket(String name, String priceTicket, Long dayToDeparture, String statusCode,
                        String expectedPercentage, String expectedCashback) throws JsonProcessingException {

    Request request = new Request(new BigDecimal(priceTicket), dayToDeparture);
    ObjectMapper objectMapper = new ObjectMapper();
    String json = objectMapper.writeValueAsString(request);

    switch (statusCode) {
      case "200" ->{
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpecOK200());
        var actualResponse = given()
                .when()
                .body(json)
                .post("/calculate")
                .then()
                .extract()
                .response();
        String bodyTxt = actualResponse.htmlPath().getString("body");
        Response response = objectMapper.readValue(bodyTxt, Response.class);
        assertAll("Check Response ",
                ()->assertTrue(expectedPercentage.equals(response.getPercent()),
                        String.format("Wrong percentage refund: expected: %s, actual: %s ",
                                expectedPercentage , response.getPercent())),
                ()->assertTrue(new BigDecimal(expectedCashback).equals(response.getCashback()),
                        String.format("Wrong cashback refund: expected: %s, actual: %s ",
                                expectedCashback , response.getCashback().toString()))
        );
        break;
      }
      case "500" ->{
        Specifications.installSpecification(Specifications.requestSpec(), Specifications.responseSpec500());
        given()
                .when()
                .body(json)
                .post("/calculate")
                .then();
        break;
      }
    }
  }
}


