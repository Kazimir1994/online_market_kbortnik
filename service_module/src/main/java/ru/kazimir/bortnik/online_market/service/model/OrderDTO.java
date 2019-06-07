package ru.kazimir.bortnik.online_market.service.model;

import ru.kazimir.bortnik.online_market.repository.model.OrderStatusEnum;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class OrderDTO {
    private Long id;

    @NotNull
    @Min(1)
    private Integer quantity;
    private Long orderNumber;
    private BigDecimal totalPrice;
    private OrderStatusEnum status;
    private UserDTO user;
    private ItemDTO item;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public ItemDTO getItem() {
        return item;
    }

    public void setItem(ItemDTO item) {
        this.item = item;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", orderNumber=" + orderNumber +
                ", totalPrice=" + totalPrice +
                ", status=" + status +
                ", user=" + user +
                ", item=" + item +
                '}';
    }
}
