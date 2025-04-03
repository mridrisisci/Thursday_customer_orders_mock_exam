package app.entities;

import app.dto.OrderDTO;
import app.enums.StatusType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "total_amount")
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    @ManyToOne
    @ToString.Exclude
    @JsonBackReference
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Order(OrderDTO orderDTO)
    {
        this.id = orderDTO.getId();
        this.name = orderDTO.getName();
        this.orderDate = orderDTO.getOrderDate();
        this.totalAmount = orderDTO.getTotalAmount();
        this.status = orderDTO.getStatus();
    }

}
