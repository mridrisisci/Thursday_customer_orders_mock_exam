package app.dao;

import app.entities.Order;

import java.util.List;
import java.util.Set;

public interface IOrderDAO
{
    public List<Order> getAllOrders();
    public Order getOrderById(Integer id);
    public Order createOrder(Order order);
    public Order updateOrder(Order order);
}
