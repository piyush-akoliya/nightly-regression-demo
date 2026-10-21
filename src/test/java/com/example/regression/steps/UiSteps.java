package com.example.regression.steps;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.regression.support.TcoeConfig;
import com.example.regression.support.TestContext;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UiSteps {
    private final TestContext ctx;

    public UiSteps(TestContext ctx) {
        this.ctx = ctx;
    }

    private WebDriver driver() {
        return ctx.getDriver();
    }

    private WebDriverWait waitFor() {
        return new WebDriverWait(driver(), Duration.ofSeconds(10));
    }

    @Given("I open the page {string}")
    public void openPage(String url) {
        driver().get(url);
    }

    @Given("I open the application from the environment properties")
    public void openApplication() {
        ctx.getScenario().log("Environment: " + TcoeConfig.environmentName() + " | base.url = " + TcoeConfig.baseUrl());
        driver().get(TcoeConfig.baseUrl());
    }

    @When("I log in with username {string} and password {string}")
    public void login(String user, String pass) {
        driver().findElement(By.id("user-name")).sendKeys(user);
        driver().findElement(By.id("password")).sendKeys(pass);
        driver().findElement(By.id("login-button")).click();
    }

    @Then("I land on the products page")
    public void landOnProducts() {
        waitFor().until(ExpectedConditions.urlContains("inventory.html"));
        assertThat(driver().findElement(By.className("title")).getText()).isEqualTo("Products");
    }

    @Then("I see the login error {string}")
    public void seeError(String expected) {
        String actual = waitFor()
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")))
                .getText();
        assertThat(actual).isEqualTo(expected);
    }
}
