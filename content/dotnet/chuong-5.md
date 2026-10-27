## 5.1. Báo cáo bắt đầu từ câu hỏi nghiệp vụ

Báo cáo là cách trình bày dữ liệu phục vụ một quyết định: danh sách sinh viên theo khoa, điểm trung bình theo nhóm hoặc số lượt đăng ký theo học kỳ. Trước khi kéo thả trường dữ liệu, xác định người đọc, bộ lọc, đơn vị tính, thời điểm chốt và công thức tổng hợp.

Một báo cáo có thể đúng bố cục nhưng sai số liệu nếu JOIN làm nhân bản dòng hoặc bộ lọc không thống nhất. Kiểm chứng truy vấn trong SQL Server với tập dữ liệu nhỏ có đáp án tính tay. Giữ cùng một định nghĩa “số sinh viên” giữa tiêu đề, tổng cuối trang và dữ liệu nguồn.

Trong chuỗi bài tập, ứng dụng WinForms và Crystal Reports chạy trên môi trường .NET Framework tương thích. Trước khi cài, kiểm tra tài liệu SAP về Visual Studio, support pack, framework và kiến trúc x86/x64. Không chỉ nhìn hệ điều hành 64 bit rồi mặc định mọi project và runtime đều phải là x64.

## 5.2. Chuẩn bị nguồn dữ liệu

Có thể lấy dữ liệu qua DAL và đưa vào DataTable/DataSet trước khi gán cho ReportDocument. Cách này giữ phần truy vấn trong tầng dữ liệu, giảm việc nhúng thông tin kết nối trực tiếp vào tệp thiết kế report. Schema tên cột và kiểu dữ liệu phải khớp với thiết kế.

```csharp
// Trong Form đã có viewer; report và DAL là các lớp do người học tạo.
private CrystalDecisions.CrystalReports.Engine.ReportDocument _report;

private void HienThiBaoCao(string maKhoa)
{
    System.Data.DataTable data = _dal.GetStudentsByDepartment(maKhoa);
    var report = new CrystalReportSinhVienTheoKhoa();
    report.SetDataSource(data);
    report.SetParameterValue("pTenKhoa", cboKhoa.Text);
    crystalReportViewer1.ReportSource = null;
    if (_report != null) { _report.Close(); _report.Dispose(); }
    _report = report;
    crystalReportViewer1.ReportSource = _report;
}
```

Đoạn mã giả định report có tham số pTenKhoa và schema khớp kết quả truy vấn. Không dispose report khi viewer còn cần sử dụng. Khi Form đóng, bỏ ReportSource rồi Close/Dispose report đang giữ. Một report mở được lần đầu chưa chứng minh vòng đời tài nguyên đã đúng; thử đổi bộ lọc và mở đóng lặp lại.

## 5.3. Cấu trúc một báo cáo

Report Header chứa tiêu đề tổng thể; Page Header chứa đầu cột lặp mỗi trang; Details hiển thị dữ liệu từng bản ghi. Group Header và Group Footer trình bày theo nhóm; Report Footer chứa tổng hợp cuối báo cáo; Page Footer chứa thông tin trang. Chọn đúng vùng tránh lặp tiêu đề hoặc tổng cộng ở mỗi dòng.

Dùng tên báo cáo **HoangTN — Danh sách sinh viên** hoặc một tên trung tính. Bài tập không cần logo trường, dấu, chữ ký, phông nền hay màu nhận diện của một đơn vị. Với ngày lập, phân biệt ngày xuất tệp và mốc dữ liệu được tổng hợp.

Căn lề số theo kiểu dữ liệu và ghi rõ số chữ số thập phân. Bật khả năng mở rộng chiều cao cho tên dài khi cần. Kiểm tra khổ A4, hướng dọc/ngang, lề và ngắt trang. Xem bản xuất PDF vì kết quả trong viewer không luôn giống bản in.

## 5.4. Tham số, nhóm và công thức

Tham số cho phép thay đổi khoa, khoảng điểm hoặc học kỳ mà không sửa thiết kế report. Khi lọc tại SQL, truyền bằng SqlParameter; khi đưa nhãn lên tiêu đề, dùng tham số report riêng. Tiêu đề và dữ liệu phải cùng biểu diễn bộ lọc đã áp dụng, tránh hiển thị tên khoa A nhưng lấy dữ liệu khoa B.

Nhóm theo mã khoa hoặc khóa ổn định, hiển thị tên khoa cho người đọc. Dùng Count hoặc DistinctCount theo đúng câu hỏi. Với điểm trung bình, xác định rõ có tính các bản ghi chưa có điểm không và trung bình có trọng số hay không. Không tự đổi NULL thành 0 nếu nghiệp vụ chưa quy định.

Kiểm thử nhóm chỉ có một dòng, nhóm nhiều dòng, nhóm có dữ liệu thiếu và không có nhóm nào. Tính tay một nhóm nhỏ để so sánh. Khi dữ liệu lớn, cân nhắc tổng hợp tại SQL để hạn chế truyền nhiều dòng không cần thiết; đánh đổi này phải giữ cùng ý nghĩa công thức.

## 5.5. Xuất PDF và Excel

PDF phù hợp chia sẻ bố cục cố định và in. Excel phù hợp lọc, tính tiếp và phân tích. Xuất bố cục report sang Excel có thể tạo nhiều ô ghép; nếu mục tiêu là dữ liệu sạch, nên xây một bảng dữ liệu xuất riêng thay vì cố giữ nguyên mọi yếu tố trình bày.

Không đổi đuôi `.csv` thành `.xlsx`: đó là hai định dạng khác nhau. Nếu xuất CSV, xử lý dấu phân cách, dấu nháy và ký tự xuống dòng, ghi đúng encoding. Dữ liệu văn bản bắt đầu bằng `=`, `+`, `-` hoặc `@` cần được xử lý theo chính sách xuất để tránh bị phần mềm bảng tính diễn giải ngoài ý muốn.

Khi xuất file, kiểm tra quyền ghi, tệp đang bị khóa và khả năng người dùng hủy hộp thoại. Thông báo đường dẫn khi xuất thành công. Với dữ liệu lớn, không để giao diện treo mà không có trạng thái tiến độ hoặc khả năng chờ có chủ đích.

## Thực hành và tự kiểm tra

Làm [Lab 13 — Báo cáo cơ bản](lab-13.html) và [Lab 14 — Tham số, nhóm, Excel](lab-14.html). Dùng dữ liệu ứng dụng từ Lab 12 và kiểm chứng số lượng bản ghi trước khi thiết kế.

- Báo cáo không có dữ liệu phải cho người đọc biết điều gì?
- Tổng cuối báo cáo có khớp truy vấn kiểm chứng độc lập không?
- Kiểm tra tên rất dài, nhiều trang và bộ lọc không tìm thấy bản ghi.
- Mở lại PDF/Excel xuất ra trên một máy khác; kiểm tra tiếng Việt và số liệu.

## Đọc thêm

[SAP — Crystal Reports for Visual Studio](https://pages.community.sap.com/topics/crystal-reports/visual-studio), [SAP — Supported Platforms](https://userapps.support.sap.com/sap/support/knowledge/en/1943973) và [hướng dẫn môi trường phát triển](https://help.sap.com/docs/SAP_CRYSTAL_REPORTS%2C_DEVELOPER_VERSION_FOR_MICROSOFT_VISUAL_STUDIO/0d6684e153174710b8b2eb114bb7f843/45c076a36e041014910aba7db0e91070.html). Nội dung chương được phát triển từ mục tạo báo cáo của đề cương, bài giảng nâng cao và Lab 13–14.
