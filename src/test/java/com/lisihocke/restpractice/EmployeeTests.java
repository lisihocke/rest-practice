package com.lisihocke.restpractice;

import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import com.google.gson.JsonObject;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

@RunWith(SerenityRunner.class)
public class EmployeeTests extends TestSetup {

    private static final String EXISTING_EMPLOYEE_ID = "1";
    private static final String NON_EXISTING_EMPLOYEE_ID = "999";
    private String NON_EXISTING_EMPLOYEE_MESSAGE = "Could not find employee ";

    @BeforeClass
    public static void applicationShouldRun() {
        SerenityRest.
                when().get("employees").
                then().statusCode(200);
    }

    @Test
    public void shouldRetrieveDetailsFromExistingEmployee() {
        SerenityRest.
                given().pathParam("id", EXISTING_EMPLOYEE_ID).
                when().get("employees/{id}").
                then().statusCode(200).
                    contentType(ContentType.JSON).
                    body("firstName", is("Bilbo")).
                    body("lastName", is("Baggins")).
                    body("role", is("Burglar")).
                    body("name", is("Bilbo Baggins"));
    }

    @Test
    public void shouldShowErrorMessageForNonExistingEmployee() {
        SerenityRest.
                given().pathParam("id", NON_EXISTING_EMPLOYEE_ID).
                when().get("employees/{id}").
                then().statusCode(404).contentType(ContentType.TEXT).body(containsString(NON_EXISTING_EMPLOYEE_MESSAGE + NON_EXISTING_EMPLOYEE_ID));
    }

    @Test
    public void shouldCreateNewEmployee() {
        JsonObject employeePayload = buildNewEmployeeJsonObject("Cindy", "Carter", "Chef");

        int employeeId = SerenityRest.
                given().contentType("application/json").body(employeePayload.toString()).
                when().post("employees").
                then().statusCode(201).
                    contentType(ContentType.JSON).
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
    public void shouldUpdateEmployee() {
        int employeeId = createNewEmployeeAndGetId();
        JsonObject employeePayload = buildNewEmployeeJsonObject("Dora", "Devonport", "Designer");

        SerenityRest.
                given().pathParam("id", employeeId).contentType("application/json").body(employeePayload.toString()).
                when().put("employees/{id}").
                then().statusCode(201).
                    contentType(ContentType.JSON).
                    body("firstName", is("Dora")).
                    body("lastName", is("Devonport")).
                    body("role", is("Designer")).
                    body("name", is("Dora Devonport"));

        SerenityRest.
                given().pathParam("id", employeeId).
                when().get("employees/{id}").
                then().statusCode(200).
                    contentType(ContentType.JSON).
                    body("firstName", is("Dora")).
                    body("lastName", is("Devonport")).
                    body("role", is("Designer")).
                    body("name", is("Dora Devonport"));
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
                then().statusCode(404).contentType(ContentType.TEXT).body(containsString(NON_EXISTING_EMPLOYEE_MESSAGE + employeeId));
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
