import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import jakarta.ws.rs.core.Response.Status;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.tks.dto.rent.CreateRentDTO;
import pl.lodz.p.it.tks.dto.rent.RentRoomForSelfDTO;
import pl.lodz.p.it.tks.dto.room.UpdateRoomDTO;
import pl.lodz.p.it.tks.dto.user.RegisterClientDTO;
import pl.lodz.p.it.tks.dto.user.RegisterEmployeeDTO;
import pl.lodz.p.it.tks.dto.user.UpdateClientDTO;
import pl.lodz.p.it.tks.model.Room;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicInteger;


@Testcontainers
public class RestIT extends RestTestcontainersSetup {

    //region RoomTests
    @Test
    void shouldReturnRoomWithStatusCode200() {
        RestAssured.given().spec(clientSpec)
            .when().get("/api/rooms/9acac245-25b3-492d-a742-4c69bfcb90cf")
            .then()
            .assertThat().statusCode(Status.OK.getStatusCode())
            .assertThat().contentType(ContentType.JSON)
            .assertThat().body("roomNumber", Matchers.equalTo(643))
            .assertThat().body("price", Matchers.equalTo(250.0F))
            .assertThat().body("size", Matchers.equalTo(6));
    }

    @Test
    void shouldReturnListOfRoomsWithStatusCode200() {
        RestAssured.when().get("/api/rooms")
            .then()
            .assertThat().statusCode(Status.OK.getStatusCode())
            .assertThat().contentType(ContentType.JSON);
    }

    @Test
    void shouldCreateRoomWithStatusCode201() {

        Room room = new Room(1, 600.0, 1);

        JSONObject req = new JSONObject(room);
        UUID id = RestAssured.given().spec(employeeSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/rooms")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract().jsonPath().getUUID("id");

        RestAssured.when().get("api/rooms/" + id)
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo(id.toString()),
                "price", Matchers.equalTo(600.0f),
                "size", Matchers.equalTo(1));
    }

    @Test
    void shouldFailCreatingRoomWithExistingNumberWithStatusCode409() {
        Room room = new Room(643, 200.0, 10);

        JSONObject req = new JSONObject(room);
        RestAssured.given().spec(employeeSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/rooms")
            .then()
            .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldFailCreatingRoomWithStatusCode403() {
        Room room = new Room(643, 200.0, 10);

        JSONObject req = new JSONObject(room);
        RestAssured.given().contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/rooms")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        RestAssured.given().spec(clientSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/rooms")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void shouldGetRoomByIdWithStatusCode200() {
        RestAssured.given().when()
            .get("/api/rooms/dba673f8-0526-4cea-941e-3c8ddd5e4f92")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo("dba673f8-0526-4cea-941e-3c8ddd5e4f92"),
                "price", Matchers.equalTo(707.19F),
                "roomNumber", Matchers.equalTo(836),
                "size", Matchers.equalTo(1));
    }

    @Test
    void shouldGetRoomByIdFailWithStatusCode404() {
        RestAssured.given().when()
            .get("/api/rooms/dba537f8-0526-4cea-941e-3c8ddd5e4f92")
            .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldFindPastRentsForRoomWithStatusCode200() {
        RestAssured.given().spec(employeeSpec).param("past", true)
            .when().get("/api/rooms/66208864-7b61-4e6e-8573-53863bd93b35/rents")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("size()", Matchers.equalTo(1));
    }

    @Test
    void shouldFindActiveRentsForRoomWithStatusCode200() {
        RestAssured.given().spec(employeeSpec).param("past", false)
            .when().get("/api/rooms/66208864-7b61-4e6e-8573-53863bd93b35/rents")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("size()", Matchers.equalTo(2));
    }

    @Test
    void shouldUpdateRoomWithStatusCode200() {
        UpdateRoomDTO dto = new UpdateRoomDTO(1934, null, 199.99);
        JSONObject req = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec).when().get("/api/rooms/b9573aa2-42fa-43cb-baa1-42d06e1bdc8d")
            .then()
            .assertThat().statusCode(Status.OK.getStatusCode())
            .assertThat().body("price", Matchers.equalTo(1396.79F))
            .assertThat().body("size", Matchers.equalTo(9))
            .assertThat().body("roomNumber", Matchers.equalTo(392));


        RestAssured.given().spec(employeeSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when().put("/api/rooms/b9573aa2-42fa-43cb-baa1-42d06e1bdc8d")
            .then().assertThat().statusCode(Status.OK.getStatusCode())
            .assertThat().body("price", Matchers.equalTo(199.99F))
            .assertThat().body("size", Matchers.equalTo(9))
            .assertThat().body("roomNumber", Matchers.equalTo(1934));
    }

    @Test
    void shouldFailUpdatingRoomNumberDueToExistingRoomNumberWithStatusCode409() {
        UpdateRoomDTO dto = new UpdateRoomDTO(836, null, null);
        JSONObject req = new JSONObject(dto);

        RestAssured.when().get("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then()
            .assertThat().statusCode(Status.OK.getStatusCode())
            .assertThat().body("roomNumber", Matchers.equalTo(244));


        RestAssured.given().spec(employeeSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when().put("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then().assertThat().statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldFailUpdatingRoomWithStatusCode403() {
        UpdateRoomDTO dto = new UpdateRoomDTO(836, null, null);
        JSONObject req = new JSONObject(dto);

        RestAssured.when().get("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then()
            .assertThat().statusCode(Status.OK.getStatusCode())
            .assertThat().body("roomNumber", Matchers.equalTo(244));

        RestAssured.given().contentType(ContentType.JSON)
            .body(req.toString())
            .when().put("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then().assertThat().statusCode(Status.FORBIDDEN.getStatusCode());

        RestAssured.given().spec(clientSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when().put("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then().assertThat().statusCode(Status.FORBIDDEN.getStatusCode());
    }


    @Test
    void shouldRemoveRoomWithStatusCode204() {
        Room room = new Room(1234321, 200.0, 4);
        JSONObject json = new JSONObject(room);
        Room addedRoom = RestAssured.given().spec(adminSpec).body(json.toString())
            .contentType(ContentType.JSON)
            .when().post("/api/rooms")
            .getBody().as(Room.class);

        RestAssured.given().spec(adminSpec).contentType(ContentType.JSON)
            .when().delete("/api/rooms/" + addedRoom.getId())
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        RestAssured.when().get("/api/rooms/" + addedRoom.getId())
            .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldFailRemovingRoomWithStatusCode403() {
        Room room = new Room(1234, 200.0, 4);
        JSONObject json = new JSONObject(room);
        Room addedRoom = RestAssured.given().spec(adminSpec).body(json.toString())
            .contentType(ContentType.JSON)
            .when().post("/api/rooms")
            .getBody().as(Room.class);

        RestAssured.given().contentType(ContentType.JSON)
            .when().delete("/api/rooms/" + addedRoom.getId())
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        RestAssured.given().spec(clientSpec).contentType(ContentType.JSON)
            .when().delete("/api/rooms/" + addedRoom.getId())
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void shouldFailRemoveRoomWhenThereAreActiveRentsForItWithStatusCode409() {
        Room room = new Room(4321, 200.0, 4);
        JSONObject json = new JSONObject(room);

        Room addedRoom = RestAssured.given().spec(employeeSpec).body(json.toString())
            .contentType(ContentType.JSON)
            .when().post("/api/rooms")
            .getBody().as(Room.class);

        LocalDateTime beginDate = LocalDateTime.of(2025, 11, 22, 11, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2025, 11, 25, 10, 0, 0);

        CreateRentDTO dto = new CreateRentDTO(beginDate, endDate, true,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            addedRoom.getId());
        JSONObject body = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec).contentType(ContentType.JSON)
            .body(body.toString())
            .when()
            .post("/api/rents")
            .then()
            .statusCode(Status.CREATED.getStatusCode());


        RestAssured.given().spec(employeeSpec).contentType(ContentType.JSON)
            .when().delete("/api/rooms/" + addedRoom.getId())
            .then()
            .statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldFailWhileUpdatingRoomWithInvalidAttributes() {
        UpdateRoomDTO roomDTO = new UpdateRoomDTO(75, 0, -1.);
        JSONObject json = new JSONObject(roomDTO);
        RestAssured.given().spec(employeeSpec).body(json.toString())
            .contentType(ContentType.JSON)
            .when()
            .put("/api/rooms/8378b753-6d05-454b-8447-efb125846fc7")
            .then()
            .assertThat().statusCode(Status.BAD_REQUEST.getStatusCode());
    }
    //endregion

    //region RentTests

    @Test
    void shouldRentRoomForSelfWithStatusCode201() {

        RentRoomForSelfDTO dto = new RentRoomForSelfDTO(LocalDateTime.now().plusDays(10),
            LocalDateTime.now().plusDays(13), false);

        JSONObject req = new JSONObject(dto);
        UUID id = RestAssured.given().spec(clientSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract().jsonPath().getUUID("id");

        RestAssured.when().get("api/rents/" + id)
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo(id.toString()));
    }

    @Test
    void shouldFailRentingRoomForSelfWithStatusCode403() {

        RentRoomForSelfDTO dto = new RentRoomForSelfDTO(LocalDateTime.now().plusDays(10),
            LocalDateTime.now().plusDays(13), false);

        JSONObject req = new JSONObject(dto);
        RestAssured.given().spec(employeeSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());

        RestAssured.given().spec(adminSpec).contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/rooms/a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());

    }

    @Test
    void shouldReturnRentWithStatusCode200() {
        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rents/22208864-7b61-4e6e-8573-53863bd93b35")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo("22208864-7b61-4e6e-8573-53863bd93b35"),
                "board", Matchers.equalTo(true),
                "client.id", Matchers.equalTo("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
                "room.id", Matchers.equalTo("9acac245-25b3-492d-a742-4c69bfcb90cf"));
    }

    @Test
    void shouldFailReturningNoExistingRentWithStatusCode404() {
        RestAssured.given().spec(employeeSpec).when().get("/api/rents/bb6be8cd-6a18-4b24-ac81-90539f86b284")
            .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldRemoveRentWithStatusCode204() {
        RestAssured.given().spec(employeeSpec)
            .when().delete("/api/rents/32208864-7b61-4e6e-8573-53863bd93b35")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rents/32208864-7b61-4e6e-8573-53863bd93b35")
            .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldReturnStatusCode204WhenRemovingNonExistingRent() {
        RestAssured.given().spec(employeeSpec)
            .when().delete("/api/rents/bb6be8cd-6a18-4b24-ac81-90539f86b284")
            .then()
            .statusCode(Status.NO_CONTENT.getStatusCode());

        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rents/bb6be8cd-6a18-4b24-ac81-90539f86b284")
            .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldCreateRentWithStatusCode201() {
        LocalDateTime beginDate = LocalDateTime.of(2023, 11, 22, 11, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 11, 25, 10, 0, 0);

        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            true,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("9acac245-25b3-492d-a742-4c69bfcb90cf"));
        JSONObject body = new JSONObject(dto);

        String id = RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(body.toString())
            .when()
            .post("/api/rents")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .contentType(ContentType.JSON)
            .body("board", Matchers.equalTo(true),
                "client.id", Matchers.equalTo("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
                "room.id", Matchers.equalTo("9acac245-25b3-492d-a742-4c69bfcb90cf"))
            .extract()
            .response()
            .path("id");

        RestAssured.given().spec(employeeSpec)
            .when().get("api/rents/" + id)
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo(id),
                "board", Matchers.equalTo(true),
                "client.id", Matchers.equalTo("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
                "room.id", Matchers.equalTo("9acac245-25b3-492d-a742-4c69bfcb90cf"));

    }

    @Test
    void shouldFailCreatingRentForNonExistingClientWithStatusCode400() {
        LocalDateTime beginDate = LocalDateTime.now().plusYears(1);
        LocalDateTime endDate = beginDate.plusDays(3);
        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            false,
            UUID.fromString("bb6be8cd-6a18-4b24-ac81-90539f86b284"),
            UUID.fromString("dba673f8-0526-4cea-941e-3c8ddd5e4f92"));

        JSONObject body = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(body.toString())
            .when()
            .post("/api/rents")
            .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldFailCreatingRentOfNonExistingRoomWithStatusCode400() {
        LocalDateTime beginDate = LocalDateTime.now().plusYears(1);
        LocalDateTime endDate = beginDate.plusDays(3);
        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            false,
            UUID.fromString("a524d75e-927a-4a10-8c46-6321fff6979e"),
            UUID.fromString("bb6be8cd-6a18-4b24-ac81-90539f86b284"));

        JSONObject body = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(body.toString())
            .when()
            .post("/api/rents")
            .then()
            .assertThat().statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldFailCreatingRentForOverlappingDatesWithStatusCode409() {
        LocalDateTime beginDate = LocalDateTime.of(2023, 10, 1, 11, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 10, 3, 10, 0, 0);
        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            false,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("9acac245-25b3-492d-a742-4c69bfcb90cf"));

        JSONObject json = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(json.toString())
            .when()
            .post("/api/rents")
            .then()
            .assertThat().statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldFailCreatingRentForDatesContainedInExistingRentWithStatusCode409() {
        LocalDateTime beginDate = LocalDateTime.of(2023, 10, 13, 9, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 10, 16, 10, 0, 0);
        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            false,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("9acac245-25b3-492d-a742-4c69bfcb90cf"));

        JSONObject json = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(json.toString())
            .when()
            .post("/api/rents")
            .then()
            .assertThat().statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldFailCreatingRentForDatesContainingExistingRentWithStatusCode409() {
        LocalDateTime beginDate = LocalDateTime.of(2023, 10, 1, 9, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 10, 6, 10, 0, 0);
        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            false,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("9acac245-25b3-492d-a742-4c69bfcb90cf"));

        JSONObject json = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(json.toString())
            .when()
            .post("/api/rents")
            .then()
            .assertThat().statusCode(Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldCreateOnlyOneRentWithConcurrentRequests() throws BrokenBarrierException, InterruptedException {
        int threadNumber = 10;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber + 1);
        List<Thread> threads = new ArrayList<>(threadNumber);
        AtomicInteger numberFinished = new AtomicInteger();
        LocalDateTime begin = LocalDateTime.now().plusDays(40);
        LocalDateTime end = LocalDateTime.now().plusDays(41);
        for (int i = 0; i < threadNumber; i++) {
            threads.add(new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }

                CreateRentDTO dto = new CreateRentDTO(
                    begin,
                    end,
                    false,
                    UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
                    UUID.fromString("b9573aa2-42fa-43cb-baa1-42d06e1bdc8d"));

                JSONObject json = new JSONObject(dto);

                RestAssured.given().spec(employeeSpec)
                    .contentType(ContentType.JSON)
                    .body(json.toString())
                    .when()
                    .post("/api/rents")
                    .then()
                    .extract().response();
                numberFinished.getAndIncrement();
            }));
        }

        threads.forEach(Thread::start);
        cyclicBarrier.await();
        while (numberFinished.get() != threadNumber) {
        }

        Response response = RestAssured.when().get("/api/rooms/b9573aa2-42fa-43cb-baa1-42d06e1bdc8d/rents")
            .then()
            .assertThat().statusCode(Status.OK.getStatusCode())
            .assertThat().contentType(ContentType.JSON)
            .extract().response();
        List<String> jsonResponse = response.jsonPath().getList("$");
        Assertions.assertEquals(1, jsonResponse.size());
    }

    @Test
    void shouldCreateTwoRentsWithNonOverlappingDates() throws BrokenBarrierException, InterruptedException {
        int threadNumber = 4;
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadNumber + 1);
        List<Thread> threads = new ArrayList<>(threadNumber);
        AtomicInteger numberFinished = new AtomicInteger();

        LocalDateTime localDateTime = LocalDateTime.now();

        for (int i = 0; i < threadNumber; i++) {
            int finalI = i;
            threads.add(new Thread(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
                CreateRentDTO dto = new CreateRentDTO(
                    localDateTime.plusDays(1000 + finalI),
                    localDateTime.plusDays(1000 + finalI + 2).minusHours(1),
                    false,
                    UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
                    UUID.fromString("b0f9495e-13a7-4da1-989c-c403ece4e22d"));
                JSONObject json = new JSONObject(dto);

                RestAssured.given().spec(employeeSpec)
                    .contentType(ContentType.JSON)
                    .body(json.toString())
                    .when()
                    .post("/api/rents");
                numberFinished.getAndIncrement();
            }));
        }

        threads.forEach(Thread::start);
        cyclicBarrier.await();
        while (numberFinished.get() != threadNumber) {
        }

        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rooms/b0f9495e-13a7-4da1-989c-c403ece4e22d/rents")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("size()", Matchers.equalTo(2));
    }

    @Test
    void shouldFailWithStatusCode404WhenGettingRentsOfNonExistentRoom() {
        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rooms/bb6be8cd-6a18-4b24-ac81-90539f86b284/rents")
            .then()
            .statusCode(Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void shouldGetEmptyArrayWithStatusCode200() {
        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rooms/0533a035-22de-45e8-b922-5fa8b103b1a2/rents")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .body("$", Matchers.empty());
    }

    @Test
    void shouldUpdateRentBoardAndRecalculateRentCost() {
        JSONObject reqFalse = new JSONObject();
        reqFalse.put("board", false);

        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rents/42208864-7b61-4e6e-8573-53863bd93b35")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo("42208864-7b61-4e6e-8573-53863bd93b35"),
                "board", Matchers.equalTo(true),
                "finalCost", Matchers.equalTo(3000.0F));


        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(reqFalse.toString())
            .when()
            .patch("/api/rents/42208864-7b61-4e6e-8573-53863bd93b35/board")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .body(
                "board", Matchers.equalTo(false),
                "finalCost", Matchers.equalTo(2500.0F));

        // perform GET request to verify changes
        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rents/42208864-7b61-4e6e-8573-53863bd93b35")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo("42208864-7b61-4e6e-8573-53863bd93b35"),
                "board", Matchers.equalTo(false),
                "finalCost", Matchers.equalTo(2500.0F));

        JSONObject reqTrue = new JSONObject();
        reqTrue.put("board", true);

        // update board once again
        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(reqTrue.toString())
            .when()
            .patch("/api/rents/42208864-7b61-4e6e-8573-53863bd93b35/board")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("board", Matchers.equalTo(true),
                "finalCost", Matchers.equalTo(3000.0F));

        // perform GET request to verify changes
        RestAssured.given().spec(employeeSpec)
            .when().get("/api/rents/42208864-7b61-4e6e-8573-53863bd93b35")
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo("42208864-7b61-4e6e-8573-53863bd93b35"),
                "board", Matchers.equalTo(true),
                "finalCost", Matchers.equalTo(3000.0F));

    }

    @Test
    void shouldFailWithStatusCode400WhenBeginDateIsPast() {
        LocalDateTime begin = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(2);

        CreateRentDTO dto = new CreateRentDTO(
            begin,
            end,
            false,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9"));
        JSONObject requestBody = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(requestBody.toString())
            .when().post("api/rents")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldFailWithStatusCode400WhenBeginDateIsAfterEndDate() {
        LocalDateTime begin = LocalDateTime.of(2023, 6, 30, 10, 0);
        LocalDateTime end = LocalDateTime.of(2023, 6, 25, 10, 0);

        CreateRentDTO dto = new CreateRentDTO(
            begin,
            end,
            false,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9"));
        JSONObject requestBody = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(requestBody.toString())
            .when().post("api/rents")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldFailWithStatusCode400WhenBothDatesArePast() {
        LocalDateTime begin = LocalDateTime.of(2020, 6, 30, 10, 0);
        LocalDateTime end = LocalDateTime.of(2020, 6, 29, 10, 0);

        CreateRentDTO dto = new CreateRentDTO(
            begin,
            end,
            false,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("a8f3eebe-df0f-48e5-a6c9-3bf1a914b3b9"));
        JSONObject requestBody = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(requestBody.toString())
            .when().post("api/rents")
            .then()
            .statusCode(Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void shouldFailWithStatusCode409WhenRemovingAnActiveRent() {
        RestAssured.given().spec(employeeSpec)
            .when().delete("/api/rents/6ddee1ee-9eba-4222-a031-463a849e1886")
            .then()
            .statusCode(409);
    }

    @Test
    void shouldFailCreatingRentForInactiveUserWithStatusCode401() {
        UUID clientId =
            RestAssured.given().spec(adminSpec)
                .when().get("/api/users/search/jakub3")
                .then()
                .extract().jsonPath().getUUID("id");

        LocalDateTime beginDate = LocalDateTime.of(2023, 11, 22, 11, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 11, 25, 10, 0, 0);

        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            true,
            clientId,
            UUID.fromString("0533a035-22de-45e8-b922-5fa8b103b1a2"));
        JSONObject body = new JSONObject(dto);

        RestAssured.given().spec(employeeSpec)
            .contentType(ContentType.JSON)
            .body(body.toString())
            .when()
            .post("/api/rents")
            .then()
            .assertThat().statusCode(Status.UNAUTHORIZED.getStatusCode());
    }

    @Test
    void shouldFailCreatingRentAsClientWithStatusCode403() {
        LocalDateTime beginDate = LocalDateTime.of(2023, 11, 22, 11, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2023, 11, 25, 10, 0, 0);

        CreateRentDTO dto = new CreateRentDTO(
            beginDate,
            endDate,
            true,
            UUID.fromString("bdbe2fcf-6203-47d6-8908-ca65b9689396"),
            UUID.fromString("9acac245-25b3-492d-a742-4c69bfcb90cf"));
        JSONObject body = new JSONObject(dto);

        RestAssured.given().spec(clientSpec)
            .contentType(ContentType.JSON)
            .body(body.toString())
            .when()
            .post("/api/rents")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void shouldFailGettingAllRentsAsClientWithStatusCode403() {
        RestAssured.given().spec(clientSpec)
            .contentType(ContentType.JSON)
            .when()
            .get("/api/rents")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void shouldFailRemovingRentAsClientWithStatusCode403() {
        RestAssured.given().spec(clientSpec)
            .when().delete("/api/rents/32208864-7b61-4e6e-8573-53863bd93b35")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void shouldFailUpdatingRentBoardAsClientWithStatusCode403() {
        JSONObject reqFalse = new JSONObject();
        reqFalse.put("board", false);

        RestAssured.given().spec(clientSpec)
            .contentType(ContentType.JSON)
            .body(reqFalse.toString())
            .when()
            .patch("/api/rents/42208864-7b61-4e6e-8573-53863bd93b35/board")
            .then()
            .statusCode(Status.FORBIDDEN.getStatusCode());
    }
    //endregion

    //region Users
    @Test
    void shouldAddEmployeeAsAdminWithStatusCode201Test() {
        RegisterEmployeeDTO dto = new RegisterEmployeeDTO("jacek1", "Jacek", "Murański", "lolo");

        JSONObject req = new JSONObject(dto);

        String id = RestAssured.with().spec(adminSpec)
            .given()
            .contentType(ContentType.JSON)
            .body(req.toString())
            .when()
            .post("/api/users/employees")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .jsonPath()
            .getString("id");

        RestAssured.with().spec(adminSpec)
            .when()
            .get("/api/users/" + id)
            .then()
            .statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo(id),
                "username", Matchers.equalTo("jacek1"),
                "firstName", Matchers.equalTo("Jacek"),
                "lastName", Matchers.equalTo("Murański"));
    }

    @Test
    void shouldAddClientWithStatusCode201() {
        RegisterClientDTO dto = new RegisterClientDTO("marek347", "Mariusz", "Pasek",
            "0124738", "Łódź", "Wesoła", 7, "hufew");

        JSONObject req = new JSONObject(dto);

        String id = RestAssured.with().spec(employeeSpec)
            .given().contentType(ContentType.JSON)
            .body(req.toString())
            .when().post("/api/users/clients")
            .then().assertThat().statusCode(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode())
            .extract().jsonPath().getString("id");

        RestAssured.with().spec(employeeSpec)
            .when().get("/api/users/" + id)
            .then()
            .statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("id", Matchers.equalTo(id),
                "username", Matchers.equalTo("marek347"),
                "firstName", Matchers.equalTo("Mariusz"),
                "lastName", Matchers.equalTo("Pasek"),
                "personalId", Matchers.equalTo("0124738"),
                "city", Matchers.equalTo("Łódź"),
                "street", Matchers.equalTo("Wesoła"),
                "houseNumber", Matchers.equalTo(7));
    }

    @Test
    void shouldReturnUserListWithStatusCode200() {
        RestAssured.with().spec(adminSpec)
            .when().get("/api/users")
            .then().assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().contentType(ContentType.JSON)
            .assertThat().body("$.size()", Matchers.equalTo(6));
    }

    @Test
    void shouldReturnUserByUsername() {
        RestAssured.with().spec(adminSpec)
            .when().get("/api/users/search/admin")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().body("username", Matchers.equalTo("admin"))
            .assertThat().body("role", Matchers.equalTo("ADMIN"))
            .assertThat().body("active", Matchers.equalTo(true));
    }

    @Test
    void shouldReturnNotFoundStatusWhenSearchingNonExistingUsername() {
        RestAssured.with().spec(adminSpec)
            .when().get("/api/users/search/random_user")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void shouldUpdateUserWithStatusCode200() {
        UpdateClientDTO dto = new UpdateClientDTO("Franciszek", null, null,
            null, "Wesoła", null);

        JSONObject req = new JSONObject(dto);
        String id = "a524d75e-927a-4a10-8c46-6321fff6979e";

        RestAssured.with().spec(adminSpec)
            .when().get("/api/users/" + id)
            .then()
            .statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .body("firstName", Matchers.equalTo("Jakub"),
                "lastName", Matchers.equalTo("Bukaj"),
                "personalId", Matchers.equalTo("3584873"),
                "city", Matchers.equalTo("Krakow"),
                "street", Matchers.equalTo("Smutna"),
                "houseNumber", Matchers.equalTo(13));


        RestAssured.with().spec(adminSpec)
            .given().contentType(ContentType.JSON)
            .body(req.toString())
            .when().put("/api/users/clients/" + id)
            .then()
            .assertThat()
            .statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .body("firstName", Matchers.equalTo("Franciszek"),
                "lastName", Matchers.equalTo("Bukaj"),
                "personalId", Matchers.equalTo("3584873"),
                "city", Matchers.equalTo("Krakow"),
                "street", Matchers.equalTo("Wesoła"),
                "houseNumber", Matchers.equalTo(13));

        RestAssured.with().spec(adminSpec)
            .when().get("/api/users/" + id)
            .then()
            .statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .body("firstName", Matchers.equalTo("Franciszek"))
            .body("lastName", Matchers.equalTo("Bukaj"))
            .body("personalId", Matchers.equalTo("3584873"))
            .body("city", Matchers.equalTo("Krakow"))
            .body("street", Matchers.equalTo("Wesoła"))
            .body("houseNumber", Matchers.equalTo(13));
    }

    @Test
    void shouldActivateUserWithStatusCode200() {
        String id = "a524d75e-927a-4a10-8c46-6321fff6979e";
        RestAssured.with().spec(employeeSpec)
            .when().put("/api/users/" + id + "/activate")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().body("active", Matchers.equalTo(true));

        RestAssured.with().spec(employeeSpec)
            .when().get("/api/users/" + id)
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("active", Matchers.equalTo(true));
    }

    @Test
    void shouldDeactivateUserWithStatusCode200() {
        String id = "a524d75e-927a-4a10-8c46-6321fff6979e";
        RestAssured.with().spec(employeeSpec)
            .when()
            .put("/api/users/" + id + "/deactivate")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().body("active", Matchers.equalTo(false));

        RestAssured.with().spec(employeeSpec)
            .when()
            .get("/api/users/" + id)
            .then()
            .statusCode(Status.OK.getStatusCode())
            .contentType(ContentType.JSON)
            .body("active", Matchers.equalTo(false));
    }

    @Test
    void shouldFailWhenCreatingUserWithSameUsernameWithStatusCode409() {
        RegisterClientDTO clientDTO = new RegisterClientDTO("test1234", "Kamil", "Graczyk",
            "777999", "Łódź", "Piotrkowska", 20, "f23ttD");
        JSONObject json = new JSONObject(clientDTO);

        RestAssured.with().spec(employeeSpec)
            .given().contentType(ContentType.JSON)
            .body(json.toString())
            .when()
            .post("/api/users/clients")
            .then()
            .statusCode(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode());

        RestAssured.with().spec(employeeSpec)
            .given().contentType(ContentType.JSON)
            .body(json.toString())
            .when()
            .post("/api/users/clients")
            .then()
            .statusCode(jakarta.ws.rs.core.Response.Status.CONFLICT.getStatusCode());
    }

    @Test
    void shouldFailWhileRegisteringClientWithInvalidAttributes() {
        RegisterClientDTO clientDTO = new RegisterClientDTO("Wicher2022", "Mariusz!", "Pasek?",
            "0124a738", "Łódź!", "Wesoła@`'", -1, "432423rcf");
        JSONObject json = new JSONObject(clientDTO);
        RestAssured.with().spec(employeeSpec)
            .given().body(json.toString())
            .contentType(ContentType.JSON)
            .when()
            .post("/api/users/clients")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode());
    }
    //endregion
}
