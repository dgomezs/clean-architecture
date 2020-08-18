package com.acme.reservation.cancellation;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(Cucumber.class)
@SpringBootTest(classes = ReservationTestConfig.class)
@CucumberContextConfiguration
@CucumberOptions(
    features = "classpath:features/cancellation",
    plugin = {"json", "json:../target/cancellation.json"})
public class CancellationBddTest {}
