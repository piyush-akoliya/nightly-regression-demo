package com.example.regression.hooks;

import com.example.regression.support.TestContext;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Hooks {
    private final TestContext ctx;

    public Hooks(TestContext ctx) {
        this.ctx = ctx;
    }

    @Before(order = 0)
    public void captureScenario(Scenario scenario) {
        ctx.setScenario(scenario);
    }

    @Before(value = "@ui", order = 1)
    public void startBrowser() {
        ChromeOptions options = new ChromeOptions();
        if (Boolean.parseBoolean(System.getProperty("headless", "true"))) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox", "--disable-dev-shm-usage", "--window-size=1920,1080");
        ctx.setDriver(new ChromeDriver(options)); // Selenium Manager downloads the matching driver
    }

    @After(value = "@ui", order = 1)
    public void stopBrowser(Scenario scenario) {
        WebDriver driver = ctx.getDriver();
        if (driver == null) {
            return;
        }
        try {
            if (scenario.isFailed()) {
                byte[] png = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(png, "image/png", "Failure screenshot");
            }
        } finally {
            driver.quit();
        }
    }
}
