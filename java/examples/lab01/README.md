# Lab 1: Java SE Console, Maven và JAR

Yêu cầu JDK 17 hoặc 21 và Maven 3.x. Các phiên bản plugin được ghim trong pom.xml.

Tại thư mục chứa pom.xml, chạy:

```sh
java -version
javac -version
mvn -version
mvn clean package
java -jar target/lab01-java-console-1.0.0.jar
```

Mã nguồn trong src/main/java/io/github/hoangtnedu/java/.
App xử lý nhập/xuất, So chứa năm hàm tính toán. Nhập số thực với dấu chấm.

Đối chiếu các ca kiểm tra tại https://hoangtnedu.github.io/java/lab-01.html.
Giới hạn minh họa: tổng nghịch đảo n<=1000000; Fibonacci 1<=n<=93.

HoangTN biên tập từ bài lab được cung cấp; chỉ dùng nhận diện cá nhân.
