package com.example.regression.support;

import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;

/** Shared per-scenario state, injected into hooks and step classes by PicoContainer. */
public class TestContext {
    private WebDriver driver;
    private Scenario scenario;

    public WebDriver getDriver() { return driver; }
    public void setDriver(WebDriver driver) { this.driver = driver; }

    public Scenario getScenario() { return scenario; }
    public void setScenario(Scenario scenario) { this.scenario = scenario; }
}
