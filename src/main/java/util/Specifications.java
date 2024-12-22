package util;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


public class Specifications {

  public static RequestSpecification requestSpec(){
    return new RequestSpecBuilder()
            .setBaseUri("http://localhost:4567")
            .setContentType(ContentType.JSON)
            .build();
  }

  public static ResponseSpecification responseSpecOK200(){
    return new ResponseSpecBuilder()
            .expectStatusCode(200)
            .build();
  }

  public static ResponseSpecification responseSpec500(){
    return new ResponseSpecBuilder()
            .expectStatusCode(500)
            .build();
  }

  public static void installSpecification(RequestSpecification requestSpec, ResponseSpecification response){
    RestAssured.requestSpecification = requestSpec;
    RestAssured.responseSpecification = response;
  }
}