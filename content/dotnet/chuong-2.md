## 2.1. Từ dữ liệu rời rạc đến đối tượng

Ở chương trước, mã sinh viên, họ tên và điểm có thể nằm trong các biến riêng. Khi quản lý nhiều sinh viên, cách này dễ làm lẫn dữ liệu. Lớp `SinhVien` mô tả dữ liệu và hành vi của một sinh viên; mỗi đối tượng là một trường hợp cụ thể của lớp. Danh sách đối tượng giúp thao tác theo bản ghi thay vì đồng bộ nhiều mảng song song.

Trường dữ liệu lưu trạng thái bên trong. Thuộc tính kiểm soát cách đọc, ghi trạng thái. Phương thức thực hiện hành vi. Constructor tạo trạng thái khởi đầu. Phạm vi `private` ngăn mã ngoài lớp truy cập trực tiếp; `public` công bố giao diện sử dụng. Mục đích của đóng gói là giữ các quy tắc luôn đúng, chẳng hạn họ tên không rỗng và điểm nằm trong miền cho phép.

```csharp
public class SinhVien
{
    public string MaSV { get; private set; }
    public string HoTen { get; private set; }
    public decimal DiemTB { get; private set; }

    public SinhVien(string ma, string ten, decimal diem)
    {
        if (string.IsNullOrWhiteSpace(ma))
            throw new System.ArgumentException("Thiếu mã sinh viên.");
        if (string.IsNullOrWhiteSpace(ten))
            throw new System.ArgumentException("Thiếu họ tên.");
        if (diem < 0 || diem > 10)
            throw new System.ArgumentOutOfRangeException("diem");
        MaSV = ma.Trim();
        HoTen = ten.Trim();
        DiemTB = diem;
    }

    public bool DuDieuKienDat() { return DiemTB >= 5; }
}
```

Ví dụ này dùng private setter để ngăn thay đổi tùy tiện sau khởi tạo. Nếu cần sửa điểm, bổ sung phương thức kiểm tra quy tắc trước khi cập nhật. Trong các lab, một số lớp dùng public setter để minh họa từng bước; khi ghép thành sản phẩm, cần chọn một cách bảo vệ dữ liệu nhất quán.

## 2.2. Constructor và trách nhiệm của lớp

Constructor có cùng tên lớp và không có kiểu trả về. Nạp chồng constructor cho phép tạo đối tượng qua các bộ tham số khác nhau. Dùng `this(...)` để gọi constructor khác trong cùng lớp, tránh lặp quy tắc kiểm tra. Một đối tượng chưa đủ dữ liệu không nên được xem là bản ghi đã sẵn sàng lưu.

Tách trách nhiệm giúp mã dễ dùng lại. `SinhVien` không cần biết TextBox nào đang nhập tên; lớp truy cập dữ liệu không cần gọi MessageBox. Khi lớp nghiệp vụ phụ thuộc trực tiếp vào giao diện, việc tái sử dụng cho Console, kiểm thử hoặc web sẽ khó hơn. Tên phương thức nên thể hiện mục đích như `TinhHocPhi`, `KiemTraHopLe`, thay vì tên gắn với nút bấm.

Đối tượng tham chiếu có thể được nhiều biến cùng trỏ tới. Sửa đối tượng qua một biến có thể thấy ở biến khác. Khi mở form chỉnh sửa, nếu muốn cho phép Hủy, cần làm việc trên bản sao dữ liệu hoặc chỉ ghi ngược sau khi người dùng xác nhận. Sao chép biến tham chiếu không tự tạo một bản sao độc lập của đối tượng.

## 2.3. Kế thừa, đa hình và lớp trừu tượng

Kế thừa mô tả quan hệ có thể thay thế: SinhVien là một loại Nguoi. Dữ liệu dùng chung đặt ở lớp cơ sở; phần riêng đặt ở lớp dẫn xuất. Từ khóa `base` gọi constructor hoặc thành viên lớp cha. Không chọn kế thừa chỉ để lấy vài dòng mã; khi quan hệ là “có một”, kết hợp đối tượng thường rõ hơn.

Phương thức `virtual` cho phép lớp con `override` cách thực hiện. Khi gọi qua tham chiếu lớp cha, hành vi thực tế phụ thuộc đối tượng lúc chạy. Đó là đa hình. Nạp chồng khác ghi đè: nạp chồng phân biệt danh sách tham số, trong khi ghi đè thay hành vi của một thành viên kế thừa phù hợp.

Lớp trừu tượng có thể chứa dữ liệu và hành vi chung nhưng không tạo trực tiếp một đối tượng của chính lớp đó. Ví dụ NguoiHoc định nghĩa `TinhHocPhi`; SinhVienChinhQuy và SinhVienLienThong hiện thực công thức. Tổng học phí có thể tính trên danh sách NguoiHoc mà không viết chuỗi if phân loại tại mọi nơi.

## 2.4. Interface và thiết kế theo hợp đồng

Interface mô tả hợp đồng mà bên sử dụng cần. Trong phạm vi bài tập, `IValidatable` khai báo kiểm tra dữ liệu, còn `IPrintable` khai báo nội dung hiển thị. Một lớp có thể thực hiện nhiều interface. Không cần xây một cây kế thừa lớn chỉ vì hai kiểu cùng có khả năng in thông tin.

Với ứng dụng quản lý, định nghĩa `ISinhVienRepository` gồm các thao tác lấy, thêm, sửa, xóa. Tầng BUS sử dụng hợp đồng này; một phiên bản repository dùng bộ nhớ cho Lab 6, phiên bản khác dùng SQL Server từ Lab 11. Việc thay cơ chế lưu trữ không nên buộc viết lại toàn bộ quy tắc nghiệp vụ.

```csharp
public interface ISinhVienRepository
{
    System.Collections.Generic.IList<SinhVien> LayTatCa();
    void Them(SinhVien sinhVien);
}
```

Đây là hợp đồng minh họa; còn cần lớp thực thi trước khi sử dụng. Khi kiểm thử BUS, có thể truyền một repository giả lập để kiểm tra trùng mã mà chưa phải mở kết nối SQL Server.

## 2.5. Collection, generics và LINQ

`List<SinhVien>` là tập hợp có kiểu: trình biên dịch kiểm tra phần tử và người đọc biết dữ liệu đang xử lý. `Dictionary<string, SinhVien>` phù hợp khi cần tra cứu theo khóa duy nhất. Không chọn cấu trúc chỉ theo thói quen; xét thao tác chính, quy mô và ràng buộc dữ liệu.

LINQ mô tả lọc, sắp xếp, chiếu và tổng hợp. Với danh sách trong bộ nhớ, `Where` chọn phần tử thỏa điều kiện, `OrderBy` sắp xếp và `Select` tạo kết quả mới. Nhiều truy vấn thực thi trì hoãn: khai báo truy vấn chưa chắc đã duyệt dữ liệu. `ToList` tạo một danh sách kết quả tại thời điểm gọi.

```csharp
// Bổ sung using System.Linq;
var ketQua = danhSach
    .Where(sv => sv.DiemTB >= 8)
    .OrderBy(sv => sv.HoTen)
    .Select(sv => new { sv.MaSV, sv.HoTen })
    .ToList();
```

Đoạn mã giả định `danhSach` là collection SinhVien đã được khởi tạo. LINQ trên đối tượng và LINQ qua EF Core không hoàn toàn giống nhau: EF cần dịch biểu thức sang truy vấn dữ liệu, nên không phải mọi hàm C# đều dịch được.

## Thực hành và tự kiểm tra

Làm [Lab 2 — Lớp và đóng gói](lab-02.html), sau đó [Lab 3 — Kế thừa và interface](lab-03.html). Các đoạn minh họa cùng tên lớp trong lab là những bước phát triển hoặc ví dụ riêng; phải hợp nhất chúng trước khi build một project.

- Mô tả một quy tắc phải được bảo vệ trong lớp SinhVien.
- So sánh nạp chồng, ghi đè và che khuất phương thức.
- Giải thích vì sao DAL không cần tham chiếu WinForms.
- Thử lọc danh sách rỗng, mã không tồn tại và mã chỉ khác chữ hoa/chữ thường.

## Đọc thêm

[Microsoft — Kỹ thuật hướng đối tượng trong C#](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/object-oriented/), [LINQ](https://learn.microsoft.com/en-us/dotnet/csharp/linq/) và [Yellow Book](https://www.robmiles.com/c-yellow-book). Khung kiến thức dựa trên chương hướng đối tượng của đề cương và bài giảng cơ bản.
