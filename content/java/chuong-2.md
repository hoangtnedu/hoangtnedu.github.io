## 1. Từ Console sang giao diện theo sự kiện

Trong Console, chương trình thường chủ động hỏi và đọc dữ liệu theo thứ tự. Với Swing, người dùng quyết định lúc nào nhấn nút, chọn hàng hoặc đóng cửa sổ. Chương trình đăng ký bộ lắng nghe để phản hồi các sự kiện. Vì vậy, cần thiết kế trạng thái giao diện: khi chưa chọn bản ghi thì nút sửa/xóa phải xử lý như thế nào, sau khi lưu thì bảng và form phải hiển thị dữ liệu gì.

`JFrame` là cửa sổ chính; `JPanel` nhóm các thành phần; `JLabel`, `JTextField`, `JButton` phục vụ hiển thị và nhập liệu; `JTable` hiển thị dữ liệu dạng bảng. Container chứa component. Layout manager phân bố component khi cửa sổ thay đổi kích thước. `BorderLayout` phù hợp bố cục tổng thể, `FlowLayout` cho thanh thao tác, `GridLayout` cho nhóm ô có kích thước đều.

Tránh đặt toàn bộ tọa độ tuyệt đối vì giao diện dễ hỏng khi đổi font, độ phân giải hoặc nội dung tiếng Việt. Màn hình quản lý sinh viên nên có vùng bộ lọc, bảng danh sách, form chi tiết và thanh thao tác riêng.

## 2. Cửa sổ và xử lý sự kiện tối thiểu

Ví dụ sau là một lớp hoàn chỉnh dùng thư viện chuẩn. Lưu tại `src/main/java/io/github/hoangtnedu/java/StudentWindow.java`. Nếu chạy bằng JAR, cập nhật `mainClass` sang lớp này. Giao diện được tạo trên Event Dispatch Thread bằng `invokeLater`.

```java
package io.github.hoangtnedu.java;

import javax.swing.*;
import java.awt.*;

public class StudentWindow {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("HoangTN - Sinh vien");
            JTextField name = new JTextField(20);
            JLabel status = new JLabel("Nhap ho ten sinh vien");
            JButton save = new JButton("Kiem tra");
            save.addActionListener(event -> {
                String value = name.getText().trim();
                status.setText(value.isBlank()
                    ? "Ho ten khong duoc trong" : "Hop le: " + value);
            });
            JPanel form = new JPanel(new FlowLayout());
            form.add(new JLabel("Ho ten"));
            form.add(name);
            form.add(save);
            frame.add(form, BorderLayout.CENTER);
            frame.add(status, BorderLayout.SOUTH);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
```

Listener đọc dữ liệu, kiểm tra và cập nhật thông báo. Khi mở rộng, đừng đưa toàn bộ nghiệp vụ và SQL vào listener. Một phương thức dài xử lý cả giao diện, kiểm tra và CSDL sẽ khó cô lập lỗi, đặc biệt khi có nhiều nút cùng sử dụng một quy tắc.

## 3. EDT và công việc chạy nền

EDT xử lý sự kiện và cập nhật Swing. Một truy vấn kéo dài trong listener có thể làm cửa sổ ngừng phản hồi. `SwingWorker` cho phép xử lý việc tốn thời gian trong `doInBackground`; `done` trở lại EDT để nhận kết quả. Không cập nhật component trực tiếp từ luồng nền.

Đoạn sau là mẫu ghép trong một listener khi đã có `save`, `status` và `service`. `service.countStudents()` là phương thức do người học xây dựng; không phải API Swing.

```java
save.setEnabled(false);
new SwingWorker<Integer, Void>() {
    protected Integer doInBackground() throws Exception {
        return service.countStudents();
    }
    protected void done() {
        try {
            status.setText("So sinh vien: " + get());
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            status.setText("Tac vu bi gian doan");
        } catch (java.util.concurrent.ExecutionException ex) {
            status.setText("Khong the tai du lieu");
        } finally {
            save.setEnabled(true);
        }
    }
}.execute();
```

Không gọi `get()` ngay sau `execute()` trên EDT vì sẽ chặn giao diện. Với bảng dữ liệu, nên đưa kết quả truy vấn về một danh sách rồi cập nhật table model khi tác vụ kết thúc. Vô hiệu hóa nút phù hợp trong lúc lưu để tránh người dùng gửi lặp thao tác.

## 4. JDBC và truy vấn tham số

JDBC là API truy cập CSDL; driver cụ thể do hệ quản trị cung cấp. Thực hành bắt đầu bằng việc tạo bảng `students(id, name)` rồi thử kết nối độc lập trước khi ghép vào giao diện. Khi dùng Maven, thêm đúng driver theo CSDL lựa chọn. URL, tài khoản và mật khẩu nên đọc từ cấu hình môi trường.

Đoạn mẫu dưới cần có biến `dataSource` kiểu `javax.sql.DataSource` đã được cấu hình và hai biến `id`, `name`. Trong Java SE, namespace `javax.sql` vẫn được dùng; không thay mọi `javax` thành `jakarta`.

```java
String sql = "INSERT INTO students(id, name) VALUES (?, ?)";
try (var connection = dataSource.getConnection();
     var statement = connection.prepareStatement(sql)) {
    statement.setString(1, id);
    statement.setString(2, name);
    int affected = statement.executeUpdate();
    if (affected != 1) {
        throw new java.sql.SQLException("Khong them duoc sinh vien");
    }
}
```

Tham số được đánh số từ 1. `executeQuery` dùng cho truy vấn trả `ResultSet`, còn `executeUpdate` trả số dòng bị tác động. Dùng try-with-resources để đóng kết nối, statement và result set khi kết thúc. Khi sửa/xóa, kiểm tra số dòng bị tác động để phát hiện mã không tồn tại. Gộp nhiều cập nhật liên quan bằng transaction, rollback nếu một bước thất bại.

## 5. Phân chia trách nhiệm bằng MVC và các lớp dịch vụ

Model biểu diễn dữ liệu và trạng thái nghiệp vụ; View hiển thị và nhận thao tác; Controller điều phối hành động. Trong ứng dụng quản lý, Service giữ quy tắc như mã không trùng, còn DAO thực hiện truy vấn. Không đồng nhất Model với một bảng CSDL: model còn có thể chứa trạng thái biểu mẫu và kết quả tính toán.

Ví dụ luồng thêm sinh viên: người dùng bấm Lưu; controller đọc form; service kiểm tra quy tắc; DAO ghi CSDL; kết quả quay về để View tải lại bảng và xóa form. Nếu thất bại, giữ dữ liệu đã nhập để người dùng sửa. Chỉ cập nhật giao diện thành công khi CSDL đã xác nhận thao tác.

## 6. Bài thực hành tổng hợp

- Buổi 3: thiết kế cửa sổ quản lý có bảng và form, co giãn được.
- Buổi 4: thêm kiểm tra rỗng, sự kiện chọn hàng, tác vụ nền và trạng thái nút.
- Buổi 5: viết DAO cho thêm, sửa, xóa, tìm kiếm bằng JDBC; tách controller và service.
- Buổi 6: hoàn thiện mini app, thử mã trùng, dữ liệu rỗng, mất kết nối và khởi động lại.
- Tự giải thích: tại sao bảng hiển thị tên mới nhưng CSDL vẫn giữ tên cũ? Thiết kế của em cập nhật thành công ở thời điểm nào?

Các nhiệm vụ này là phần biên soạn bổ sung theo lịch trình. Đọc yêu cầu, sản phẩm và ca kiểm tra ở [danh sách bài tập](bai-tap.html).

## Nguồn đọc theo chủ đề

Giáo trình OOP được cung cấp: lớp và đối tượng, đóng gói, ngoại lệ, collection. Đối chiếu [SwingWorker JDK 21](https://docs.oracle.com/en/java/javase/21/docs/api/java.desktop/javax/swing/SwingWorker.html), [JDBC Prepared Statements](https://docs.oracle.com/javase/tutorial/jdbc/basics/prepared.html) và [sách Java bản Swing của David J. Eck](https://math.hws.edu/javanotes-swing/). Tài liệu JDBC Tutorial dùng ví dụ JDK 8; các ví dụ của chuyên mục giữ nguyên tắc tham số hóa và đóng tài nguyên.
