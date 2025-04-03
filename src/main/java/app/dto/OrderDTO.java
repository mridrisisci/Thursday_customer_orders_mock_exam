package app.dto;

import app.entities.Order;
import app.enums.StatusType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO
{
    private Integer id;
    private String name;

    @Column(name = "order_date")
    @JsonProperty("order_date")
    private LocalDate orderDate;

    @Column(name = "total_amount")
    @JsonProperty("total_amount")
    private Integer totalAmount;

    @Enumerated(EnumType.STRING)
    private StatusType status;

    public OrderDTO(Order order)
    {
        this.name = order.getName();
        this.orderDate = order.getOrderDate();
        this.totalAmount = order.getTotalAmount();
        this.status = order.getStatus();
    }

}
