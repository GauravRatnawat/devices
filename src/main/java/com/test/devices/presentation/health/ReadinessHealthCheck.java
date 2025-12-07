package com.test.devices.presentation.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;
import org.jboss.logging.Logger;

/**
 * Readiness probe to indicate if the application is ready to serve requests.
 * Checks if the database connection is healthy.
 */
@Readiness
@ApplicationScoped
public class ReadinessHealthCheck implements HealthCheck {

    private static final Logger logger = Logger.getLogger(ReadinessHealthCheck.class);

    @Inject
    EntityManager entityManager;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Database connection");

        try {
            // Simple query to check database connectivity
            entityManager.createNativeQuery("SELECT 1").getSingleResult();
            return responseBuilder.up().build();
        } catch (Exception e) {
            logger.error("Database health check failed", e);
            return responseBuilder.down()
                    .withData("error", e.getMessage())
                    .build();
        }
    }
}
