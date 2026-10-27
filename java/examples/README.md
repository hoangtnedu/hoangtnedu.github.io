# Bộ mã mẫu thực thi Công nghệ Java — HoangTN

Đủ 16 lab tương ứng bộ PDF đã cung cấp. Nội dung bài tập đầy đủ ở
https://hoangtnedu.github.io/java/bai-tap.html.
Mã mẫu minh họa các chức năng có hướng dẫn và kiểm thử cốt lõi; các bài tự làm
trong tài liệu vẫn là nhiệm vụ người học cần hoàn thiện.

## Cấu trúc

| Lab | Nội dung và mã chạy |
| --- | --- |
| 1 | Năm bài toán Console, JAR độc lập |
| 2 | Student, GradeCalculator, điểm 10%–30%–60%, JAR |
| 3 | Swing: chào, tổng, phương trình, tam giác, Fibonacci |
| 4 | SwingWorker: đếm ngược, tiến trình, tổng nguyên tố, Fibonacci, đếm dòng, hủy |
| 5 | MiniShop: DAL/BUS/GUI, CRUD, hóa đơn, transaction, thống kê |
| 6 | Servlet/JSP/JSTL, Session, Filter, Listener, dữ liệu bộ nhớ |
| 7 | CRUD MVC, sửa/xóa/tìm, validation và escape đầu ra |
| 8 | Faces, CDI, Bean Validation, message và dataTable |
| 9 | JPA EntityManager, CRUD/JPQL, unique và rollback |
| 10 | Tài khoản JPA, password hash, role ADMIN/STAFF/USER, URL và CSRF |
| 11 | Boot, Controller, Model, Thymeleaf, trang chủ/giới thiệu/danh sách |
| 12 | MVC, form binding, validation, dữ liệu bộ nhớ |
| 13 | Data JPA, CRUD, tìm kiếm và CSDL |
| 14 | Spring Security, login/logout, role và CSRF |
| 15 | Course/Enrollment, đăng ký học phần, kiểm tra trùng và khóa ngoại |
| 16 | Customer WAR (JPA), Warehouse Swing/JDBC, Management Boot/Data JPA |

## Build và chạy kiểm thử

Cần JDK 17 hoặc 21, Maven 3.6.3 trở lên, Python 3, MySQL 8.x và màn hình
cho các kiểm thử Swing. Trên Linux không có màn hình, dùng Xvfb.
Clone repository hoặc giải nén gói mã nguồn, giữ nguyên `java/examples/pom.xml`
và thư mục từng lab vì các lab Java SE/Jakarta sử dụng POM cha dùng chung.

Trong môi trường thực hành cục bộ, tạo một database rỗng, gán tài khoản chỉ có
quyền trên database này rồi cấu hình biến môi trường trước khi build:

```sh
export DB_URL='jdbc:mysql://127.0.0.1:3306/java_labs?serverTimezone=UTC'
export DB_USER='lab'
export DB_PASSWORD='mat-khau-database-cua-ban'
export LAB_DEMO_PASSWORD='mat-khau-demo-do-ban-chon'
xvfb-run -a mvn -f java/examples/pom.xml clean verify
python3 tools/smoke_java.py
python3 tools/java_suite_smoke.py
python3 tools/java_test_report.py
```

Windows có màn hình không cần `xvfb-run -a`; đặt biến bằng PowerShell:
`$env:DB_URL='...'`, `$env:DB_USER='...'`, `$env:DB_PASSWORD='...'`.
Các project riêng có thể build từ thư mục chứa POM của chúng.
Mã mẫu JDBC/JPA/Spring có mặc định H2 cục bộ để bắt đầu nhanh; CI dùng MySQL thật.
Lab 16 cần cả ba ứng dụng trỏ cùng DB_URL; dùng MySQL khi chạy ba tiến trình riêng.

## Chạy sản phẩm

- Lab 1: `java -jar java/examples/lab01/target/lab01-java-console-1.0.0.jar`.
- Lab 2–5: `java -jar java/examples/labNN/target/labNN.jar`.
- Lab 6–10: copy WAR trong `labNN/target/` vào `webapps` của Tomcat 10.1.x.
  Mở `/labNN/hello`, `/labNN/auth`, `/labNN/students`; Lab 8 thêm `/lab08/students.xhtml`.
- Lab 11–15: `java -jar java/examples/labNN/target/labNN.jar`, mở http://localhost:8080.
- Lab 16: xem `lab16/README.md`.

Lab 6–10 có tài khoản `admin`, `staff`, `user`. Lab 14–15 có `admin`, `user`.
Mật khẩu lấy từ LAB_DEMO_PASSWORD; khi chưa đặt biến, mặc định là
`change-me-local`, chỉ dùng cho máy thực hành. Lab 10 giữ tài khoản trong CSDL:
nếu đổi password môi trường sau khi seed, cập nhật/xóa dữ liệu demo cũ trước khi seed lại.

## GitHub Actions

Workflow `Java examples CI` chạy toàn bộ reactor trên JDK 17 và 21, MySQL 8.4,
màn hình ảo cho Swing và container Tomcat nhúng cho các kiểm thử WAR.
Mỗi lần thay đổi mã nguồn sẽ tự chạy; có thể chọn Run workflow thủ công.
Artifacts gồm JAR/WAR, báo cáo JUnit/Failsafe và bảng trạng thái từng lab, giữ 7 ngày.
Trang GitHub Pages là nơi đọc học liệu/lấy mã; Servlet/Faces/Spring chạy trên
server Java hoặc trong phiên CI, không chạy trực tiếp trong GitHub Pages.
