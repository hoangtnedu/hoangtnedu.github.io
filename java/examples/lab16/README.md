# Lab 16 — Hệ thống đơn hàng đa nền tảng

- `common`: schema dùng chung, Order entity, CustomerService bằng JPA.
- `customer`: Customer Portal Servlet, WAR cho Tomcat 10.1.x.
- `warehouse`: ứng dụng Swing/JDBC để tiếp nhận và đóng gói.
- `management`: Spring Boot + Data JPA + Security + Thymeleaf để duyệt giao.

Ba tiến trình phải dùng cùng DB_URL, DB_USER, DB_PASSWORD và LAB_DEMO_PASSWORD.
Database.init tạo bảng nếu chưa có và seed tài khoản customer/warehouse/manager/admin
bằng BCrypt. Tài khoản chỉ phục vụ môi trường thực hành.

Build từ thư mục gốc repository:

```sh
mvn -f java/examples/pom.xml -pl lab16/management -am clean verify
```

Copy `java/examples/lab16/customer/target/customer.war` vào Tomcat; mở
http://localhost:8080/customer/orders. Trình duyệt yêu cầu tài khoản customer.
Đặt LAB_DEMO_PASSWORD ở tiến trình Tomcat trước khi chạy.

Warehouse dùng classpath có các dependency:

```sh
mvn -f java/examples/lab16/warehouse/pom.xml dependency:copy-dependencies
java -cp 'java/examples/lab16/warehouse/target/warehouse.jar:java/examples/lab16/warehouse/target/dependency/*' io.github.hoangtnedu.java.warehouse.App
```

Với Windows thay dấu `:` phân cách classpath bằng `;`.
Để Maven tìm orders-common từ một lần build riêng, trước đó chạy
`mvn -f java/examples/pom.xml -pl lab16/warehouse -am install -DskipTests`.

Chạy Management Portal trên cổng khác Tomcat:

```sh
java -jar java/examples/lab16/management/target/management.jar --server.port=8081
```

Đăng nhập manager. Trình tự: Customer tạo đơn PENDING; Warehouse chọn đơn,
chuyển PROCESSING để trừ kho rồi READY; Manager duyệt SHIPPING;
Customer xác nhận COMPLETED. Mỗi bước ghi lịch sử. Kiểm thử tích hợp dùng
Customer HTTP thật, WarehouseService/JDBC và Spring MVC/Data JPA trên cùng MySQL;
đồng thời kiểm tra thiếu kho rollback, thao tác nhận đơn lặp và từ chối sai role.

Các chức năng tự làm như hủy/hoàn kho, dashboard, tìm kiếm nâng cao và quản trị
tài khoản vẫn giữ trong nội dung bài lab, không được đánh dấu hoàn thành bởi bộ mẫu này.
