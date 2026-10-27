## 3.1. Lập trình giao diện theo sự kiện

Ứng dụng Console thường đi theo một chuỗi nhập, xử lý, xuất. Ứng dụng WinForms chờ người dùng tương tác: bấm nút, thay đổi lựa chọn, chọn dòng hoặc đóng cửa sổ. Hệ thống chuyển các tương tác thành sự kiện; chương trình đăng ký phương thức xử lý cho những sự kiện cần quan tâm.

Một Form là cửa sổ và là vùng chứa điều khiển. Label trình bày thông tin, TextBox nhận văn bản, Button khởi phát thao tác. CheckBox biểu diễn lựa chọn độc lập; RadioButton trong cùng nhóm biểu diễn lựa chọn loại trừ nhau. ComboBox phù hợp danh mục hữu hạn; DateTimePicker giúp nhập ngày theo kiểu dữ liệu thay vì tự phân tích chuỗi.

Trong Designer, đặt tên thể hiện vai trò như `txtHoTen`, `btnLuu`, `cboKhoa`. Thuộc tính Name phục vụ mã; Text là nội dung hiển thị. Đặt TabIndex để người dùng di chuyển bằng bàn phím, Anchor hoặc Dock để thích ứng kích thước cửa sổ. Tránh căn vị trí chỉ dựa trên màn hình đang thiết kế.

## 3.2. Luồng xử lý một nút Lưu

Khi bấm Lưu, giao diện đọc dữ liệu điều khiển, kiểm tra định dạng, tạo đối tượng truyền dữ liệu và gọi BUS. BUS kiểm tra nghiệp vụ rồi gọi DAL. Chỉ khi thao tác lưu thành công, giao diện mới tải lại danh sách và thông báo kết quả. Nếu lưu thất bại, giữ nội dung đang nhập để người dùng sửa.

```csharp
private void btnLuu_Click(object sender, System.EventArgs e)
{
    errorProvider1.Clear();
    if (string.IsNullOrWhiteSpace(txtHoTen.Text))
    {
        errorProvider1.SetError(txtHoTen, "Nhập họ tên.");
        txtHoTen.Focus();
        return;
    }
    decimal diem;
    if (!decimal.TryParse(txtDiem.Text, out diem)
        || diem < 0 || diem > 10)
    {
        errorProvider1.SetError(txtDiem, "Điểm phải từ 0 đến 10.");
        return;
    }
    // Tạo DTO và gọi BUS tại đây; chỉ báo thành công sau khi lưu xong.
}
```

Đoạn mã là bộ xử lý sự kiện trong Form đã có các điều khiển và ErrorProvider tương ứng. Chặn phím ở KeyPress chỉ cải thiện thao tác nhập; không thay thế kiểm tra cuối cùng vì người dùng có thể dán văn bản hoặc dữ liệu có thể đến từ nguồn khác.

## 3.3. Binding và các điều khiển danh sách

DataGridView hiển thị dữ liệu dạng bảng. Khi gán DataSource, có thể dùng BindingSource làm lớp trung gian. Tắt AutoGenerateColumns nếu muốn kiểm soát thứ tự và tiêu đề; mỗi cột cần DataPropertyName đúng với tên thuộc tính. Giữ mã định danh trong dữ liệu thay vì suy ra từ số thứ tự dòng.

`BindingList<T>` thông báo thêm, xóa phần tử cho giao diện. Để việc thay đổi thuộc tính một phần tử tự phản ánh lên UI, lớp phần tử có thể cần INotifyPropertyChanged. Nếu dùng List thông thường, phải chủ động nạp lại nguồn hoặc reset binding theo cách phù hợp. Việc đổi biến chứa danh sách chưa chắc tự cập nhật lưới.

ListView thuận tiện cho nhiều cách hiển thị như danh sách chi tiết và biểu tượng. TreeView thể hiện quan hệ cha–con, ví dụ Khoa → Sinh viên. Dữ liệu thật nên gắn vào Tag bằng mã hoặc đối tượng xác định rõ; tránh dựa vào chuỗi nhãn để thực hiện thao tác cập nhật cơ sở dữ liệu.

## 3.4. Ứng dụng nhiều Form

Một Form chính có thể mở Form danh sách, Form nhập và Form báo cáo. `Show` mở cửa sổ không khóa luồng tương tác với Form gọi; `ShowDialog` hiển thị hộp thoại modal. Với form sửa dữ liệu, truyền đầu vào qua constructor hoặc thuộc tính rõ ràng và trả trạng thái qua DialogResult.

```csharp
// Trong Form gọi; FrmSuaSinhVien là lớp do người học xây dựng.
using (var form = new FrmSuaSinhVien(maSinhVien))
{
    if (form.ShowDialog(this) == System.Windows.Forms.DialogResult.OK)
        TaiLaiDanhSach();
}
```

Không mở vô hạn các bản sao cùng một Form mà không có chủ đích. Khi người dùng đóng cửa sổ có dữ liệu chưa lưu, hỏi xác nhận hoặc cho phép quay lại chỉnh sửa. Dùng `using` cho dialog tạo mới khi vòng đời đã rõ. Không dùng biến static làm kho chia sẻ mọi trạng thái giữa các Form.

## 3.5. Kiến trúc GUI – BUS – DAL – DTO

**GUI** nhận tương tác và hiển thị kết quả. **BUS** thực thi quy tắc nghiệp vụ. **DAL** truy xuất nơi lưu trữ. **DTO** mang dữ liệu giữa các phần. Luồng gọi thông thường là GUI gọi BUS, BUS gọi DAL; DTO được dùng chung. DAL không cần biết TextBox hay MessageBox.

Mô hình ba lớp là cách tổ chức trách nhiệm trong mã; không có nghĩa ứng dụng bắt buộc chạy trên ba máy chủ. Trong Lab 6, DAL có thể chỉ là danh sách trong bộ nhớ. Sau đó thay bằng SQL Server nhưng vẫn giữ các phương thức nghiệp vụ và luồng giao diện.

Ví dụ khi thêm sinh viên: GUI chuyển chuỗi điểm thành số; BUS kiểm tra mã trùng và điều kiện nghiệp vụ; DAL thực hiện INSERT bằng tham số. Cơ sở dữ liệu vẫn cần khóa chính và ràng buộc vì nhiều máy có thể cùng ghi dữ liệu. Kiểm tra ở BUS không thay thế tính toàn vẹn ở database.

## 3.6. Giao diện có thể sử dụng được

Mỗi thao tác cần trạng thái rõ ràng: đang thêm, đang sửa, đang tải hay đã hoàn thành. Vô hiệu hóa nút không phù hợp và ngăn bấm lặp khi đang lưu. Thông báo nên cho biết người dùng cần sửa gì, không đưa toàn bộ stack trace lên màn hình.

Thử cửa sổ nhỏ, màn hình có tỉ lệ hiển thị khác, tên tiếng Việt dài và danh sách không có dữ liệu. Kiểm tra thứ tự Tab, nhãn cạnh ô nhập, khả năng thao tác bằng bàn phím và thông báo lỗi. Những trường hợp này phát hiện vấn đề mà ảnh chụp giao diện với dữ liệu đẹp thường che khuất.

## Thực hành và tự kiểm tra

Lần lượt làm [Lab 4 — Điều khiển cơ bản](lab-04.html), [Lab 5 — Sự kiện, validation, đa Form](lab-05.html) và [Lab 6 — Danh sách và ba lớp](lab-06.html).

- Theo dõi một lần bấm Lưu từ GUI đến DAL và đường kết quả quay lại.
- Hủy form sửa có làm thay đổi dữ liệu ban đầu không?
- Khi lưới rỗng, sự kiện SelectionChanged có hoạt động an toàn không?
- Kiểm tra thêm, sửa, xóa trong bộ nhớ trước khi kết nối database.

## Đọc thêm

[Microsoft — Tổng quan Windows Forms](https://learn.microsoft.com/en-us/dotnet/desktop/winforms/overview/) và [Form API](https://learn.microsoft.com/en-us/dotnet/api/system.windows.forms.form). Các chủ đề điều khiển nâng cao, nhiều Form và ba lớp được triển khai từ khung đề cương và bài giảng .NET nâng cao.
