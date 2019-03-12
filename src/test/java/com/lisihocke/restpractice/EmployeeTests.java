package com.lisihocke.restpractice;

import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.gson.JsonObject;

import static org.hamcrest.Matchers.is;

@RunWith(SerenityRunner.class)
public class EmployeeTests extends TestSetup {

    @BeforeClass
    public static void makeSureThatApplicationIsUp() {
        SerenityRest.
                when().
                    get("employees").
                then().
                    statusCode(200);
    }

    @Test
    public void getFirstEmployee() {
        SerenityRest.
                given().
                    pathParam("id", "1").
                when().
                    get("employees/{id}").
                then().
                    statusCode(200).
                    body("firstName", is("Bilbo")).
                    body("lastName", is("Baggins")).
                    body("role", is("burglar")).
                    body("name", is("Bilbo Baggins"));
    }

    @Test
    public void createEmployee() {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("firstName", "Cindy");
        requestBody.addProperty("lastName", "Chef");
        requestBody.addProperty("role", "chef");

        SerenityRest.
                given().
                    contentType("application/json").
                    body(requestBody.toString()).
                when().
                    post("employees").
                then().
                    statusCode(201).
                    body("firstName", is("Cindy")).
                    body("lastName", is("Chef")).
                    body("role", is("chef")).
                    body("name", is("Cindy Chef"));
    }

    @Test
    public void deleteEmployee() {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("firstName", "Betty");
        requestBody.addProperty("lastName", "Baker");
        requestBody.addProperty("role", "baker");

        int employeeId = SerenityRest.
                given().
                    contentType("application/json").
                    body(requestBody.toString()).
                when().
                    post("employees").
                then().
                    extract().path("id");

        SerenityRest.
                given().
                    pathParam("id", employeeId).
                when().
                    delete("employees/{id}").
                then().
                    statusCode(204);
    }
}
