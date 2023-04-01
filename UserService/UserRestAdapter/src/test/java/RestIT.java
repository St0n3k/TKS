import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.Response.Status;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.lodz.p.it.tks.user.dto.user.RegisterClientDTO;
import pl.lodz.p.it.tks.user.dto.user.RegisterEmployeeDTO;
import pl.lodz.p.it.tks.user.dto.user.UpdateClientDTO;


@Testcontainers
public class RestIT extends RestTestcontainersSetup {

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
            .post("/user/users/employees")
            .then()
            .statusCode(Status.CREATED.getStatusCode())
            .extract()
            .jsonPath()
            .getString("id");

        RestAssured.with().spec(adminSpec)
            .when()
            .get("/user/users/" + id)
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
            .when().post("/user/users/clients")
            .then().assertThat().statusCode(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode())
            .extract().jsonPath().getString("id");

        RestAssured.with().spec(employeeSpec)
            .when().get("/user/users/" + id)
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
            .when().get("/user/users")
            .then().assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().contentType(ContentType.JSON)
            .assertThat().body("$.size()", Matchers.equalTo(6));
    }

    @Test
    void shouldReturnUserByUsername() {
        RestAssured.with().spec(adminSpec)
            .when().get("/user/users/search/admin")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().body("username", Matchers.equalTo("admin"))
            .assertThat().body("role", Matchers.equalTo("ADMIN"))
            .assertThat().body("active", Matchers.equalTo(true));
    }

    @Test
    void shouldReturnNotFoundStatusWhenSearchingNonExistingUsername() {
        RestAssured.with().spec(adminSpec)
            .when().get("/user/users/search/random_user")
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
            .when().get("/user/users/" + id)
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
            .when().put("/user/users/clients/" + id)
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
            .when().get("/user/users/" + id)
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
            .when().put("/user/users/" + id + "/activate")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().body("active", Matchers.equalTo(true));

        RestAssured.with().spec(employeeSpec)
            .when().get("/user/users/" + id)
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
            .put("/user/users/" + id + "/deactivate")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.OK.getStatusCode())
            .assertThat().body("active", Matchers.equalTo(false));

        RestAssured.with().spec(employeeSpec)
            .when()
            .get("/user/users/" + id)
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
            .post("/user/users/clients")
            .then()
            .statusCode(jakarta.ws.rs.core.Response.Status.CREATED.getStatusCode());

        RestAssured.with().spec(employeeSpec)
            .given().contentType(ContentType.JSON)
            .body(json.toString())
            .when()
            .post("/user/users/clients")
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
            .post("/user/users/clients")
            .then()
            .assertThat().statusCode(jakarta.ws.rs.core.Response.Status.BAD_REQUEST.getStatusCode());
    }
    //endregion
}
