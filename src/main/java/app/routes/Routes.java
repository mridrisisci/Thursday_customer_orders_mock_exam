package app.routes;

import app.config.HibernateConfig;
import app.controllers.OrderController;
import app.controllers.PackingListController;
import app.utils.DataAPIReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import app.controllers.SecurityController;
import app.enums.Roles;
import io.javalin.apibuilder.EndpointGroup;
import jakarta.persistence.EntityManagerFactory;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes
{
    private final OrderController orderController;
    private final SecurityController securityController;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private final DataAPIReader dataAPIReader = new DataAPIReader();
    private final PackingListController packingListController = new PackingListController(dataAPIReader, emf);

    public Routes(OrderController orderController, SecurityController securityController)
    {
        this.orderController = orderController;
        this.securityController = securityController;
    }

    public  EndpointGroup getRoutes()
    {
        return () -> {
            path("orders", orderRoutes());
            path("auth", authRoutes());
            path("protected", protectedRoutes());
            path("packing", packingRoutes());
        };
    }
    private EndpointGroup packingRoutes()
    {
        return () -> {
            get("/", packingListController::getPackingList);
        };
    }

    private  EndpointGroup orderRoutes()
    {
        return () -> {
            get("/all", orderController::getAll, Roles.ADMIN);
            post("/", orderController::create, Roles.ANYONE);
            get("/{id}", orderController::getById, Roles.ANYONE);
            put("/{id}", orderController::update, Roles.ANYONE);
            delete("/{id}", orderController::delete, Roles.USER_WRITE, Roles.ADMIN);
            post("/populate", (ctx) -> orderController.populateDB(emf), Roles.ADMIN, Roles.USER_WRITE);
            get("/search/{status}", orderController::searchByStatus, Roles.USER_READ);
        };
    }

    private  EndpointGroup authRoutes()
    {
        return () -> {
            get("/test", ctx->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from Open")), Roles.ANYONE);
            get("/healthcheck", securityController::healthCheck, Roles.ANYONE);
            post("/login", securityController::login, Roles.ANYONE);
            post("/register", securityController::register, Roles.ANYONE);
            get("/verify", securityController::verify , Roles.ANYONE);
            get("/tokenlifespan", securityController::timeToLive , Roles.USER_READ);
        };
    }

    private  EndpointGroup protectedRoutes()
    {
        return () -> {
            get("/user_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from USER Protected")), Roles.USER_READ);
            get("/admin_demo",(ctx)->ctx.json(jsonMapper.createObjectNode().put("msg",  "Hello from ADMIN Protected")), Roles.ADMIN);
        };
    }

}
