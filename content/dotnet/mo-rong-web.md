## Từ desktop đến ứng dụng web

Phần này nối lộ trình WinForms với khung Công nghệ .NET: nền tảng C#, OOP, LINQ và bất đồng bộ → ASP.NET Core → EF Core → kiểm thử, bảo mật → AI và Azure. Các bài dưới đây là bài mở rộng riêng, không đổi tên hay thay thế nội dung của 16 lab desktop.

Ứng dụng web tiếp nhận HTTP request, xử lý ở server rồi trả response. Trình duyệt không truy cập trực tiếp database. Mã C# phía server cũng không chạy trên GitHub Pages: website này là nơi đọc học liệu; ứng dụng thực hành chạy ở localhost hoặc dịch vụ hỗ trợ .NET.

## M1. HTTP, routing và dependency injection

Cài SDK .NET theo hướng dẫn chính thức, dùng một phiên bản còn hỗ trợ và ghi phiên bản trong README. Ví dụ dưới đây dùng .NET 10 cho project mở rộng; không đưa nó vào solution .NET Framework của Crystal Reports.

```bash
dotnet new web -n DotNetWeb -f net10.0
cd DotNetWeb
dotnet run
```

Thay nội dung Program.cs bằng ví dụ tối giản sau. Đọc địa chỉ cổng được in trong terminal để mở đúng ứng dụng.

```csharp
var builder = WebApplication.CreateBuilder(args);
var app = builder.Build();
app.MapGet("/api/sinh-vien/{ma}", (string ma) =>
    Results.Ok(new { MaSV = ma, HoTen = "Dữ liệu minh họa" }));
app.Run();
```

Theo dõi request tới route, giá trị ma được binding và JSON được trả về. Dữ liệu ở ví dụ này là giả lập, chưa truy vấn database. Tiếp theo tách dịch vụ ISinhVienService, đăng ký vào DI và gọi từ endpoint. Giải thích vòng đời scoped, transient, singleton trước khi chọn cách đăng ký.

**Bài mở rộng M1:** xây GET danh sách, GET theo mã và trường hợp mã không tồn tại. Nộp bản ghi request/response có mã trạng thái phù hợp. Thử ký tự tiếng Việt và đầu vào rỗng.

## M2. MVC, Razor và validation

Tạo project MVC, xác định Model, Controller và View. Model binding chuyển dữ liệu request sang đối tượng đầu vào; validation kiểm tra theo quy tắc khai báo và nghiệp vụ. Khi dữ liệu sai, trả lại form cùng thông báo, không bỏ toàn bộ dữ liệu người dùng đã nhập.

Controller điều phối, dịch vụ thực hiện nghiệp vụ. Dùng view model chỉ chứa những trường được phép sửa để tránh gán quá nhiều thuộc tính ngoài ý muốn. Với form ghi dữ liệu, áp dụng bảo vệ chống giả mạo yêu cầu theo cơ chế framework.

**Bài mở rộng M2:** chuyển form sinh viên từ Lab 5 sang Razor, thực hiện danh sách và thêm dữ liệu trong bộ nhớ. Kiểm tra họ tên rỗng, điểm ngoài miền và thông báo tại đúng ô nhập. Ghi rõ dữ liệu chưa được lưu bền vững.

## M3. EF Core, migration và truy vấn

EF Core ánh xạ đối tượng với dữ liệu quan hệ. DbContext đại diện một phiên làm việc; DbSet mô tả tập thực thể. Khai báo khóa và quan hệ rõ ràng, thêm provider đúng database, tạo migration rồi kiểm tra mã và SQL trước khi áp dụng. Phiên bản provider phải tương thích với EF Core và target framework.

LINQ trên DbSet thường được dịch sang truy vấn database. Lọc và phân trang trước khi vật chất hóa dữ liệu; tránh gọi ToList quá sớm rồi lọc toàn bộ trên bộ nhớ. Xem xét tracking theo mục tiêu cập nhật. DbContext không được chia sẻ tùy tiện giữa các thao tác chạy song song.

**Bài mở rộng M3:** cài CRUD Khoa–SinhVien, tìm kiếm, phân trang và migration. Nộp sơ đồ dữ liệu, script hoặc migration cùng cách tạo database mới. So sánh một thao tác với cách dùng SqlCommand ở Lab 11.

## M4. Kiểm thử và bảo mật web

Xác thực trả lời người dùng là ai; phân quyền quyết định họ được làm gì. Ẩn nút Xóa trong giao diện không ngăn một request trực tiếp tới server. Mọi endpoint cần kiểm tra quyền theo thiết kế, đặc biệt thao tác cập nhật và truy cập bản ghi của người khác.

Tách cấu hình môi trường phát triển và triển khai. Secret của server nằm trong cơ chế quản lý bí mật hoặc cấu hình bảo mật của môi trường chạy. Không đặt API key trong JavaScript, HTML hay GitHub Pages. Kiểm thử cả người chưa đăng nhập, tài khoản thiếu quyền và tài khoản đủ quyền.

**Bài mở rộng M4:** tạo ca kiểm thử cho validation và quyền đọc/ghi. Nộp kết quả mong đợi, thực tế và một lỗi đã phát hiện rồi sửa. Có thể dùng repository giả lập cho unit test, đồng thời có kiểm thử tích hợp với database thử nghiệm.

## M5. AI có kiểm chứng

Chọn một chức năng nhỏ như gợi ý mô tả học phần từ nội dung giả lập. Gọi dịch vụ từ server; cấu hình timeout, hủy, giới hạn đầu vào và xử lý lỗi. Không mặc định một câu trả lời có cấu trúc đúng là thông tin chính xác. Đầu ra AI cần được kiểm tra trước khi hiển thị hoặc ghi dữ liệu.

Bài tập có thể chạy với dịch vụ giả lập để kiểm tra luồng khi chưa có tài khoản API. Với dịch vụ thật, ghi rõ nhà cung cấp, phiên bản SDK, cấu hình và giới hạn chi phí đã đặt. Không đưa dữ liệu cá nhân thật vào yêu cầu minh họa.

**Bài mở rộng M5:** lưu nhật ký gồm yêu cầu đầu vào, kết quả đã lược dữ liệu nhạy cảm, cách kiểm chứng và xử lý lỗi mô phỏng. API thất bại không được làm mất chức năng CRUD cốt lõi.

## M6. Triển khai trên Azure và bàn giao

Chạy và kiểm thử cục bộ trước. Chọn môi trường Azure App Service phù hợp runtime, cấu hình kết nối database ở môi trường triển khai, kiểm tra HTTPS và log. Sau publish, kiểm tra URL, CRUD, quyền truy cập và kết nối dữ liệu bằng tài khoản thử.

Tài nguyên cloud có thể phát sinh phí. Bài tập không yêu cầu tạo tài nguyên nếu chưa có môi trường được cấp; có thể nộp gói publish và hướng dẫn triển khai. Với tài nguyên dùng thật, ghi chủ sở hữu và cách dừng hoặc xóa sau thực hành.

**Bài mở rộng M6:** bàn giao README, cấu hình mẫu không có secret, migration, ca kiểm thử và bằng chứng chạy. Ghi điểm khác nhau giữa publish ứng dụng web và đóng gói WinForms ở Lab 15.

## Tài liệu chính thức

- [Cài .NET SDK](https://learn.microsoft.com/en-us/dotnet/core/install/).
- [ASP.NET Core — Tổng quan](https://learn.microsoft.com/en-us/aspnet/core/overview?view=aspnetcore-10.0).
- [Architect Modern Web Applications with ASP.NET Core and Azure](https://learn.microsoft.com/en-us/dotnet/architecture/modern-web-apps-azure/).
- [Entity Framework Core](https://learn.microsoft.com/en-us/ef/core/).
- [Phát triển tính năng AI trong .NET](https://learn.microsoft.com/en-us/dotnet/ai/overview).
- [Triển khai ứng dụng .NET trên App Service](https://learn.microsoft.com/en-us/azure/app-service/quickstart-dotnetcore).
