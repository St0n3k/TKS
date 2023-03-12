import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.tks.dto.CreateRentDTO;
import pl.lodz.p.it.tks.dto.UpdateRoomDTO;
import pl.lodz.p.it.tks.model.Room;

import java.time.LocalDateTime;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;


@Testcontainers
public class IntegrationTests extends TestcontainersSetup {

    @Test
    void shouldReturnRoomWithStatusCode200() {
        given().spec(clientSpec)
                .when().get("/api/rooms/9acac245-25b3-492d-a742-4c69bfcb90cf")
                .then()
                .assertThat().statusCode(Status.OK.getStatusCode())
                .assertThat().contentType(ContentType.JSON)
                .assertThat().body("roomNumber", equalTo(643))
                .assertThat().body("price", equalTo(250.0F))
                .assertThat().body("size", equalTo(6));
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

        when().get("api/rooms/" + id)
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("id", equalTo(id.toString()),
                        "price", equalTo(600.0f),
                        "size", equalTo(1));
    }

    @Test
    void shouldFailCreatingRoomWithExistingNumberWithStatusCode409() {
        Room room = new Room(643, 200.0, 10);

        JSONObject req = new JSONObject(room);
        given().spec(employeeSpec).contentType(ContentType.JSON)
                .body(req.toString())
                .when()
                .post("/api/rooms")
                .then()
                .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldGetRoomByIdWithStatusCode200() {
        given().when()
                .get("/api/rooms/dba673f8-0526-4cea-941e-3c8ddd5e4f92")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("id", equalTo("dba673f8-0526-4cea-941e-3c8ddd5e4f92"),
                        "price", equalTo(707.19F),
                        "roomNumber", equalTo(836),
                        "size", equalTo(1));
    }

    @Test
    void shouldGetRoomByIdFailWithStatusCode404() {
        given().when()
                .get("/api/rooms/dba537f8-0526-4cea-941e-3c8ddd5e4f92")
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldFindPastRentsForRoomWithStatusCode200() {
        given().spec(employeeSpec).param("past", true)
                .when().get("/api/rooms/66208864-7b61-4e6e-8573-53863bd93b35/rents")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("size()", equalTo(1));
    }

    @Test
    void shouldFindActiveRentsForRoomWithStatusCode200() {
        given().spec(employeeSpec).param("past", false)
                .when().get("/api/rooms/66208864-7b61-4e6e-8573-53863bd93b35/rents")
                .then()
                .statusCode(Status.OK.getStatusCode())
                .contentType(ContentType.JSON)
                .body("size()", equalTo(2));
    }

    @Test
    void shouldUpdateRoomWithStatusCode200() {
        UpdateRoomDTO dto = new UpdateRoomDTO(1934, null, 199.99);
        JSONObject req = new JSONObject(dto);

        given().spec(employeeSpec).when().get("/api/rooms/b9573aa2-42fa-43cb-baa1-42d06e1bdc8d")
                .then()
                .assertThat().statusCode(Status.OK.getStatusCode())
                .assertThat().body("price", equalTo(1396.79F))
                .assertThat().body("size", equalTo(9))
                .assertThat().body("roomNumber", equalTo(392));


        given().spec(employeeSpec).contentType(ContentType.JSON)
                .body(req.toString())
                .when().put("/api/rooms/b9573aa2-42fa-43cb-baa1-42d06e1bdc8d")
                .then().assertThat().statusCode(Status.OK.getStatusCode())
                .assertThat().body("price", equalTo(199.99F))
                .assertThat().body("size", equalTo(9))
                .assertThat().body("roomNumber", equalTo(1934));
    }

    @Test
    void shouldFailUpdatingRoomNumberDueToExistingRoomNumberWithStatusCode409() {
        UpdateRoomDTO dto = new UpdateRoomDTO(836, null, null);
        JSONObject req = new JSONObject(dto);

        when().get("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
                .then()
                .assertThat().statusCode(Status.OK.getStatusCode())
                .assertThat().body("roomNumber", equalTo(244));


        given().spec(employeeSpec).contentType(ContentType.JSON)
                .body(req.toString())
                .when().put("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
                .then().assertThat().statusCode(Status.CONFLICT.getStatusCode());
    }


    @Test
    void shouldRemoveRoomWithStatusCode204() {
        Room room = new Room(1234, 200.0, 4);
        JSONObject json = new JSONObject(room);
        Room addedRoom = given().spec(adminSpec).body(json.toString())
                .contentType(ContentType.JSON)
                .when().post("/api/rooms")
                .getBody().as(Room.class);

        given().spec(adminSpec).contentType(ContentType.JSON)
                .when().delete("/api/rooms/" + addedRoom.getId())
                .then()
                .statusCode(Status.NO_CONTENT.getStatusCode());

        when().get("/api/rooms/" + addedRoom.getId())
                .then()
                .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldFailRemoveRoomWhenThereAreActiveRentsForItWithStatusCode409() {
        Room room = new Room(4321, 200.0, 4);
        JSONObject json = new JSONObject(room);

        Room addedRoom = given().spec(employeeSpec).body(json.toString())
                .contentType(ContentType.JSON)
                .when().post("/api/rooms")
                .getBody().as(Room.class);

        LocalDateTime beginDate = LocalDateTime.of(2025, 11, 22, 11, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 11, 25, 10, 0, 0);

        CreateRentDTO dto = new CreateRentDTO(beginDate, endDate, true, UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"), addedRoom.getId());
        JSONObject body = new JSONObject(dto);

        given().spec(employeeSpec).contentType(ContentType.JSON)
                .body(body.toString())
                .when()
                .post("/api/rents")
                .then()
                .statusCode(Status.CREATED.getStatusCode());


        given().spec(employeeSpec).contentType(ContentType.JSON)
                .when().delete("/api/rooms/" + addedRoom.getId())
                .then()
                .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldFailWhileUpdatingRoomWithInvalidAttributes() {
        UpdateRoomDTO roomDTO = new UpdateRoomDTO(75, 0, -1.);
        JSONObject json = new JSONObject(roomDTO);
        given().spec(employeeSpec).body(json.toString())
                .contentType(ContentType.JSON)
                .when()
                .put("/api/rooms/8378b753-6d05-454b-8447-efb125846fc7")
                .then()
                .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());
    }
}
