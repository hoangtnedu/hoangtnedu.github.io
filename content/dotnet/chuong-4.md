## 4.1. Mô hình dữ liệu cho chuỗi bài tập

Ứng dụng sử dụng bốn thực thể: Khoa, SinhVien, HocPhan và DangKy. Một khoa có nhiều sinh viên; một sinh viên đăng ký nhiều học phần; một học phần có nhiều sinh viên. Bảng DangKy giải quyết quan hệ nhiều–nhiều và lưu thông tin học kỳ, điểm hoặc trạng thái đăng ký tùy thiết kế.

Trước khi viết C#, xác định khóa chính, khóa ngoại, miền giá trị và quy tắc xóa. Mã khoa không tồn tại phải bị từ chối; xóa khoa đang có sinh viên phải có chính sách rõ ràng. Dữ liệu mẫu dùng tên giả; không cần đưa dữ liệu cá nhân thật vào repository thực hành.

Dùng NVARCHAR cho văn bản tiếng Việt, DATE khi chỉ cần ngày và DECIMAL cho giá trị yêu cầu độ chính xác xác định. Giới hạn độ dài cột phải thống nhất với kiểm tra trên giao diện. Ràng buộc CHECK, UNIQUE và FOREIGN KEY bảo vệ dữ liệu ngay cả khi thao tác không đi qua ứng dụng.

## 4.2. Các thành phần ADO.NET

`SqlConnection` thiết lập phiên làm việc với SQL Server. `SqlCommand` mô tả câu lệnh hoặc stored procedure. `SqlParameter` truyền giá trị có kiểu. `SqlDataReader` duyệt kết quả theo từng dòng. `SqlDataAdapter` trao đổi dữ liệu giữa database và DataTable/DataSet. `SqlTransaction` gom các thao tác cần thành công cùng nhau.

Các lab dùng namespace **System.Data.SqlClient trong .NET Framework**. Nếu phát triển một project .NET hiện đại, cần xem xét provider Microsoft.Data.SqlClient cùng gói và cấu hình phù hợp; không trộn hai namespace trong cùng ví dụ rồi giả định mọi kiểu tương thích.

Mở kết nối gần lúc cần dùng và đóng ngay sau thao tác bằng `using`. Không giữ một kết nối static mở suốt vòng đời chương trình. Chuỗi kết nối thuộc cấu hình, không đặt mật khẩu trong mã nguồn. Khi dùng Windows Authentication, tài khoản chạy ứng dụng cần quyền trên SQL Server.

## 4.3. ExecuteScalar, ExecuteReader và ExecuteNonQuery

`ExecuteScalar` lấy cột đầu của dòng đầu, phù hợp COUNT hoặc một giá trị tổng hợp. `ExecuteReader` trả bộ đọc cho tập kết quả. `ExecuteNonQuery` chạy thao tác không lấy một bảng kết quả, thường là INSERT/UPDATE/DELETE; với SET NOCOUNT hoặc trigger, không được mặc định mọi giá trị trả về đều là số bản ghi nghiệp vụ mong đợi.

```csharp
using System;
using System.Data;
using System.Data.SqlClient;

public static class SinhVienQueries
{
    public static int DemTheoKhoa(string connectionString, string maKhoa)
    {
        const string sql = "SELECT COUNT(*) FROM SinhVien WHERE MaKhoa=@MaKhoa";
        using (var conn = new SqlConnection(connectionString))
        using (var cmd = new SqlCommand(sql, conn))
        {
            cmd.Parameters.Add("@MaKhoa", SqlDbType.VarChar, 10).Value = maKhoa;
            conn.Open();
            return Convert.ToInt32(cmd.ExecuteScalar());
        }
    }
}
```

Ví dụ giả định bảng và cột theo script Lab 7. Truyền chuỗi kết nối từ cấu hình. Không ghép dữ liệu người dùng trực tiếp vào CommandText. Tham số truyền **giá trị**, không thay tên cột hay hướng sắp xếp. Với lựa chọn ORDER BY, ánh xạ lựa chọn giao diện sang danh sách cột cho phép do chương trình định nghĩa.

## 4.4. Kiểu tham số, NULL và tìm kiếm

Chỉ định SqlDbType và độ dài phù hợp giúp ý định rõ ràng, tránh suy luận kiểu không mong muốn. SQL NULL khác null của C#: khi truyền giá trị SQL rỗng, dùng `DBNull.Value`. Khi đọc, kiểm tra IsDBNull trước GetString hoặc GetDateTime nếu cột cho phép rỗng.

Tìm kiếm LIKE phải truyền mẫu qua tham số. Nếu yêu cầu tìm đúng ký tự `%` hoặc `_`, bổ sung cách escape ký tự đại diện; tham số hóa bảo vệ cấu trúc truy vấn nhưng không tự thay ý nghĩa wildcard. Phân trang cần ORDER BY ổn định, thêm mã duy nhất khi trường sắp xếp có thể trùng.

DataReader cần kết nối đang mở trong lúc đọc. Đọc hết dữ liệu cần thiết và giải phóng reader trước khi dùng lại kết nối theo thiết kế thông thường. `DataTable.Load(reader)` thuận tiện cho binding nhưng đưa kết quả vào bộ nhớ; truy vấn chỉ những cột và dòng thực sự cần.

## 4.5. Dữ liệu ngắt kết nối

DataTable chứa cột, dòng và ràng buộc trong bộ nhớ. DataSet chứa nhiều DataTable cùng quan hệ DataRelation. DataView trình bày góc nhìn lọc hoặc sắp xếp mà không nhất thiết thay bảng gốc. Những đối tượng này phù hợp với giao diện bảng và nguồn báo cáo trong chuỗi lab.

`Fill` nạp dữ liệu vào bảng bộ nhớ. Sửa DataRow chưa tự sửa database: cần câu lệnh cập nhật và gọi Update hoặc phương thức DAL tương ứng. RowState theo dõi Added, Modified, Deleted; gọi AcceptChanges trước khi adapter cập nhật có thể làm mất trạng thái cần gửi. RejectChanges hoàn tác thay đổi trong bộ nhớ, không tự rollback giao dịch đã commit ở SQL Server.

SqlCommandBuilder hữu ích trong bài tập đơn giản với truy vấn phù hợp, nhưng không tự giải quyết mọi truy vấn JOIN hay nghiệp vụ nhiều bảng. Với sản phẩm có yêu cầu rõ, viết câu lệnh hoặc stored procedure tường minh để kiểm soát ràng buộc và số dòng chịu tác động.

## 4.6. CRUD và stored procedure

Tầng DAL nhận DTO hoặc tham số nghiệp vụ, tạo command, thêm tham số và thực thi. Tầng BUS kiểm tra quy tắc và quyết định có cho phép thao tác hay không. GUI chỉ cập nhật trạng thái thành công sau khi DAL hoàn tất. Không báo “Đã lưu” ngay khi người dùng bấm nút.

Stored procedure cần quy ước đầu vào, đầu ra và lỗi. Trùng mã, dữ liệu ngoài miền và bản ghi không tồn tại là các tình huống cần phân biệt. Không đưa mọi lỗi SQL lên giao diện dưới một thông báo “Sai mật khẩu” hay “Lỗi kết nối”. Stored procedure vẫn có thể bị SQL injection nếu bên trong ghép SQL động không an toàn.

## 4.7. Master-detail và transaction

Master-detail là cách hiển thị quan hệ: chọn khoa ở lưới trên, lưới dưới tải sinh viên của khoa đó. Sự kiện thay đổi lựa chọn phải xử lý trường hợp không có dòng, dòng mới và mã rỗng. Cập nhật detail dựa trên khóa, không dựa vào vị trí dòng đang nhìn thấy.

Transaction dùng khi nhiều thao tác phải thành công hoặc thất bại cùng nhau, ví dụ thêm sinh viên và đăng ký học phần ban đầu. Mở kết nối, BeginTransaction, gán transaction đó cho mọi command tham gia, rồi Commit khi tất cả thành công. Nếu một bước thất bại, Rollback khi còn có thể và giữ nguyên ngoại lệ gốc khi chuyển lỗi lên tầng trên.

Không mở transaction rồi chờ người dùng nhập trên Form. Giữ thời gian giao dịch ngắn để giảm giữ khóa. Thử cố ý làm bước thứ hai thất bại và kiểm tra trực tiếp cả hai bảng: không được còn bản ghi của bước đầu. Đây là kiểm thử ý nghĩa của tính nguyên tử, không chỉ kiểm tra xuất hiện thông báo lỗi.

## Thực hành và tự kiểm tra

Lộ trình: [Lab 7 — CSDL và kết nối](lab-07.html), [Lab 8 — Command và tham số](lab-08.html), [Lab 9 — Reader và tìm kiếm](lab-09.html), [Lab 10 — DataSet](lab-10.html), [Lab 11 — CRUD](lab-11.html), [Lab 12 — Transaction](lab-12.html).

- Tìm tên có dấu nháy đơn, danh sách rỗng và dữ liệu NULL.
- Thêm trùng mã, sửa mã không tồn tại và xóa bản ghi đang được tham chiếu.
- Mất kết nối ở giữa thao tác; kiểm tra không báo thành công giả.
- Làm lỗi giữa transaction và xác minh database đã trở về trạng thái trước đó.

## Đọc thêm

[Microsoft — Truy xuất và thay đổi dữ liệu](https://learn.microsoft.com/en-us/dotnet/framework/data/adonet/retrieving-and-modifying-data), [Command và parameter](https://learn.microsoft.com/en-us/dotnet/framework/data/adonet/commands-and-parameters), [DataAdapter và DataReader](https://learn.microsoft.com/en-us/dotnet/framework/data/adonet/dataadapters-and-datareaders), [Local transactions](https://learn.microsoft.com/en-us/dotnet/framework/data/adonet/local-transactions). Nội dung theo đề cương, bài giảng nâng cao và Lab 7–12.
