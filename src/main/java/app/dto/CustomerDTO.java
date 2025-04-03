package app.dto;

import app.entities.Customer;
import app.entities.Order;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO
{
    private Integer id;
    private String name;
    private String email;
    private Integer phone;
    private String address;
    private Set<Order> orders = new HashSet<>();

    public CustomerDTO(Customer customer)
    {
        this.name = customer.getName();
        this.email = customer.getEmail();
        this.phone = customer.getPhone();
        this.address = customer.getAddress();
        this.orders = customer.getOrders();
    }

}
