## 6.1. Xử lý lỗi theo tầng

Một ứng dụng quản lý cần phân biệt lỗi nhập liệu, lỗi nghiệp vụ, lỗi truy cập dữ liệu và lỗi lập trình. Điểm ngoài miền có thể được báo ngay tại giao diện; mã trùng được BUS hoặc database phát hiện; mất kết nối cần thông báo có thể thử lại; lỗi tham chiếu null cần được sửa trong mã. Gom mọi lỗi vào cùng một thông báo khiến việc khắc phục khó hơn.

DAL không nên tự mở MessageBox. DAL trả kết quả hoặc chuyển ngoại lệ phù hợp lên BUS; GUI quyết định cách trình bày. Nếu bắt ngoại lệ chỉ để ghi log rồi ném tiếp, dùng `throw;` để giữ stack trace. Không dùng `throw ex;` khi muốn giữ nguyên vị trí phát sinh ban đầu.

Chỉ bắt lỗi tại nơi có khả năng xử lý hoặc bổ sung ngữ cảnh có ích. Không để catch rỗng và không coi một ngoại lệ không được xử lý là một lần lưu thành công. Global exception handler chỉ là lớp xử lý cuối cùng; không bảo đảm mọi lỗi nghiêm trọng đều có thể phục hồi an toàn.

## 6.2. Logging và cấu hình

Log giúp tái hiện vấn đề sau khi ứng dụng rời máy lập trình. Mỗi bản ghi nên có thời gian, mức độ, thao tác và mã tương quan nếu có. Ghi loại ngoại lệ và stack trace vào nơi quản trị được phép truy cập; thông báo cho người dùng nên ngắn và có hướng xử lý.

Không ghi mật khẩu, khóa API hay toàn bộ chuỗi kết nối vào log. Với dữ liệu cá nhân, chỉ ghi lượng thông tin cần thiết. Tệp log phải có giới hạn dung lượng hoặc cơ chế xoay vòng, nơi lưu có quyền ghi, và chính sách dọn dẹp. Thử tình huống thư mục không tồn tại hoặc hết quyền ghi để log không tạo thêm một vòng lỗi khó hiểu.

Trong .NET Framework, App.config có thể lưu thiết lập ứng dụng và connectionStrings. Đọc qua ConfigurationManager và kiểm tra thiếu giá trị trước khi mở kết nối. Không hard-code đường dẫn riêng của máy giảng viên. Mật khẩu không trở nên an toàn chỉ vì chuyển từ mã C# sang một tệp cấu hình được commit công khai.

## 6.3. Chỉ thị biên dịch và mã an toàn

`#if DEBUG` cho phép đưa đoạn mã vào một cấu hình biên dịch nhất định. Nó khác câu lệnh if lúc chạy: mã có thể không nằm trong assembly đầu ra nếu biểu tượng không được định nghĩa. Có thể dùng cho chẩn đoán, nhưng không dùng như cơ chế phân quyền hoặc bảo vệ mật khẩu.

```csharp
#if DEBUG
System.Diagnostics.Debug.WriteLine("Bắt đầu tải danh sách sinh viên.");
#endif
```

“Code an toàn” trong ứng dụng quản lý gồm kiểm tra dữ liệu, quản lý tài nguyên, truy vấn tham số hóa, quyền truy cập và bảo vệ cấu hình. Từ khóa `unsafe` của C# liên quan thao tác con trỏ; không đồng nghĩa với việc một chương trình có hay không có bảo mật. Phạm vi bài tập không cần bật unsafe để xây CRUD.

## 6.4. Bất đồng bộ và giao diện phản hồi

Tải dữ liệu hoặc gọi dịch vụ có thể mất thời gian. Nếu chặn UI thread, cửa sổ không vẽ lại và người dùng tưởng chương trình bị treo. `async/await` giúp biểu diễn thao tác chờ mà không cần chặn luồng gọi khi API hỗ trợ bất đồng bộ. Nó không tự biến mọi phép tính thành chạy song song.

Với trình xử lý sự kiện WinForms, `async void` được sử dụng theo chữ ký sự kiện; phương thức nghiệp vụ bất đồng bộ thông thường nên trả `Task` hoặc `Task<T>`. Tránh `.Result` và `.Wait()` trên UI thread. Không sửa điều khiển từ một luồng nền tùy ý; việc cập nhật UI cần theo cơ chế điều phối phù hợp.

Khi tải, vô hiệu hóa nút khởi phát và khôi phục ở finally; xử lý lỗi và hủy theo mục tiêu ứng dụng. Không bọc mọi truy vấn SQL bằng Task.Run rồi cho rằng đã giải quyết toàn bộ thiết kế. Ưu tiên API bất đồng bộ phù hợp với phiên bản đang dùng và luôn kiểm tra vòng đời Form.

## 6.5. Kiểm thử từ chức năng đến bàn giao

Một ca kiểm thử cần đầu vào, bước thực hiện, kết quả mong đợi và kết quả thực tế. Với CRUD, thử luồng đúng, dữ liệu rỗng, trùng khóa, độ dài tối đa và dữ liệu ngoài miền. Với transaction, làm lỗi giữa chừng và kiểm tra database. Với báo cáo, so sánh số liệu tính tay và mở lại file xuất.

Tách quy tắc nghiệp vụ khỏi Form để kiểm thử mà không phải bấm giao diện. Kiểm thử tự động phù hợp với hàm tính toán và validation ổn định; kiểm thử tích hợp xác minh tương tác database. Kiểm thử trên máy khác phát hiện phụ thuộc mà máy lập trình đã có sẵn nên khó nhận thấy.

Không đưa database cá nhân hoặc khóa thật vào repository kiểm thử. Dùng script khởi tạo và dữ liệu giả lập có thể tái tạo. Nếu bài tập có tích hợp AI hỗ trợ lập trình, lưu mô tả phần được hỗ trợ và các kiểm chứng đã thực hiện; người nộp vẫn phải giải thích được mã.

## 6.6. Đóng gói và triển khai ứng dụng desktop

Build thành công không có nghĩa ứng dụng chạy được trên máy đích. Cần kiểm tra .NET Framework, kiến trúc target, SQL Server, cấu hình, runtime Crystal Reports, quyền ghi và đường dẫn tài nguyên. Chọn cơ chế publish hoặc bộ cài phù hợp công cụ đang dùng và yêu cầu bàn giao.

Quy trình bàn giao gồm tạo bản Release, đóng gói các phụ thuộc được phép phân phối, chuẩn bị script database, tài liệu cấu hình và hướng dẫn chạy. Không đưa thư mục bin/obj, mật khẩu hay tệp cache vào mã nguồn. Với SQL Server, xác định ai khởi tạo database và tài khoản nào có quyền cần thiết.

Dùng máy ảo hoặc máy sạch để cài. Chạy lại thêm–sửa–xóa, tìm kiếm, transaction, báo cáo và ghi log. Thử đường dẫn có khoảng trắng, tài khoản không có quyền quản trị và cấu hình kết nối sai. Ghi các bước gỡ cài đặt hoặc khôi phục phiên bản trước nếu cần.

## Thực hành tổng hợp

Làm [Lab 15 — Lỗi, log, cấu hình và đóng gói](lab-15.html), sau đó [Lab 16 — Ứng dụng hoàn chỉnh](lab-16.html). Bài tổng hợp giữ hình thức sản phẩm nhóm nếu tổ chức theo lớp; mỗi thành viên cần có phần việc và minh chứng riêng.

- Trình diễn một luồng nghiệp vụ xuyên GUI → BUS → DAL → database.
- Chứng minh rollback bằng dữ liệu trước và sau lỗi.
- Xuất một báo cáo có bộ lọc và đối chiếu số liệu.
- Bàn giao mã nguồn, script, README, ca kiểm thử và gói chạy trên máy khác.

## Đọc thêm

[Microsoft — Ngoại lệ](https://learn.microsoft.com/en-us/dotnet/csharp/fundamentals/exceptions/), [async/await](https://learn.microsoft.com/en-us/dotnet/csharp/asynchronous-programming/) và [cấu hình ứng dụng .NET Framework](https://learn.microsoft.com/en-us/dotnet/framework/configure-apps/). Phần mở rộng web, EF Core, AI và Azure nằm ở [Lộ trình .NET hiện đại](mo-rong-web.html).
