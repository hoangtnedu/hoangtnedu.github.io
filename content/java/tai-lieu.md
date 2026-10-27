## Nguồn khung và bài tập

Khung chuyên mục lấy từ file lịch trình học tập Công nghệ Java được cung cấp: 20 buổi, 4 chương; buổi 17–20 dành cho review project. File kế hoạch giảng dạy dùng để đối chiếu tên chủ đề. Khi hai nguồn phân phối buổi khác nhau, chuyên mục ưu tiên lịch trình 20 buổi có bốn buổi review cuối theo yêu cầu.

Lab_1_Cong_Nghe_Java.docx là lab Java có trong bộ tệp của lần biên soạn này. Năm bài toán gốc được giữ: tổng chẵn, tổng nghịch đảo, nguyên tố, tam giác và Fibonacci. Mã minh họa được biên tập lại với package cá nhân, kiểm tra đầu vào, giới hạn miền dữ liệu và cấu hình Maven nhất quán. Các bài thực hành ở buổi 3–16 là bài bổ sung theo lịch trình, không được gắn nhãn là lab gốc chưa cung cấp.

Nội dung trên website do HoangTN biên soạn phục vụ học tập. Không đưa các bản gốc đề cương, biểu mẫu, mẫu slide hoặc tệp có nhận diện tổ chức lên chuyên mục. Tên tác giả, thư viện và nguồn kỹ thuật được giữ để người học tra cứu.

## Giáo trình và sách được cung cấp

- **Lập trình hướng đối tượng với Java**, tệp OOP_2013.pdf. Dùng Chương 2–5 cho nền tảng, lớp và phương thức; Chương 11 cho ngoại lệ; Chương 13 cho generics/collection; Phụ lục A–B cho JDK và package. Không xác quyết tên tác giả khi trang tiêu đề trong tệp chưa đủ thông tin thư mục.
- **Programming Jakarta Struts**, Chuck Cavaness, O'Reilly, 2002. Tệp đề cập Struts 1.1; dùng phần MVC/Model 2 và vai trò controller/model/view để đọc lịch sử kiến trúc web. Từ “Jakarta” trong tên sách gắn với dự án Apache thời kỳ đó, không đồng nghĩa Jakarta EE hiện nay. Không dùng Struts 1.x làm stack triển khai của chuyên mục.
- **Spring Persistence with Hibernate**, Paul Tepper Fisher và Brian D. Murphy, Apress, 2010. Đọc Chương 1–2 về kiến trúc/DI, Chương 4–6 về persistence/domain/DAO, Chương 7–8 về transaction/kiểm thử. XML/API cũ cần chuyển đổi khi sử dụng trong Boot 3.

Những sách này được dùng làm tài liệu tham khảo để viết giải thích mới. Chuyên mục không phân phối lại toàn văn PDF hoặc sao chép hình minh họa của sách.

## Tài liệu trong danh mục tham khảo

- **Introduction to Programming Using Java**, David J. Eck, phiên bản 9, 2022: [bản chính thức](https://math.hws.edu/javanotes/), [bản dùng Swing](https://math.hws.edu/javanotes-swing/). Phù hợp nền tảng Java 17, OOP, GUI và thread. Trang bản JavaFX không được dùng thay cho bài Swing mà không điều chỉnh ví dụ.
- **Spring Framework Documentation**: [tài liệu chính thức](https://docs.spring.io/spring-framework/reference/). Chọn tài liệu trùng phiên bản framework của project, đặc biệt phần MVC, DI và transaction.
- Mục **Jakarta EE Application Development – David R. Heffelfinger – 2024** có trong tệp danh mục tham khảo được cung cấp, nhưng chưa có bản sách và dữ liệu thư mục đủ để đối chiếu. Mục này chưa được dùng làm nguồn cho các khẳng định kỹ thuật trong bài. Thay vào đó, chuyên mục liên kết tài liệu Jakarta EE chính thức.

## Tra cứu API và công nghệ

- [Maven lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html): build, test, package; [cấu trúc thư mục chuẩn](https://maven.apache.org/guides/introduction/introduction-to-the-standard-directory-layout.html).
- [Oracle SwingWorker JDK 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/swing/SwingWorker.html): EDT, tác vụ nền và lấy kết quả.
- [Oracle JDBC Prepared Statements](https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html): tham số hóa; [JDBC transaction](https://docs.oracle.com/javase/tutorial/jdbc/basics/transactions.html): commit/rollback. Tutorial này dùng JDK 8; API Java SE hiện dùng được tra thêm theo JDK cài đặt.
- [Jakarta EE Learning Hub](https://jakarta.ee/learn/) và [Jakarta EE Tutorial](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/): Servlet, JSP, CDI, Faces và JPA. Xem nhãn phiên bản của từng chương trước khi sao chép ví dụ.
- [Spring Boot 3.5 system requirements](https://docs.spring.io/spring-boot/3.5/system-requirements.html): môi trường của chuỗi bài; [Spring Initializr](https://start.spring.io/): tạo project.
- [Spring MVC reference](https://docs.spring.io/spring-framework/reference/web/webmvc.html), [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/), [JPA getting started](https://spring.io/guides/gs/accessing-data-jpa/).
- [Spring Security 6.5 authorization](https://docs.spring.io/spring-security/reference/6.5/servlet/authorization/authorize-http-requests.html), [form login](https://docs.spring.io/spring-security/reference/6.5/servlet/authentication/passwords/form.html) và [CSRF](https://docs.spring.io/spring-security/reference/6.5/servlet/exploits/csrf.html).
- [Thymeleaf + Spring](https://www.thymeleaf.org/doc/tutorials/3.1/thymeleafspring.html): binding, template và form.

## Quy ước đọc mã mẫu

Mã hoàn chỉnh được ghi rõ tên lớp/tệp và môi trường. Đoạn tích hợp cần các lớp/service đã tạo sẽ được giới thiệu là mẫu ghép, không phải project độc lập. Chuyên mục là tài liệu tĩnh; Java chạy trên máy người học. Hãy thử ca bình thường, ca biên và ca lỗi, ghi lại phiên bản dependency trong project và không đưa dữ liệu bí mật vào mã nguồn.
