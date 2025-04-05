package app.controllers;

import app.dao.CrudDAO;
import app.dao.OrderDAO;
import app.dto.ErrorMessage;
import app.dto.OrderDTO;
import app.dto.PackingListDTO;
import app.entities.Customer;
import app.entities.Order;
import app.entities.PackingList;
import app.utils.DataAPIReader;
import app.utils.Populator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class PackingListController implements IController
{
    private final CrudDAO dao;
    private static final Logger logger = LoggerFactory.getLogger(PackingListController.class);
    private final String BASE_URL = "https://packingapi.cphbusinessapps.dk/packinglist";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private DataAPIReader dataAPIReader;

    public PackingListController(DataAPIReader dataAPIReader, EntityManagerFactory emf)
    {
        this.dataAPIReader = dataAPIReader;
        dao = new OrderDAO(emf);
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    public PackingListController(DataAPIReader dataAPIReader, CrudDAO dao)
    {
        this.dataAPIReader = dataAPIReader;
        this.dao = dao;
    }


    public void getPackingList(Context ctx)
    {
        List<PackingListDTO> list;
        try
        {
            String json = dataAPIReader.getDataFromClient(BASE_URL);
            JsonNode node = objectMapper.readValue(json, JsonNode.class).get("categories");
            list = objectMapper.convertValue(node, new TypeReference<List<PackingListDTO>>() {});
            ctx.json(list);
        } catch (Exception e)
        {
            logger.error("Error getting packing list: ", e);
            ErrorMessage error = new ErrorMessage("Error getting packing list");
            ctx.status(404).json(error);
        }
    }



    @Override
    public void getAll(Context ctx)
    {
        try
        {
            ctx.json(dao.getAll(Order.class));
        }
        catch (Exception ex)
        {
            logger.error("Error getting entities", ex);
            ErrorMessage error = new ErrorMessage("Error getting entities");
            ctx.status(404).json(error);
        }
    }



    @Override
    public void getById(Context ctx)
    {

        try {
            //long id = Long.parseLong(ctx.pathParam("id"));
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i>0, "id must be at least 0")
                    .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            OrderDTO foundEntity = new OrderDTO(dao.getById(Order.class, id));
            ctx.json(foundEntity);

        } catch (Exception ex){
            logger.error("Error getting entity", ex);
            ErrorMessage error = new ErrorMessage("No entity with that id");
            ctx.status(404).json(error);
        }
    }

    @Override
    public void create(Context ctx)
    {
        try
        {
            OrderDTO incomingTest = ctx.bodyAsClass(OrderDTO.class);
            Order entity = new Order(incomingTest);
            Order createdEntity = dao.create(entity);
            /*for (Room room : entity.getRooms())
            {
                room.setHotel(createdEntity);
                dao.update(room);
            }
            ctx.json(new HotelDTO(createdEntity));*/
        }
        catch (Exception ex)
        {
            logger.error("Error creating entity", ex);
            ErrorMessage error = new ErrorMessage("Error creating entity");
            ctx.status(400).json(error);
        }
    }

    public void update(Context ctx)
    {

        try
        {
            //int id = Integer.parseInt(ctx.pathParam("id"));
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i>0, "id must be at least 0")
                    .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            OrderDTO incomingEntity = ctx.bodyAsClass(OrderDTO.class);
            Order orderToUpdate = dao.getById(Order.class, id);
            if (incomingEntity.getName() != null)
            {
                orderToUpdate.setName(incomingEntity.getName());
            }
            if (incomingEntity.getStatus() != null)
            {
                orderToUpdate.setStatus(incomingEntity.getStatus());
            }
            Order updatedOrder = dao.update(orderToUpdate);
            OrderDTO returnedEntity = new OrderDTO(updatedOrder);
            ctx.json(returnedEntity);
        }
        catch (Exception ex)
        {
            logger.error("Error updating entity", ex);
            ErrorMessage error = new ErrorMessage("Error updating entity");
            ctx.status(400).json(error);
        }
    }

    public void delete(Context ctx)
    {
        try
        {
            //long id = Long.parseLong(ctx.pathParam("id"));
            long id = ctx.pathParamAsClass("id", Long.class)
                    .check(i -> i>0, "id must be at least 0")
                    .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            dao.delete(Order.class, id);
            ctx.status(204);
        }
        catch (Exception ex)
        {
            logger.error("Error deleting entity", ex);
            ErrorMessage error = new ErrorMessage("Error deleting entity");
            ctx.status(400).json(error);
        }
    }

    public void getOrders(@NotNull Context context)
    {
        try
        {
            long id = context.pathParamAsClass("id", Long.class)
                    .check(i -> i>0, "id must be at least 0")
                    .getOrThrow((valiappor) -> new BadRequestResponse("Invalid id"));
            Customer customer = dao.getById(Customer.class, id);
            context.json(customer.getOrders());
        }
        catch (Exception ex)
        {
            logger.error("Error getting orders", ex);
            ErrorMessage error = new ErrorMessage("Error getting orders");
            context.status(404).json(error);
        }
    }
}
