package pl.lodz.p.it.tks.user.controller.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import java.io.IOException;
import java.net.Socket;

@Readiness
@ApplicationScoped
public class DatabaseConnectionCheck implements HealthCheck {
    @Inject
    @ConfigProperty(name = "POSTGRESQL_SERVICE_HOST", defaultValue = "localhost")
    private String dbHost;
    @Inject
    @ConfigProperty(name = "POSTGRESQL_SERVICE_PORT", defaultValue = "5432")
    private int dbPort;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("User Service database connection");
        try {
            pingServer(dbHost, dbPort);
            responseBuilder.up();
        } catch (Exception e) {
            responseBuilder.down()
                .withData("error", e.getMessage());
        }
        return responseBuilder.build();
    }

    private void pingServer(String dbhost, int port) throws IOException {
        Socket socket = new Socket(dbhost, port);
        socket.close();
    }
}
