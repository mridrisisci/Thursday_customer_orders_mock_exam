package app.controllers;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.dto.CustomerDTO;
import app.dto.OrderDTO;
import app.entities.Customer;
import app.entities.Order;
import app.enums.StatusType;
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

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class OrderResourceTest
{

    private static final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    final ObjectMapper objectMapper = new ObjectMapper();
    Customer c1, c2;
    Order o1, o2, o3;
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
            c1 = new Customer("TestEntityA");
            c2 = new Customer("TestEntityB");
            o1 = new Order("Test Order A");
            o2 = new Order("Test Order B");
            o3 = new Order("Test Order C");
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Customer ").executeUpdate();
            em.createQuery("DELETE FROM Order ").executeUpdate();

            em.persist(c1);
            em.persist(c2);

            em.persist(o1);
            em.persist(o2);
            em.persist(o3);
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
        given().when().get("/orders/all").then().statusCode(200).body("size()", equalTo(3));
    }

    @Test
    void getById()
    {
        given().when().get("/orders/" + c2.getId()).then().statusCode(200).body("id", equalTo(c2.getId().intValue()));
    }

    @Test
    void create()
    {
        try
        {
            String json = objectMapper.createObjectNode()
                .put("name: ", "test order 1")
                .put("order date:", LocalDate.now().toString())
                .put("total amount:", 100)
                .put("status:", StatusType.PENDING.toString())
                .toString();
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .post("/orders")
                    .then()
                    .statusCode(201)
                    .body("name", equalTo("test order 1"));
        } catch (Exception e)
        {
            logger.error("Error creating order", e);

            fail();
        }
    }

    @Test
    void update()
    {
        Customer entity = new Customer("New entity2");
        try
        {
            String json = objectMapper.writeValueAsString(new CustomerDTO(entity));
            given().when()
                    .contentType("application/json")
                    .accept("application/json")
                    .body(json)
                    .put("/orders/" + c1.getId()) // double check id
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
                .delete("/orders/" + c1.getId())
                .then()
                .statusCode(204);
    }
}