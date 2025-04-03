package app;

import app.config.ApplicationConfig;
import app.config.HibernateConfig;
import app.controllers.OrderController;
import app.controllers.SecurityController;
import app.routes.Routes;
import jakarta.persistence.EntityManagerFactory;


public class Main
{
    private final static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();


    public static void main(String[] args)
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
                .startServer(7070);
    }
}