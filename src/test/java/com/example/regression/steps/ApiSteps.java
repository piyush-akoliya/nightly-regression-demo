package com.example.regression.steps;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

import com.example.regression.support.TcoeConfig;
import com.example.regression.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/** Generic, reusable REST steps. */
public class ApiSteps {
    private final TestContext ctx;
    private RequestSpecification spec;
    private Response response;

    public ApiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    @Given("the API base URL is {string}")
    public void apiBaseUrl(String url) {
        ctx.getScenario().log("Environment: " + TcoeConfig.environmentName() + " | base.url = " + url);
        spec = given()
                .baseUri(url)
                .accept(ContentType.JSON)
                .header("User-Agent", "nightly-regression-demo");
    }

    @Given("the API base URL is taken from the environment properties")
    public void apiBaseUrlFromConfig() {
        apiBaseUrl(TcoeConfig.baseUrl());
    }

    @When("I send a GET request to {string}")
    public void sendGet(String path) {
        response = spec.when().get(path);
        attachResponse();
    }

    @When("I send a POST request to {string} with body:")
    public void sendPost(String path, String body) {
        response = spec.contentType(ContentType.JSON).body(body).when().post(path);
        attachResponse();
    }

    @Then("the response status code is {int}")
    public void statusCodeIs(int expected) {
        assertThat(response.statusCode()).isEqualTo(expected);
    }

    @Then("the response field {string} equals {string}")
    public void fieldEquals(String jsonPath, String expected) {
        assertThat(response.jsonPath().getString(jsonPath)).isEqualTo(expected);
    }

    @Then("the response field {string} is not empty")
    public void fieldNotEmpty(String jsonPath) {
        assertThat(response.jsonPath().getString(jsonPath)).isNotBlank();
    }

    @Then("the response field {string} is greater than {int}")
    public void fieldGreaterThan(String jsonPath, int min) {
        assertThat(response.jsonPath().getInt(jsonPath)).isGreaterThan(min);
    }

    @Then("the response list {string} has more than {int} items")
    public void listSize(String jsonPath, int min) {
        assertThat(response.jsonPath().getList(jsonPath)).hasSizeGreaterThan(min);
    }

    /** Puts the raw response into the Cucumber report so failures are easy to diagnose. */
    private void attachResponse() {
        ctx.getScenario().attach(response.asPrettyString(), "text/plain",
                "Response (HTTP " + response.statusCode() + ")");
    }
}
