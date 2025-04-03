package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.OrderController;
import app.controllers.SecurityController;
import app.routes.Routes;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManagerFactory;


public class Main
{
    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    static ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args)
    {
        OrderController orderController = new OrderController(emf);
        SecurityController securityController = new SecurityController(emf);
        Routes routes = new Routes(orderController, securityController);
        objectMapper.registerModule(new JavaTimeModule());

        ApplicationConfig
                .getInstance()
                .initiateServer()
                .setRoute(routes.getRoutes())
                .handleException()
                .setApiExceptionHandling()
                .checkSecurityRoles()
                .startServer(7070);
    }
}