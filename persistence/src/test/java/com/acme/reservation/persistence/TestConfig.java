package com.acme.reservation.persistence;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.acme.reservation.persistence.adapters"})
public class TestConfig {}
