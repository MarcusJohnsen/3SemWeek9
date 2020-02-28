package rest;

import dto.PersonDTO;
import entities.Person;
import facades.PersonFacade;
import utils.EMF_Creator;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.parsing.Parser;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import static org.hamcrest.Matchers.equalTo;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator.DbSelector;
import utils.EMF_Creator.Strategy;

public class PersonResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Person p1, p2;
    private static List<Person> personList = new ArrayList();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactory(DbSelector.TEST, Strategy.CREATE);

        httpServer = startServer();
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }
    
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Sudosa","Kuma", "1234");
        p2 = new Person("Ashayla", "Guev", "12345");
        personList.clear();
        personList.add(p1);
        personList.add(p2);
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2); 
            em.getTransaction().commit();
        } finally { 
            em.close();
        }
    }
    
    @Test
    public void testServerIsUp() {
        System.out.println("Testing is server UP");
        given().when().get("/person").then().statusCode(200);
    }
   
    @Test
    public void testDummyMsg() throws Exception {
        given()
        .contentType("application/json")
        .get("/person/").then()
        .assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("msg", equalTo("Hello World"));   
    }
    
    @Test
    public void testCount() throws Exception {
        given()
        .contentType("application/json")
        .get("/person/count/").then().assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("count", equalTo(2));
    }
    
    @Test
    public void testGetPersonFromID() throws Exception {
        Person expectedPerson = p1;
        given()
        .contentType("application/json")
        .get("/person/id/" + expectedPerson.getId()).then().assertThat()
        .statusCode(HttpStatus.OK_200.getStatusCode())
        .body("firstName", equalTo(expectedPerson.getFirstName()))
        .body("lastName", equalTo(expectedPerson.getLastName()))
        .body("phone", equalTo(expectedPerson.getPhone()));
    }
    
    @Test
    public void testAddPerson() throws Exception {
        String newFirstName = "Rock";
        String newLastName = "Steady";
        String newPhone = "12";
        PersonDTO expectedPersonDTO = new PersonDTO (newFirstName, newLastName, newPhone);
        given()
                .contentType("application/json").body(expectedPersonDTO)
                .when().post("/person/add").then().assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("fName", equalTo(expectedPersonDTO.getFirstName()))
                .body("lName", equalTo(expectedPersonDTO.getLastName()))
                .body("phone", equalTo(expectedPersonDTO.getPhone()));

        given()
                .contentType("application/json")
                .get("/person/count").then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(3));

    }
    
    @Test
    public void testEditPerson() throws Exception {
        String newFirstName = "J";
        String newLastName = "Whistle";
        String newPhone = "15298125";
        PersonDTO expectedPersonDTO = new PersonDTO(newFirstName, newLastName, newPhone);
        given()
                .contentType("application/json").body(expectedPersonDTO)
                .when().post("/Person/edit").then().assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("firstName", equalTo(expectedPersonDTO.getFirstName()))
                .body("lastName", equalTo(expectedPersonDTO.getLastName()))
                .body("phone", equalTo(expectedPersonDTO.getPhone()))
                .body("id", equalTo(Math.toIntExact(expectedPersonDTO.getId())));
    }
    
    @Test
    public void testDeletePerson() throws Exception {
        Person expectedPerson = p2;

        given()
                .contentType("application/json").body(new PersonDTO(expectedPerson))
                .when().post("/person/delete").then().assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("fName", equalTo(expectedPerson.getFirstName()))
                .body("lName", equalTo(expectedPerson.getLastName()))
                .body("phone", equalTo(expectedPerson.getPhone()))
                .body("id", equalTo(Math.toIntExact(expectedPerson.getId())));

        given()
                .contentType("application/json").get("/person/count")
                .then().assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("count", equalTo(1));
    }
}