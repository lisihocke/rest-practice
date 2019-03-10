package com.lisihocke.restpractice;

import io.restassured.response.Response;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.DefaultUrl;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.gson.JsonObject;

import static org.hamcrest.Matchers.is;

@RunWith(SerenityRunner.class)
@DefaultUrl("http://localhost:8080/")
public class EmployeeTests {

    private Response response;

    @Test
    public void verifyEmployee1() {
        response = SerenityRest.when().get("employees/1");
        response.then().statusCode(200);
        response.then().body("firstName", is("Bilbo"));
        response.then().body("lastName", is("Baggins"));
        response.then().body("role", is("burglar"));
        response.then().body("name", is("Bilbo Baggins"));
    }

    @Test
    public void createEmployee() {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("firstName", "Anja");
        requestBody.addProperty("lastName", "Müller");
        requestBody.addProperty("role", "baker");

        response = SerenityRest.given()
                .contentType("application/json")
                .body(requestBody.toString())
                .when().post("employees");
        response.then().statusCode(201);
        response.then().body("firstName", is("Anja"));
        response.then().body("lastName", is("Müller"));
        response.then().body("role", is("baker"));
        response.then().body("name", is("Anja Müller"));
    }
}
