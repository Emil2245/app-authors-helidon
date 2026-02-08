package com.programacion.distribuida.authors.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Readiness;

import javax.sql.DataSource;

@Readiness
@ApplicationScoped
public class ReadinessCheck implements HealthCheck {

    @Inject
    DataSource dataSource;

    @Override
    public HealthCheckResponse call() {
        try (var c = dataSource.getConnection()) {
            boolean valid = c.isValid(2);
            return valid
                    ? HealthCheckResponse.up("app-authors-db")
                    : HealthCheckResponse.down("app-authors-db");
        } catch (Exception e) {
            return HealthCheckResponse.down("app-authors-db");
        }
    }
}


