import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import jakarta.ws.rs.core.Response.Status;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import pl.lodz.p.it.tks.model.Room;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;


@Testcontainers
public class IntegrationTest {

    private static RequestSpecification clientSpec;
    private static RequestSpecification employeeSpec;
    private static RequestSpecification adminSpec;

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

        GenericContainer payaraContainer = new GenericContainer("payara/server-full:6.2023.2-jdk17")
                .withExposedPorts(8181)
                .dependsOn(postgresContainer)
                .withNetwork(network)
                .withNetworkAliases("payara")
                .withCopyFileToContainer(warFile, "/opt/payara/deployments/RestController-1.0.war")
                .waitingFor(Wait.forHttps("/api/rooms").allowInsecure());

        postgresContainer.start();
        payaraContainer.start();

        RestAssured.baseURI = "https://localhost/";
        RestAssured.port = payaraContainer.getMappedPort(8181);
        RestAssured.useRelaxedHTTPSValidation();

        generateClientSpec();
        generateEmployeeSpec();
        generateAdminSpec();
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



    @Test
    void shouldReturnListOfRoomsWithStatusCode200() {
        when().get("/api/rooms")
                .then()
                .assertThat().statusCode(Status.OK.getStatusCode())
                .assertThat().contentType(ContentType.JSON);
    }

    @Test
    void shouldCreateRoomWithStatusCode201() {

        Room room = new Room(1, 600.0, 1);

        JSONObject req = new JSONObject(room);
        UUID id = given().spec(employeeSpec).contentType(ContentType.JSON)
                .body(req.toString())
                .when()
                .post("/api/rooms")
                .then()
                .statusCode(Status.CREATED.getStatusCode())
                .extract().jsonPath().getUUID("id");

        given().spec(employeeSpec).when().get("api/rooms/" + id)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("id", equalTo(id.toString()),
                        "price", equalTo(600.0f),
                        "size", equalTo(1));
    }
}
