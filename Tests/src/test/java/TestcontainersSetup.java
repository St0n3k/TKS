import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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

import static io.restassured.RestAssured.given;


@Testcontainers
public abstract class TestcontainersSetup {

    protected static RequestSpecification clientSpec;
    protected static RequestSpecification employeeSpec;
    protected static RequestSpecification adminSpec;

    private static GenericContainer postgresContainer;
    private static GenericContainer payaraContainer;

    @BeforeAll
    public static void setup() {
        Network network = Network.SHARED;
        MountableFile warFile;
        String warFilePath = "../RestAdapter/target/RestAdapter-1.0.war";

        try {
            warFile = MountableFile.forHostPath(
                    Paths.get(new File(warFilePath).getCanonicalPath()).toAbsolutePath(), 0777);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
                .withDatabaseName("pas")
                .withUsername("pas")
                .withPassword("pas")
                .withExposedPorts(5432)
                .withNetwork(network)
                .withNetworkAliases("db")
                .withReuse(true);

        payaraContainer = new GenericContainer("payara/server-full:6.2023.2-jdk17")
                .withExposedPorts(8181)
                .dependsOn(postgresContainer)
                .withNetwork(network)
                .withNetworkAliases("payara")
                .withCopyFileToContainer(warFile, "/opt/payara/deployments/RestController-1.0.war")
                .waitingFor(Wait.forHttps("/api/rooms").allowInsecure())
                .withReuse(true);

        postgresContainer.start();
        payaraContainer.start();

        RestAssured.baseURI = "https://localhost/";
        RestAssured.port = payaraContainer.getMappedPort(8181);
        RestAssured.useRelaxedHTTPSValidation();

        generateClientSpec();
        generateEmployeeSpec();
        generateAdminSpec();
    }

    @AfterAll
    static void clearContainers() {
        postgresContainer.stop();
        payaraContainer.stop();
    }

    static void generateClientSpec() {
        JSONObject clientCredentials = new JSONObject();
        clientCredentials.put("username", "client");
        clientCredentials.put("password", "password");

        String clientJwt = given().body(clientCredentials.toString())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .jsonPath()
                .get("jwt");

        RequestSpecBuilder builder = new RequestSpecBuilder();

        builder.addHeader("Authorization", "Bearer " + clientJwt);
        clientSpec = builder.build();
    }

    static void generateEmployeeSpec() {
        JSONObject employeeCredentials = new JSONObject();
        employeeCredentials.put("username", "employee");
        employeeCredentials.put("password", "password");

        String employeeJwt = given().body(employeeCredentials.toString())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .jsonPath()
                .get("jwt");

        RequestSpecBuilder builder = new RequestSpecBuilder();

        builder.addHeader("Authorization", "Bearer " + employeeJwt);
        employeeSpec = builder.build();
    }

    static void generateAdminSpec() {
        JSONObject adminCredentials = new JSONObject();
        adminCredentials.put("username", "admin");
        adminCredentials.put("password", "password");

        String adminJwt = given().body(adminCredentials.toString())
                .contentType(ContentType.JSON)
                .when()
                .post("/api/login")
                .jsonPath()
                .get("jwt");

        RequestSpecBuilder builder = new RequestSpecBuilder();

        builder.addHeader("Authorization", "Bearer " + adminJwt);
        adminSpec = builder.build();
    }
}
