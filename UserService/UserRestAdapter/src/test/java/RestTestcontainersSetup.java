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


@Testcontainers
public abstract class RestTestcontainersSetup {

    protected static RequestSpecification clientSpec;
    protected static RequestSpecification employeeSpec;
    protected static RequestSpecification adminSpec;

    private static GenericContainer postgresContainer;
    private static GenericContainer payaraContainer;

    @BeforeAll
    public static void setup() throws IOException {
        Network network = Network.SHARED;
        MountableFile warFile;
        String warFilePath = "../UserRestAdapter/target/UserRestAdapter-1.0.war";

        warFile = MountableFile.forHostPath(
            Paths.get(new File(warFilePath).getCanonicalPath()).toAbsolutePath(), 0777);

        postgresContainer = new PostgreSQLContainer(DockerImageName.parse("postgres:latest"))
            .withDatabaseName("pas")
            .withUsername("pas")
            .withPassword("pas")
            .withNetwork(network)
            .withNetworkAliases("db")
            .withReuse(true);

        payaraContainer = new GenericContainer("payara/server-full:6.2023.2-jdk17")
            .withExposedPorts(8181)
            .dependsOn(postgresContainer)
            .withNetwork(network)
            .withNetworkAliases("payara")
            .withCopyFileToContainer(warFile, "/opt/payara/deployments/RestAdapter-1.0.war")
            .waitingFor(Wait.forHttps("/api/ping").forStatusCode(204).allowInsecure())
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

        String clientJwt = RestAssured.given().body(clientCredentials.toString())
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

        String employeeJwt = RestAssured.given().body(employeeCredentials.toString())
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

        String adminJwt = RestAssured.given().body(adminCredentials.toString())
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
