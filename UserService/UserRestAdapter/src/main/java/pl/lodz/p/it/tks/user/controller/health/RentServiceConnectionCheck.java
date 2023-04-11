package pl.lodz.p.it.tks.user.controller.health;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.HealthCheckResponseBuilder;
import org.eclipse.microprofile.health.Readiness;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Readiness
@ApplicationScoped
public class RentServiceConnectionCheck implements HealthCheck {

    @Inject
    @ConfigProperty(name = "RENT_SERVICE_HOST", defaultValue = "localhost")
    private String rentServiceHost;

    @Inject
    @ConfigProperty(name = "RENT_SERVICE_PORT", defaultValue = "8181")
    private int rentServicePort;

    @Inject
    @ConfigProperty(name = "RENT_SERVICE_PATH", defaultValue = "/rent/rooms")
    private String rentServicePath;

    @Override
    public HealthCheckResponse call() {
        HealthCheckResponseBuilder responseBuilder = HealthCheckResponse.named("Rent Service connection");
        try {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI("https://%s:%s%s"
                    .formatted(rentServiceHost, rentServicePort, rentServicePath)))
                .GET()
                .build();
            HttpResponse<String> response =
                HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == Response.Status.OK.getStatusCode()) {
                responseBuilder.up();
            } else {
                responseBuilder.down();
            }
        } catch (Exception e) {
            responseBuilder.down()
                .withData("error", e.getMessage());
        }
        return responseBuilder.build();
    }
}
