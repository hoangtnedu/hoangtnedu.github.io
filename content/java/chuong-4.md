## 1. Spring Framework, Spring Boot và IoC

Spring Framework cung cấp hạ tầng cho dependency injection, web MVC, giao dịch và nhiều thành phần ứng dụng. Spring Boot giúp khởi tạo ứng dụng, cấu hình theo dependency và chạy với server nhúng. Hai tên này chỉ các vai trò khác nhau trong cùng hệ sinh thái. Auto-configuration dựa vào điều kiện, không thay thế việc hiểu cấu hình CSDL và bảo mật.

Để giữ tương thích với ví dụ, chọn JDK 21, Maven và nhánh Spring Boot 3.5.x cho chuỗi bài này; dùng bản vá có sẵn được nhóm thống nhất và ghi vào `pom.xml`. Đây là môi trường học được chọn, không phải khẳng định phiên bản mới nhất. Boot 3 dùng các namespace Jakarta cho web và persistence. Không trộn dependency `javax.persistence` của dự án cũ vào project này.

Tạo project tại [Spring Initializr](https://start.spring.io/) với Spring Web, Thymeleaf, Validation, Spring Data JPA, H2 Database và Spring Security. Nếu Initializr không còn cung cấp nhánh đã chọn, cần dùng một project template tương thích hoặc cập nhật đồng bộ toàn bộ dependency và hướng dẫn. Không tự ghép các phiên bản jar rời rạc.

## 2. Cấu trúc ứng dụng và constructor injection

Đặt lớp có `@SpringBootApplication` tại package gốc `io.github.hoangtnedu.java`. Bên dưới chia `controller`, `service`, `repository`, `entity` và `config`. Component scan mặc định bắt đầu từ package của lớp ứng dụng, nên một lớp nằm ngoài cây package có thể không được phát hiện.

Controller nhận yêu cầu; service thực thi quy tắc; repository truy cập dữ liệu. Constructor injection thể hiện rõ phụ thuộc và giúp thay đối tượng khi kiểm tra riêng. Ví dụ dưới là khung cấu trúc; `StudentRepository` được định nghĩa ở mục persistence.

```java
@org.springframework.stereotype.Service
public class StudentService {
    private final StudentRepository repository;
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }
    public java.util.List<Student> findAll() {
        return repository.findAll();
    }
}
```

Không trả một connection thô từ repository lên controller. Đối tượng truyền tới view chỉ nên chứa dữ liệu màn hình thực sự cần. Khi nghiệp vụ phát triển, dùng DTO/form object để tránh cho phép client gán trực tiếp các thuộc tính nhạy cảm như role hoặc trạng thái duyệt.

## 3. Spring MVC và Thymeleaf

DispatcherServlet phân phối request tới handler. Controller thêm dữ liệu vào `Model` và trả tên view. Thymeleaf kết hợp template với dữ liệu phía server để sinh HTML. Tên view `students/list` thường tương ứng `src/main/resources/templates/students/list.html`.

```java
@org.springframework.stereotype.Controller
public class StudentController {
    private final StudentService service;
    public StudentController(StudentService service) {
        this.service = service;
    }
    @org.springframework.web.bind.annotation.GetMapping("/students")
    public String list(org.springframework.ui.Model model) {
        model.addAttribute("students", service.findAll());
        return "students/list";
    }
}
```

```html
<!doctype html><html lang="vi" xmlns:th="http://www.thymeleaf.org">
<head><meta charset="utf-8"><title>Sinh viên</title></head><body>
<h1>Danh sách sinh viên</h1><ul>
<li th:each="s : ${students}" th:text="${s.name}">Tên minh họa</li>
</ul></body></html>
```

Dùng `th:text` cho chuỗi người dùng nhập để xuất text đã escape; chỉ dùng HTML không escape khi có nhu cầu rõ và dữ liệu đã được xử lý phù hợp. Sau thêm/sửa thành công, controller trả `redirect:/students`. Khi lỗi validation, trả lại view form để hiển thị lỗi. `BindingResult` đặt ngay sau đối tượng `@Valid` trong chữ ký handler tương ứng.

## 4. JPA, repository và cấu hình dữ liệu

Ví dụ entity và repository dưới cần đặt ở hai tệp riêng, cùng package phù hợp hoặc có import tương ứng. Tên `Student` được dùng xuyên suốt trong service/controller.

```java
@jakarta.persistence.Entity
@jakarta.persistence.Table(name = "students")
public class Student {
    @jakarta.persistence.Id
    @jakarta.persistence.GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;
    @jakarta.validation.constraints.NotBlank
    private String name;
    protected Student() { }
    public Student(String name) { this.name = name; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
```

```java
public interface StudentRepository
        extends org.springframework.data.jpa.repository.JpaRepository<Student, Long> {
    java.util.List<Student> findByNameContainingIgnoreCase(String name);
}
```

JPA là đặc tả mapping/persistence; Hibernate là một implementation; Spring Data JPA tạo lớp trừu tượng repository trên JPA. Quy tắc nghiệp vụ nhiều bước đặt trong service và dùng `@Transactional` tại phương thức public được gọi qua Spring proxy. Cần hiểu trường hợp tự gọi phương thức trong cùng đối tượng vì annotation giao dịch không tự áp dụng như một lời gọi từ bên ngoài qua proxy.

```properties
spring.datasource.url=jdbc:h2:file:./data/java-course
spring.datasource.username=sa
spring.datasource.password=${DB_PASSWORD:}
spring.jpa.hibernate.ddl-auto=update
spring.jpa.open-in-view=false
```

Cấu hình này dành cho thực hành cục bộ. H2 dạng file giúp kiểm tra dữ liệu còn sau khi khởi động lại; H2 dạng memory sẽ mất khi tiến trình kết thúc. `ddl-auto=update` thuận tiện trong lab, còn khi bàn giao cần script/migration và dữ liệu mẫu kiểm soát được. Không đưa tệp CSDL và mật khẩu thật vào Git.

## 5. Spring Security và kiểm soát truy cập

Security filter chain xử lý authentication và authorization trước controller. Authentication xác định người dùng; authorization quyết định quyền của người dùng đối với hành động hoặc tài nguyên. Ẩn nút trên giao diện chỉ giúp trình bày, không bảo vệ URL.

Đoạn sau là lớp cấu hình cho Boot 3.5/Security 6, dùng form login mặc định. Phải có UserDetailsService hoặc nguồn người dùng được cấu hình riêng; bean này không tự tạo tài khoản nghiệp vụ. Có thể dùng tài khoản do Boot sinh cho lần thử đầu, rồi thay bằng tài khoản demo với mật khẩu được băm qua PasswordEncoder.

```java
@org.springframework.context.annotation.Configuration
public class SecurityConfig {
    @org.springframework.context.annotation.Bean
    org.springframework.security.web.SecurityFilterChain chain(
            org.springframework.security.config.annotation.web.builders.HttpSecurity http)
            throws Exception {
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/css/**", "/error").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            .formLogin(org.springframework.security.config.Customizer.withDefaults());
        return http.build();
    }
}
```

Giữ CSRF protection cho biểu mẫu web. Form dùng `th:action` tích hợp với Spring Security để đưa token phù hợp vào yêu cầu. Xóa dữ liệu bằng POST hoặc phương thức thay đổi trạng thái thích hợp, không dùng GET. `hasRole("ADMIN")` kiểm tra authority có tiền tố `ROLE_ADMIN`; cần giữ nhất quán với cách tạo user.

## 6. Kiểm tra luồng hoàn chỉnh và đóng gói

Chạy `mvn test`, sau đó `mvn package`; với plugin Spring Boot đã cấu hình, chạy JAR bằng `java -jar`. Kiểm tra trang chủ, đăng nhập, danh sách, thêm/sửa/xóa, tìm kiếm và truy cập trái quyền. Thử bỏ trống tên, gửi lại request trực tiếp và khởi động lại server để kiểm tra dữ liệu.

Nộp README có JDK, Boot, CSDL, biến môi trường, lệnh chạy và cách tạo tài khoản demo. Bản triển khai không dùng password mẫu hoặc dữ liệu thật của sinh viên. Bốn buổi 17–20 dùng để rà soát phạm vi, kiến trúc, chất lượng và demo theo [hướng dẫn project](project.html).

## Nguồn đọc theo chủ đề

Spring Persistence with Hibernate (Paul Tepper Fisher, Brian D. Murphy, 2010): kiến trúc, domain model, DAO, giao dịch và kiểm thử; cấu hình cũ được đọc để hiểu nguyên lý. Đối chiếu [Boot 3.5 System Requirements](https://docs.spring.io/spring-boot/3.5/system-requirements.html), [Spring Data JPA guide](https://spring.io/guides/gs/accessing-data-jpa/), [Spring MVC](https://docs.spring.io/spring-framework/reference/web/webmvc.html) và [Spring Security authorization](https://docs.spring.io/spring-security/reference/6.5/servlet/authorization/authorize-http-requests.html).
