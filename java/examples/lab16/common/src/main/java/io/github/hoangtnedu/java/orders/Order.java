package io.github.hoangtnedu.java.orders;
@jakarta.persistence.Entity @jakarta.persistence.Table(name="orders") public class Order {
 @jakarta.persistence.Id @jakarta.persistence.GeneratedValue(strategy=jakarta.persistence.GenerationType.IDENTITY) public Long id;
 @jakarta.persistence.Column(name="customer_id") public long customerId;
 @jakarta.persistence.Column(name="created_at") public java.time.LocalDateTime createdAt;
 @jakarta.persistence.Column(name="total_amount") public java.math.BigDecimal totalAmount;
 public String status;public String note;@jakarta.persistence.Version public int version;
 public Long getId(){return id;}public String getStatus(){return status;}public int getVersion(){return version;}
}