package compensation.domain;

import compensation.domain.OrderPlaced;
import compensation.domain.OrderCancelled;
import compensation.OrderApplication;
import javax.persistence.*;
import java.util.List;
import lombok.Data;
import java.util.Date;
import java.time.LocalDate;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;


@Entity
@Table(name="Order_table")
@Data

//<<< DDD / Aggregate Root
public class Order  {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    
    
    
private Long id;    
    
    
private String productId;    
    
    
private Integer qty;    
    
    
private String customerId;    
    
    
private Double amount;    
    
    
private String status;    
    
    
private String address;

    @PostPersist
    public void onPostPersist(){

        OrderPlaced orderPlaced = new OrderPlaced(this);
        orderPlaced.publishAfterCommit();   
    }

    public static OrderRepository repository(){
        OrderRepository orderRepository = OrderApplication.applicationContext.getBean(OrderRepository.class);
        return orderRepository;
    }




//<<< Clean Arch / Port Method

    public static void updateStatus(OutOfStock outOfStock) {
        repository().findById(outOfStock.getOrderId()).ifPresent(order ->{
            
            order.setStatus("OrderCancelled");
            repository().save(order);
            // OrderCancelled orderCancelled = new OrderCancelled(this);
            // orderCancelled.publishAfterCommit(); 
        });

    }

//>>> Clean Arch / Port Method


}
//>>> DDD / Aggregate Root
