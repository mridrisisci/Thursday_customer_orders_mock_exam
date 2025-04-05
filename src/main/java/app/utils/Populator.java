package app.utils;


import app.config.HibernateConfig;
import app.dao.GenericDAO;
import app.entities.Customer;
import app.entities.Order;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public class Populator
{

    // declare instance variables here
    Order order1, order2, order3, order4, order5, order6;
    Customer customer1, customer2;
    private final EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
    private GenericDAO dao = new GenericDAO(emf);

    public Populator()
    {
        // initialize dummy objects here
        customer1 = Customer.builder()
            .name("John Doe")
            .phone(20202020)
            .email("johndoe@example.com")
            .address("123 Main St")
            .build();
        customer2 = Customer.builder()
            .name("Jane Smith")
            .phone(30303030)
            .email("janesmith@example.com")
            .address("456 Elm St")
            .build();

        order1 = Order.builder()
            .name("Order 1")
            .orderDate(java.time.LocalDate.of(2023, 1, 1))
            .totalAmount(100)
            .status(app.enums.StatusType.PENDING)
            .customer(customer1)
            .build();
        order2 = Order.builder()
            .name("Order 2")
            .orderDate(java.time.LocalDate.of(2023, 2, 1))
            .totalAmount(200)
            .status(app.enums.StatusType.COMPLETED)
            .customer(customer1)
            .build();
        order3 = Order.builder()
            .name("Order 3")
            .orderDate(java.time.LocalDate.of(2023, 3, 1))
            .totalAmount(300)
            .status(app.enums.StatusType.CANCELLED)
            .customer(customer1)
            .build();
        order4 = Order.builder()
            .name("order 4")
            .orderDate(java.time.LocalDate.of(2023, 4, 1))
            .totalAmount(400)
            .status(app.enums.StatusType.PENDING)
            .customer(customer2)
            .build();
        order5 = Order.builder()
            .name("order 5")
            .orderDate(java.time.LocalDate.of(2023, 5, 1))
            .totalAmount(500)
            .status(app.enums.StatusType.COMPLETED)
            .customer(customer2)
            .build();
        order6 = Order.builder()
            .name("order 6")
            .orderDate(java.time.LocalDate.of(2023, 6, 1))
            .totalAmount(600)
            .status(app.enums.StatusType.CANCELLED)
            .customer(customer2)
            .build();
    }

    public Map<String, Order> getOrders()
    {
        Map<String, Order> orders = new HashMap<>();
        orders.put("order1", order1);
        orders.put("order2", order2);
        orders.put("order3", order3);
        orders.put("order4", order4);
        orders.put("order5", order5);
        orders.put("order6", order6);
        return orders;
    }
    public Map<String, Customer> getCustomers()
    {
        Map<String, Customer> customers = new HashMap<>();
        customers.put("customer1", customer1);
        customers.put("customer2", customer2);
        return customers;
    }

    public void createBiDirectional(EntityManager em)
    {
        for (Order order : dao.getAll(Order.class))
        {
            // ensures bi-directional mapping
            customer1.addOrder(order);
            customer1.addOrder(order);
            customer1.addOrder(order);

            customer2.addOrder(order);
            customer2.addOrder(order);
            customer2.addOrder(order);

            // ensures bi-directional mapping is updated to DB
            customer1 = em.merge(customer1);
            customer2 = em.merge(customer2);


        }
    }

    public void resetAndPersistEntities(EntityManager em)
    {
        em.getTransaction().begin();
        em.createQuery("DELETE FROM Order ").executeUpdate();
        em.createQuery("DELETE FROM Customer ").executeUpdate();

        for (Customer entity : getCustomers().values())
        {
            em.persist(entity);
        }
        for (Order entity : getOrders().values())
        {
            em.persist(entity);
        }
        em.getTransaction().commit();
    }
}
