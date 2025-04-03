package app.controllers;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.routes.Routes;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderResourceTest
{

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    final ObjectMapper objectMapper = new ObjectMapper();
    Hotel t1, t2;
    final Logger logger = LoggerFactory.getLogger(OrderResourceTest.class.getName());


    @BeforeAll
    static void setUpAll()
    {
        OrderController orderController = new OrderController(emf);
        SecurityController securityController = new SecurityController(emf);
        Routes routes = new Routes(orderController, securityController);
        ApplicationConfig
                .getInstance()
                .initiateServer()
                .setRoute(routes.getRoutes())
                .handleException()
                .setApiExceptionHandling()
                .checkSecurityRoles()
                .startServer(7078);
        RestAssured.baseURI = "http://localhost:7078/api";
    }

    @BeforeEach
    void setUp()
    {
        try (EntityManager em = emf.createEntityManager())
        {
            //TestEntity[] entities = EntityPopulator.populate(genericDAO);
            t1 = new Hotel("TestEntityA");
            t2 = new Hotel("TestEntityB");
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Room ").executeUpdate();
            em.createQuery("DELETE FROM Hotel ").executeUpdate();

            em.persist(t1);
            em.persist(t2);
            em.getTransaction().commit();
        }
        catch (Exception e)
        {
            logger.error("Error setting up test", e);
        }
    }

    @Test
    void getAll()
    {
        given().when().get("/hotel").then().statusCode(200).body("size()", equalTo(2));
    }

    @Test
    void getById()
    {
        given().when().get("/hotel/" + t2.getId()).then().statusCode(200).body("id", equalTo(t2.getId().intValue()));
    }

    @Test
    void create()
    {
        Hotel entity = new Hotel("Thor Partner Hotel", "Carl Gustavs Gade 1");
        Room room = new Room("201");
        entity.addRoom(room);
        try
        {
            String json = objectMapper.writeValueAsString(new HotelDTO(entity));
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .post("/hotel")
                    .then()
                    .statusCode(200)
                    .body("name", equalTo(entity.getName()))
                    .body("address", equalTo(entity.getAddress()));
                    //.body("rooms.size()", equalTo(1));
        } catch (JsonProcessingException e)
        {
            logger.error("Error creating hotel", e);

            fail();
        }
    }

    @Test
    void update()
    {
        Hotel entity = new Hotel("New entity2");
        try
        {
            String json = objectMapper.writeValueAsString(new HotelDTO(entity));
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .put("/hotel/" + t1.getId()) // double check id
                    .then()
                    .statusCode(200)
                    .body("name", equalTo("New entity2"));
        } catch (JsonProcessingException e)
        {
            logger.error("Error updating entity", e);
            fail();
        }
    }

    @Test
    void delete()
    {
        given().when()
                .delete("/hotel/" + t1.getId())
                .then()
                .statusCode(204);
    }
}