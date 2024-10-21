package com.ecommerce.trenzio.monitoring;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        boolean customHealthCheck = performCustomCheck(); // Simulate a health check
        if (customHealthCheck) {
            return Health.up().withDetail("Custom Health Check", "Service is healthy").build();
        }
        return Health.down().withDetail("Custom Health Check", "Service is down").build();
    }

    private boolean performCustomCheck() {
        // Custom health check logic
        return true;
    }
}
