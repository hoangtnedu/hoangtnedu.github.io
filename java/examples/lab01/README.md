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

## Kiểm thử tự động

Từ thư mục chứa pom.xml, chạy `mvn clean verify` để chạy JUnit và tạo JAR.
Từ thư mục gốc repository, chạy `python3 tools/smoke_java.py` để kiểm tra
12 kịch bản nhập/xuất của JAR, gồm các bài toán, dữ liệu sai, phục hồi menu và EOF.

Workflow `.github/workflows/java-examples.yml` chạy trên JDK 17 và 21 khi mã mẫu
hoặc cấu hình kiểm tra thay đổi. Có thể chạy thủ công bằng Actions → Java examples CI → Run workflow.
Mỗi job lưu JAR, báo cáo JUnit và báo cáo chạy JAR trong artifact riêng, giữ 7 ngày.
