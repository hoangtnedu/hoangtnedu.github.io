## 1.1. .NET và .NET Framework

Một chương trình C# cần nhiều thành phần ngoài tệp mã nguồn: trình biên dịch, môi trường thực thi, thư viện và công cụ gỡ lỗi. .NET cung cấp nền tảng để các thành phần này làm việc cùng nhau. C# là ngôn ngữ; Visual Studio là môi trường phát triển; WinForms là thư viện xây dựng giao diện. Phân biệt bốn khái niệm giúp chọn đúng công cụ và đọc thông báo lỗi chính xác.

Trong lộ trình này, ứng dụng quản lý dùng **Windows Forms App (.NET Framework), target .NET Framework 4.8**, để nhất quán với các lab ADO.NET và Crystal Reports. Cần Windows, Visual Studio với workload .NET desktop development và targeting pack tương ứng. Console có thể thực hiện trên .NET hiện đại, nhưng không chuyển tùy tiện project giao diện hoặc thư viện Crystal Reports sang target khác.

.NET Framework và .NET hiện đại có hệ sinh thái liên quan nhưng không thay thế trực tiếp mọi thư viện của nhau. Khi mở solution, kiểm tra Target framework trong thuộc tính project, nền tảng x86/x64 và các assembly đang tham chiếu. Chọn runtime theo yêu cầu ứng dụng, không theo số phiên bản lớn nhất.

## 1.2. CLR, thư viện và quá trình chạy

Với cách biên dịch thông thường của .NET Framework, mã C# được biên dịch thành mã trung gian IL và metadata trong assembly. Khi chương trình chạy, CLR quản lý việc thực thi; JIT chuyển phần mã cần chạy sang mã máy. Thư viện nền tảng cung cấp các kiểu như String, DateTime, List và các lớp truy cập tệp. Garbage collector thu hồi đối tượng không còn được tham chiếu, nhưng không thay thế việc đóng kết nối hay giải phóng tài nguyên qua IDisposable.

Một solution chứa một hoặc nhiều project. Project quy định loại đầu ra, framework đích, mã nguồn và phụ thuộc. Assembly có thể là tệp thực thi hoặc thư viện. Namespace tổ chức tên kiểu trong mã; namespace không bắt buộc trùng tên assembly. NuGet quản lý gói phụ thuộc; khi thêm gói phải kiểm tra target framework và phiên bản tương thích.

**Thực hiện:** tạo solution `DotNetLabs`, thêm Console project `Lab01_Console`, chạy Build rồi Start Debugging. Đặt breakpoint trước câu lệnh nhập, dùng Step Over để theo dõi từng câu lệnh, mở Locals để quan sát giá trị. Thử gây một lỗi biên dịch và một lỗi lúc chạy để phân biệt chúng.

## 1.3. Biến, kiểu dữ liệu và nhập xuất

Biến có tên, kiểu và giá trị. Dùng `int` cho số nguyên trong phạm vi phù hợp, `double` cho tính toán gần đúng, `decimal` cho tiền, `bool` cho điều kiện và `string` cho văn bản. Không dùng chuỗi làm kiểu lưu điểm chỉ vì dữ liệu ban đầu đến từ TextBox. Việc chuyển đổi và kiểm tra phải thực hiện trước khi tính toán.

```csharp
using System;

class Program
{
    static void Main()
    {
        Console.OutputEncoding = System.Text.Encoding.UTF8;
        Console.Write("Nhập điểm [0, 10]: ");
        decimal diem;
        if (!decimal.TryParse(Console.ReadLine(), out diem)
            || diem < 0 || diem > 10)
        {
            Console.WriteLine("Điểm không hợp lệ.");
            return;
        }
        Console.WriteLine(diem >= 5 ? "Đạt" : "Chưa đạt");
    }
}
```

`TryParse` trả về kết quả thành công hay thất bại và đưa giá trị qua tham số `out`. Cách này phù hợp với dữ liệu nhập sai có thể dự đoán được. Dấu thập phân phụ thuộc thiết lập văn hóa của máy; README cần ghi rõ dữ liệu thử và quy ước nhập. Kiểm tra được định dạng chưa có nghĩa dữ liệu hợp lệ: điểm 12 vẫn là số nhưng nằm ngoài miền nghiệp vụ.

## 1.4. Rẽ nhánh, vòng lặp và phương thức

Dùng `if/else` khi lựa chọn phụ thuộc các điều kiện; dùng `switch` khi phân nhánh theo các giá trị rõ ràng. Với xếp loại điểm, kiểm tra miền trước, sau đó xét ngưỡng từ cao xuống thấp. Viết thứ tự ngược có thể khiến mọi điểm trên 5 rơi vào cùng một nhánh.

`for` phù hợp khi biết số lần lặp; `while` lặp khi điều kiện còn đúng; `do/while` thực hiện thân ít nhất một lần. Mỗi vòng lặp cần xác định trạng thái ban đầu, điều kiện dừng và bước cập nhật. Menu nhập sai phải yêu cầu nhập lại thay vì vô tình thoát hoặc lặp vô hạn.

Phương thức tách một nhiệm vụ có tên, đầu vào và đầu ra. Ví dụ `XepLoai(decimal diem)` trả về tên mức; Main chỉ điều phối nhập và hiển thị. Khi kiểm thử, có thể gọi trực tiếp phương thức với các giá trị 0; 4,99; 5; 6,5; 8; 9; 10, rồi kiểm tra dữ liệu ngoài miền. Ngưỡng xếp loại ở đây là quy ước của bài tập, không phải quy chế đào tạo.

## 1.5. Ngoại lệ và gỡ lỗi

Ngoại lệ thể hiện luồng thực hiện không thể tiếp tục bình thường. `try` bao quanh thao tác có thể thất bại; `catch` xử lý loại lỗi phù hợp; `finally` thực hiện công việc kết thúc khi điều khiển rời khối theo cơ chế thông thường. Không đặt toàn bộ chương trình trong một catch rỗng vì sẽ mất dấu nguyên nhân.

Với phép chia số nguyên, số chia bằng 0 gây DivideByZeroException. Phép chia số thực có thể cho Infinity hoặc NaN; không suy rộng cách xử lý giữa các kiểu. Khi sửa lỗi, đọc loại ngoại lệ, thông điệp và call stack, sau đó tái hiện bằng dữ liệu nhỏ. Ghi lại đầu vào, kết quả mong đợi và kết quả thực tế để lần chạy sau có thể kiểm tra lại.

## Thực hành và tự kiểm tra

Làm [Lab 1 — Console và gỡ lỗi](lab-01.html). Hoàn thành các bài nhập thông tin, hình trụ, xếp loại, menu và ngoại lệ trước khi chuyển sang phương trình bậc hai, thống kê và số nguyên tố.

- Giải thích vai trò khác nhau của CLR, C#, Visual Studio và WinForms.
- Tại sao nhập được một số vẫn cần kiểm tra miền giá trị?
- Với menu, trường hợp nhập chữ phải đi theo nhánh nào?
- Nộp mã nguồn, README và kết quả kiểm thử cả trường hợp đúng lẫn sai.

## Đọc thêm

[Microsoft — Giới thiệu .NET](https://learn.microsoft.com/en-us/training/modules/dotnet-introduction/), [C# documentation](https://learn.microsoft.com/en-us/dotnet/csharp/) và [C# Programming Yellow Book — Rob Miles](https://www.robmiles.com/c-yellow-book). Nội dung chương được biên soạn lại theo khung học phần và bài giảng .NET cơ bản; không sao chép trang sách.
