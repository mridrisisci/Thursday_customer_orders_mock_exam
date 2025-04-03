package app.entities;

import app.dto.CustomerDTO;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class Customer
{
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Integer id;

    private String name;
    private String email;
    private Integer phone;
    private String address;

    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    @ToString.Exclude
    @JsonManagedReference
    private Set<Order> orders = new HashSet<>();

    public Customer(CustomerDTO customerDTO)
    {
        this.id = customerDTO.getId();
        this.name = customerDTO.getName();
        this.email = customerDTO.getEmail();
        this.phone = customerDTO.getPhone();
        this.address = customerDTO.getAddress();
        this.orders = customerDTO.getOrders();
    }

    public Customer(String name)
    {
        this.name = name;
    }

    public void addOrder(Order order)
    {
        orders.add(order);
        order.setCustomer(this);
    }

    public void deleteOrder(Order order)
    {
        orders.remove(order);
        order.setCustomer(null);
    }
}
