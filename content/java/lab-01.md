## Mục tiêu và chuẩn bị

Xây dựng ứng dụng Java SE Console bằng Maven, nhận dữ liệu qua bàn phím, tách logic vào lớp `So`, đóng gói JAR và chạy bằng JVM. Lab giữ năm bài toán trong tệp Lab 1 được cung cấp. Mã được biên tập lại để xử lý nhập sai và làm rõ miền giá trị.

Dùng JDK 17 hoặc 21, Maven 3.x, IDE tùy chọn và terminal. Chạy `java -version`, `javac -version`, `mvn -version`, kiểm tra JDK nhất quán. Đọc [Chương 1](chuong-1.html) trước khi bắt đầu.

## Bài 1. Tổng số chẵn

Nhập số nguyên dương `n`, tính tổng 2 + 4 + ... đến số chẵn lớn nhất không vượt `n`. Bản đầu dùng vòng lặp; bản mở rộng dùng `k = n / 2`, tổng bằng `k * (k + 1)`. Dùng `long` cho kết quả và chuyển kiểu trước phép nhân.

- Kiểm tra: n=10 cho 30; n=9 cho 20; n=1 cho 0.
- n=0 và n âm phải bị từ chối. Thử một n lớn để giải thích vì sao không dùng `int` cho tổng.

## Bài 2. Tổng nghịch đảo

Tính 1 + 1/2 + ... + 1/n với n nguyên dương. Dùng `1.0 / i` để tránh phép chia nguyên. Mã minh họa giới hạn n không vượt một triệu để thao tác tương tác không chạy quá lâu.

- Kiểm tra: n=1 cho 1; n=4 cho xấp xỉ 2.0833 khi in bốn chữ số thập phân.
- So sánh kết quả theo sai số, không yêu cầu hai số thực tính theo thứ tự khác nhau phải giống tuyệt đối.

## Bài 3. Số nguyên tố

Nhận một số nguyên n và trả kết quả đúng/sai. Các số nhỏ hơn 2 không là nguyên tố. Sau khi xét 2 và loại số chẵn, chỉ cần thử các ước lẻ đến căn bậc hai của n. Mã dùng điều kiện `i <= n / i` để tránh phép nhân `i * i` vượt miền kiểu số.

- Kiểm tra: -5, 0, 1 cho sai; 2 và 17 cho đúng; 21 và 49 cho sai.
- Giải thích vì sao không cần thử mọi ước đến n-1.

## Bài 4. Kiểm tra và phân loại tam giác

Nhập ba cạnh a, b, c. Từ chối giá trị không hữu hạn, không dương hoặc không thỏa bất đẳng thức tam giác. Nếu hợp lệ, phân loại đều, vuông cân, vuông, cân hoặc thường. Thứ tự kiểm tra cần tránh gán tam giác đều thành tam giác cân.

- Kiểm tra: (3,4,5) vuông; (3,3,3) đều; (2,2,3) cân; (2,3,4) thường; (1,2,3) không phải tam giác.
- Thử đảo thứ tự cạnh và nhập NaN/Infinity. Kết quả không được phụ thuộc thứ tự nhập.
- Mã minh họa chuẩn hóa cạnh theo cạnh lớn nhất và dùng sai số 1e-9 cho so sánh phân loại. Đây là quy ước tính toán của lab; dữ liệu gần suy biến chịu giới hạn biểu diễn số thực.

## Bài 5. Dãy Fibonacci

Hiển thị n số đầu bắt đầu bằng 0, 1, 1, 2... Với kiểu `long`, giới hạn lab là 1 đến 93 phần tử, tức đến F92. Chỉ cộng để tạo phần tử tiếp theo khi còn cần xuất, tránh tính dư một phần tử gây tràn.

- Kiểm tra: n=1 cho `0`; n=2 cho `0 1`; n=7 cho `0 1 1 2 3 5 8`.
- n=93 kết thúc bằng 7540113804746346429; n=94 bị từ chối.
- Bài mở rộng: dùng BigInteger để bỏ giới hạn `long`, kèm một giới hạn n phù hợp cho giao diện.

## Tổ chức project và mã minh họa

Tạo thư mục `lab01-java-console`, dùng `pom.xml` và hai lớp `App.java`, `So.java` ở đúng đường dẫn package. `So` không đọc bàn phím. `App` điều khiển menu và chuyển dữ liệu sang các hàm. Đọc từng dòng bằng `nextLine` rồi parse giúp tránh dữ liệu lỗi còn kẹt trong Scanner.

Các tệp bên dưới là mã của lab đã biên tập. Nếu trình duyệt hiển thị trực tiếp, dùng Lưu thành hoặc nút sao chép; giữ đúng tên tệp và thư mục.

- [pom.xml](examples/lab01/pom.xml)
- [So.java](examples/lab01/src/main/java/io/github/hoangtnedu/java/So.java)
- [App.java](examples/lab01/src/main/java/io/github/hoangtnedu/java/App.java)
- [README và cách chạy](examples/lab01/README.md)

## Build, chạy và kiểm tra

```sh
mvn clean package
java -jar target/lab01-java-console-1.0.0.jar
```

Thử đủ năm bài, dữ liệu biên, dữ liệu rỗng và chuỗi không phải số. Nhập sai phải nhận thông báo và quay về menu. Phân biệt ba loại lỗi: môi trường không tìm thấy lệnh; biên dịch không tìm thấy lớp/package; chương trình chạy nhưng tính sai.

## Sản phẩm và tiêu chí đánh giá

Nộp project Maven, JAR chạy được, ảnh kiểm tra môi trường, kết quả năm bài và báo cáo giải thích `.java` → `.class` → JVM. Đặt gói nộp `Lab01_MSSV_HoTen.zip` và không chứa mật khẩu hoặc tệp cá nhân không liên quan.

- Môi trường phát triển hoạt động: 1,5 điểm.
- Cấu trúc project, package và POM: 1,5 điểm.
- Lớp So và kiểm tra dữ liệu: 2,0 điểm.
- Kết quả năm bài Console: 2,5 điểm.
- Menu, nhập dữ liệu và thông báo: 1,0 điểm.
- Đóng gói và chạy JAR: 1,0 điểm.
- Báo cáo, bằng chứng và cách nộp: 0,5 điểm.

Tổng 10 điểm, giữ cơ cấu tiêu chí của lab nguồn. Chỉ tính hoàn thành khi có kết quả chạy, không chỉ có ảnh mã nguồn.
