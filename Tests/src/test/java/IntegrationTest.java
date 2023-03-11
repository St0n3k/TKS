import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static io.restassured.RestAssured.when;


@Testcontainers
public class IntegrationTest {

    @BeforeAll
    public static void setup() {
        Network network = Network.newNetwork();
        MountableFile warFile;

        try {
            warFile = MountableFile.forHostPath(
                    Paths.get(new File("../RestController/target/RestController-1.0.war").getCanonicalPath()).toAbsolutePath(), 0777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        GenericContainer postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("pas")
                .withUsername("pas")
                .withPassword("pas")
                .withExposedPorts(5432)
                .withNetwork(network)
                .withNetworkAliases("db");

        GenericContainer payaraContainer = new GenericContainer("payara/micro:6.2023.2-jdk17")
                .withExposedPorts(8181)
                .dependsOn(postgresContainer)
                .withNetwork(network)
                .withNetworkAliases("payara")
                .withCopyFileToContainer(warFile, "/opt/payara/deployments/RestController-1.0.war")
                .withCommand("--deploy /opt/payara/deployments/RestController-1.0.war --autoBindSsl")
                .waitingFor(Wait.forHttps("/api/rooms").allowInsecure());

        postgresContainer.start();
        payaraContainer.start();

        RestAssured.baseURI = "https://localhost/";
        RestAssured.port = payaraContainer.getMappedPort(8181);
        RestAssured.useRelaxedHTTPSValidation();
    }

    @Test
    void shouldReturnListOfRoomsWithStatusCode200() {
        when().get("/api/rooms")
                .then()
                .assertThat().statusCode(HttpStatus.SC_OK)
                .assertThat().contentType(ContentType.JSON);
    }

}
