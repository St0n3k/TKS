package pl.lodz.p.it.tks.messaging;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;
import pl.lodz.p.it.tks.user.dto.user.RegisterClientDTO;
import pl.lodz.p.it.tks.user.dto.user.UpdateClientDTO;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.UUID;

@Testcontainers
class CreateClientConsumerIT {
    private static final Network network = Network.SHARED; //Network.newNetwork();

    private static GenericContainer<?> db;
    private static GenericContainer<?> payaraServer;
    private static GenericContainer<?> rabbitMq;

    private static RequestSpecification adminSpec;

    @BeforeAll
    static void init() {
        Path initScriptPath = Path.of("..", "..", "createdb.sh").toAbsolutePath().normalize();

        Path userServiceWar = Path.of(
            "..", "..", "UserService", "UserRestAdapter", "target", "UserRestAdapter-1.0.war");

        Path rentServiceWar = Path.of(
            "..", "..", "RentService", "RestAdapter", "target", "RestAdapter-1.0.war");

        assert Files.exists(userServiceWar);
        assert Files.exists(rentServiceWar);

        db = new PostgreSQLContainer(DockerImageName.parse("postgres:15.2"))
            .withDatabaseName("rentdb")
            .withUsername("rent")
            .withPassword("rent")
            .withCopyFileToContainer(
                MountableFile.forHostPath(initScriptPath),
                "/docker-entrypoint-initdb.d/10-createdb.sh")
            .withNetworkAliases("db")
            .withNetwork(network)
            .withReuse(true);

        rabbitMq = new GenericContainer<>(DockerImageName.parse("bitnami/rabbitmq"))
            .withEnv(Map.of(
                "RABBITMQ_DEFAULT_USER", "user",
                "RABBITMQ_DEFAULT_PASS", "password"))
            // .withExposedPorts(5672)
            .waitingFor(Wait.forLogMessage(".*Starting broker.*", 1))
            .withNetwork(network)
            .withNetworkAliases("rabbitMQ")
            .withReuse(true);


        db.start();
        rabbitMq.start();

        payaraServer = new GenericContainer<>("payara/server-full:6.2023.2-jdk17")
            .withExposedPorts(8181)
            .withEnv(Map.of(
                "mq_host", "rabbitMQ",
                // "mq_port", rabbitMq.getMappedPort(5672).toString(),
                "mq_port", "5672",
                "mq_username", "user",
                "mq_password", "password"))
            .dependsOn(db, rabbitMq)
            .withNetwork(network)
            .withNetworkAliases("payara")
            .withCopyFileToContainer(
                MountableFile.forHostPath(userServiceWar),
                "/opt/payara/deployments/UserRestAdapter-1.0.war")
            .withCopyFileToContainer(
                MountableFile.forHostPath(rentServiceWar),
                "/opt/payara/deployments/RestAdapter-1.0.war")
            .waitingFor(Wait.forHttps("/user/ping").allowInsecure())
            .waitingFor(Wait.forHttps("/rent/rooms").allowInsecure())
            .withReuse(true);

        payaraServer.start();

        RestAssured.baseURI = "https://localhost/";
        RestAssured.port = payaraServer.getMappedPort(8181);
        RestAssured.useRelaxedHTTPSValidation();

        JSONObject adminCredentials = new JSONObject().put("username", "admin").put("password", "password");

        String adminToken = given()
            .contentType(ContentType.JSON)
            .body(adminCredentials.toString())
            .when()
            .post("/user/login")
            .then()
            .extract()
            .body()
            .jsonPath()
            .get("jwt");

        adminSpec = new RequestSpecBuilder()
            .addHeader("Authorization", "Bearer " + adminToken)
            .build();
    }

    @AfterAll
    static void cleanUp() {
        payaraServer.stop();
        rabbitMq.stop();
        db.stop();

        network.close();
    }

    @Nested
    class CreateUserTest {
        @Test
        void shouldCreateClientInRentServiceTest() throws InterruptedException {
            var personalId = "9871235612";
            JSONObject clientData = new JSONObject(
                new RegisterClientDTO(
                    "test-user",
                    "Adam",
                    "Smith",
                    personalId,
                    "Lodz",
                    "Dluga",
                    1234,
                    "pwd"
                ));

            UUID id = given()
                .spec(adminSpec)
                .contentType(ContentType.JSON)
                .body(clientData.toString())
                .when()
                .post("/user/users/clients")
                .then()
                // .log().all()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .extract()
                .jsonPath()
                .getUUID("id");

            Thread.sleep(1000);

            given()
                .spec(adminSpec)
                .when()
                .get("rent/users/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .body(
                    "username", equalTo("test-user"),
                    "firstName", equalTo("Adam"),
                    "personalId", equalTo(personalId)
                );
        }

        @Test
        void shouldNotPropagateMessageIfErrorOccurredTest() throws InterruptedException {
            var personalId = "3584173"; // conflicting personalId
            JSONObject clientData = new JSONObject(
                new RegisterClientDTO(
                    "conflict",
                    "Adam",
                    "Smith",
                    personalId,
                    "Lodz",
                    "Krotka",
                    321,
                    "pwd"
                ));

            given()
                .spec(adminSpec)
                .contentType(ContentType.JSON)
                .body(clientData.toString())
                .when()
                .post("user/users/clients")
                .then()
                .statusCode(409);

            Thread.sleep(1000);

            given()
                .spec(adminSpec)
                .when()
                .get("rent/users/search/conflict")
                .then()
                .log().all()
                .statusCode(404);
        }
    }

    @Nested
    class UpdateUserTest {
        @Test
        void shouldUpdateClientInRentServiceTest() throws InterruptedException {
            String personalId = "54352353";
            String clientId = "bdbe2fcf-6203-47d6-8908-ca65b9689396";

            JSONObject dto = new JSONObject(
                new UpdateClientDTO(
                    "Adam",
                    "Malysz",
                    personalId,
                    "Warszawa",
                    "Narciarska",
                    98
                ));

            given()
                .spec(adminSpec)
                .contentType(ContentType.JSON)
                .body(dto.toString())
                .when()
                .put("user/users/clients/" + clientId)
                .then()
                // .log().all()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(
                    "firstName", equalTo("Adam"),
                    "lastName", equalTo("Malysz"),
                    "personalId", equalTo(personalId)
                );

            Thread.sleep(1000);

            given()
                .spec(adminSpec)
                .when()
                .get("rent/users/" + clientId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(
                    "firstName", equalTo("Adam"),
                    "lastName", equalTo("Malysz"),
                    "personalId", equalTo(personalId)
                );
        }

        @Test
        void shouldNotPropagateChangesAfterFailedUpdateTest() throws InterruptedException {
            String clientId = "dbc028ea-a233-4280-b953-564a69da1810";

            JSONObject dto = new JSONObject(
                new UpdateClientDTO(
                    "Adam151",
                    "Ma231lysz",
                    "3584173",
                    "",
                    "",
                    -12
                ));

            given()
                .spec(adminSpec)
                .contentType(ContentType.JSON)
                .body(dto.toString())
                .when()
                .put("user/users/clients/" + clientId)
                .then()
                .statusCode(400);

            Thread.sleep(1000);

            given()
                .spec(adminSpec)
                .when()
                .get("rent/users/" + clientId)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body(
                    "firstName", equalTo("Kuba"),
                    "lastName", equalTo("Bokaj"),
                    "city", equalTo("Krakow")
                );
        }
    }
}
