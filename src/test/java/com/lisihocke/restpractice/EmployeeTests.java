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

    private static final String EXISTING_EMPLOYEE = "1";
    private static final String NON_EXISTING_EMPLOYEE = "999";

    @BeforeClass
    public static void applicationShouldRun() {
        SerenityRest.
                when().get("employees").
                then().statusCode(200);
    }

    @Test
    public void shouldRetrieveDetailsFromExistingEmployee() {
        SerenityRest.
                given().pathParam("id", EXISTING_EMPLOYEE).
                when().get("employees/{id}").
                then().statusCode(200).
                    body("firstName", is("Bilbo")).
                    body("lastName", is("Baggins")).
                    body("role", is("burglar")).
                    body("name", is("Bilbo Baggins"));
    }

    @Test
    public void shouldShowErrorMessageForNonExistingEmployee() {
        SerenityRest.
                given().pathParam("id", NON_EXISTING_EMPLOYEE).
                when().get("employees/{id}").
                then().statusCode(404).body("message", is("Could not find employee " + NON_EXISTING_EMPLOYEE));
    }

    @Test
    public void shouldCreateNewEmployee() {
        JsonObject employeePayload = buildNewEmployeeJsonObject("Cindy", "Carter", "Chef");

        int employeeId = SerenityRest.
                given().contentType("application/json").body(employeePayload.toString()).
                when().post("employees").
                then().statusCode(201).
                    body("firstName", is("Cindy")).
                    body("lastName", is("Carter")).
                    body("role", is("Chef")).
                    body("name", is("Cindy Carter")).
                    extract().path("id");

        SerenityRest.
                given().pathParam("id", employeeId).
                when().get("employees/{id}").
                then().statusCode(200);
    }

    @Test
    public void shouldDeleteEmployee() {
        int employeeId = createNewEmployeeAndGetId();

        SerenityRest.
                given().pathParam("id", employeeId).
                when().delete("employees/{id}").
                then().statusCode(204);

        SerenityRest.
                given().pathParam("id", employeeId).
                when().get("employees/{id}").
                then().statusCode(404);
        // TODO: assert for the content of the response
        // TODO: service responds plain text, but we want it to be JSON!
    }

    private int createNewEmployeeAndGetId() {
        JsonObject employeePayload = buildNewEmployeeJsonObject("Betty", "Barclay", "Bartender");

        // TODO: can we get a response without given/when/then? this is just the setup
        return SerenityRest.
                given().contentType("application/json").body(employeePayload.toString()).
                when().post("employees").
                then().extract().path("id");
    }

    private JsonObject buildNewEmployeeJsonObject(String firstName, String lastName, String role) {
        JsonObject payload = new JsonObject();
        payload.addProperty("firstName", firstName);
        payload.addProperty("lastName", lastName);
        payload.addProperty("role", role);
        return payload;
    }
}
