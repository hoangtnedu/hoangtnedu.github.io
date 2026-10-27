## 1. Java Platform và quy trình thực thi

Một ứng dụng Java bắt đầu từ mã nguồn `.java`. Trình biên dịch `javac` tạo bytecode `.class`; JVM nạp lớp và thực thi bytecode. JIT là thành phần tối ưu các đoạn mã được thực thi nhiều trong lúc chương trình chạy. Vì vậy, lỗi cú pháp xuất hiện khi biên dịch, còn dữ liệu nhập sai hoặc thiếu thư viện có thể chỉ bộc lộ khi chạy.

JDK cung cấp công cụ phát triển, trong đó có `javac`, `java` và `jar`. JRE là khái niệm môi trường chạy gồm JVM cùng thư viện cần thiết. Khi học với JDK 17 hoặc 21, cài một bản JDK đầy đủ là đủ cho các ví dụ; không phải tìm thêm một bộ JRE riêng. Cần kiểm tra cả đường dẫn thực thi vì IDE có thể sử dụng JDK khác terminal.

Java SE là nền tảng ngôn ngữ, thư viện chuẩn và ứng dụng độc lập. Swing và JDBC nằm trong hướng phát triển ứng dụng Java SE. Jakarta EE bổ sung các đặc tả cho ứng dụng doanh nghiệp, web, persistence và dịch vụ do container quản lý. Java ME hướng tới thiết bị có tài nguyên giới hạn; chuyên mục này chỉ giới thiệu vị trí của nó, không xây dựng project Java ME.

Java xuất hiện công khai năm 1995; hệ sinh thái phát triển từ các ứng dụng độc lập đến ứng dụng doanh nghiệp. Tên Java EE trong sách cũ và Jakarta EE trong tài liệu mới phản ánh hai giai đoạn của nền tảng. Khi chuyển mã nguồn cần xem phiên bản API và namespace, không chỉ đổi tên thư mục dự án.

## 2. Chuẩn bị và kiểm tra môi trường

Cài JDK 17 hoặc 21, một IDE và Maven 3.x. Dùng cùng một JDK trong IDE và terminal. Với Windows, `JAVA_HOME` trỏ tới thư mục JDK, còn `PATH` chứa thư mục `bin`. Không trỏ `JAVA_HOME` tới chính thư mục `bin`.

```sh
java -version
javac -version
mvn -version
```

Lệnh đầu kiểm tra trình chạy, lệnh thứ hai kiểm tra trình biên dịch. Kết quả Maven cho biết Maven đang dùng JDK nào. Nếu `java` tồn tại nhưng `javac` không có, cần kiểm tra lại bản cài đặt và PATH. Sau khi sửa biến môi trường, mở terminal mới rồi kiểm tra lại.

## 3. Tổ chức một dự án Maven

Maven thống nhất cấu trúc thư mục, khai báo thư viện và cách build. Mã sản phẩm nằm ở `src/main/java`, tài nguyên ở `src/main/resources`, mã kiểm thử ở `src/test/java`. Thư mục `target` chứa kết quả sinh ra và có thể xóa để build lại. Không đặt mã nguồn cần giữ trong `target`.

`groupId`, `artifactId` và `version` định danh artifact. Chuyên mục dùng package cá nhân `io.github.hoangtnedu.java`, giữ tên package nhất quán với thư mục và khai báo `package`. Tên lớp bắt đầu bằng chữ hoa; tên phương thức thể hiện hành động.

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>io.github.hoangtnedu</groupId>
  <artifactId>java-console</artifactId>
  <version>1.0.0</version>
  <properties>
    <maven.compiler.release>17</maven.compiler.release>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  <build><plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId><version>3.13.0</version>
    </plugin>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-jar-plugin</artifactId><version>3.4.2</version>
      <configuration><archive><manifest>
        <mainClass>io.github.hoangtnedu.java.App</mainClass>
      </manifest></archive></configuration>
    </plugin>
  </plugins></build>
</project>
```

Các phiên bản plugin ở đây được ghim để bài thực hành có cấu hình cụ thể. Đây không phải tuyên bố về phiên bản mới nhất. `release` xác định mức ngôn ngữ và API đích; không tự cài JDK cho máy.

## 4. Từ chương trình đầu tiên đến JAR

Lưu ví dụ sau vào `src/main/java/io/github/hoangtnedu/java/App.java`. `main` là điểm vào của chương trình Console. Giữ phần nhập/xuất trong `App`; các phép tính của Lab 1 được tách sang lớp `So` để kiểm tra riêng.

```java
package io.github.hoangtnedu.java;

public class App {
    public static void main(String[] args) {
        System.out.println("Hoc Java cung HoangTN");
    }
}
```

```sh
mvn clean package
java -jar target/java-console-1.0.0.jar
```

Maven đi qua các bước cần thiết trước `package`, trong đó có biên dịch và kiểm thử nếu có. `clean` thuộc vòng đời riêng, dùng để dọn kết quả cũ. JAR của ví dụ chứa lớp chương trình và manifest chỉ ra `mainClass`. Với ứng dụng có thư viện ngoài, JAR thông thường chưa tự chứa các dependency; cần chọn cách phân phối classpath hoặc công cụ đóng gói phù hợp.

## 5. Thiết kế bài toán Console có thể kiểm tra

Một hàm tính tổng nên nhận `n` và trả kết quả, thay vì tự đọc từ bàn phím. Cách chia này cho phép dùng lại logic ở giao diện Swing trong chương sau. Với dữ liệu không hợp lệ, thông báo rõ điều kiện bị vi phạm. Không tiếp tục tính toán bằng giá trị mặc định sau khi nhập thất bại.

Lab 1 gồm tổng số chẵn, tổng nghịch đảo, số nguyên tố, phân loại tam giác và Fibonacci. Phải xác định miền giá trị trước khi chọn kiểu dữ liệu: tổng có thể vượt `int`; Fibonacci tăng rất nhanh. Sai số số thực cần được xem xét khi so sánh cạnh và góc vuông. Bản lab trên website bổ sung kiểm tra đầu vào và giới hạn số phần tử Fibonacci.

## 6. Tự kiểm tra và thực hành

- Giải thích đường đi từ `.java` đến `.class`, JVM và JAR. JAR là gói chứa gì?
- Vì sao `mvn -version` có thể hiển thị JDK khác `java -version`?
- Thử bỏ `mainClass` khỏi manifest rồi giải thích thông báo khi chạy JAR.
- Làm [Lab 1: Java SE Console và Maven](lab-01.html), chụp kết quả cho cả dữ liệu đúng và sai.

## Nguồn đọc theo chủ đề

Giáo trình OOP được cung cấp: Chương 2 về Java, Chương 3–5 về lớp và phương thức, Chương 11 về ngoại lệ, Phụ lục A–B về biên dịch và package. Đọc thêm [Maven Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) và [Introduction to Programming Using Java](https://math.hws.edu/javanotes/). Xem [danh mục học liệu](tai-lieu.html) để phân biệt nguồn gốc và phần biên soạn bổ sung.
