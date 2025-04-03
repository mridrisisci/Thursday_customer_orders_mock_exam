package app.dao;

import app.entities.Order;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;
import java.util.Set;

public class OrderDAO extends GenericDAO implements IOrderDAO
{
    public OrderDAO(EntityManagerFactory emf)
    {
        super(emf);
    }


    public List<Order> getAllOrders()
    {
        return super.getAll(Order.class);
    }

    public Order getOrderById(Integer id)
    {
        return super.getById(Order.class, id);
    }

    public Order createOrder(Order order)
    {
        return super.create(order);
    }

    public Order updateOrder(Order order)
    {
        return super.update(order);
    }

    public void deleteOrder(Integer id)
    {
        super.delete(Order.class, id);
    }



}
