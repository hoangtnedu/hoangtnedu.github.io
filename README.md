# HoangTN — Ghi chép nghiên cứu

Blog tĩnh cho GitHub Pages, biên soạn bởi Trần Nguyên Hoàng.

## Bài FloodAI

- 26 mục có nguyên lý, công thức, lưu đồ, kiến trúc và bảng cấu hình.
- 26 video MP4 và 26 ảnh động GIF dùng dữ liệu giả lập.
- Tài liệu Word 55 trang và draw.io 78 trang.
- Trang bài: `2026/10/27/floodai-mo-hinh-hoc-sau.html`.
- Nội dung Markdown: `content/floodai-mo-hinh-hoc-sau.md`.
- Dữ liệu cấu trúc: `content/floodai-models.json`.

## Xuất bản

1. Dùng repository công khai `hoangtnedu.github.io` của tài khoản `hoangtnedu`.
2. Đưa toàn bộ các tệp và thư mục trong gói này vào thư mục gốc nhánh `main`.
3. Trong Settings → Pages, chọn Deploy from a branch → main → /(root) → Save.
4. Khi GitHub Pages hoàn tất triển khai, mở địa chỉ được hiển thị trong Settings → Pages.

Trang dùng HTML/CSS/JS thuần, không cần npm hay Jekyll; `.nojekyll` được giữ ở thư mục gốc. Tất cả hình, công thức và video nằm trong repository, không cần CDN.

## Xem thử tại máy

Chạy `python3 -m http.server 8000` ở thư mục gốc rồi mở `http://localhost:8000`. Có thể mở `index.html` trực tiếp nếu chỉ cần đọc offline.

## Biên tập

Sửa bài HTML để thay đổi ngay nội dung đang hiển thị. Bản Markdown dùng để đọc và biên tập nội dung; hiện không có quy trình tự sinh HTML khi Markdown thay đổi. CSS và JS ở `assets/css/site.css` và `assets/js/site.js`.

Không có dữ liệu quan trắc thật hay trọng số mô hình đã huấn luyện trong gói. Các cấu hình và hoạt ảnh phục vụ minh họa, không thay thế thực nghiệm.

Tài liệu GitHub Pages: https://docs.github.com/en/pages/getting-started-with-github-pages/configuring-a-publishing-source-for-your-github-pages-site


## Lập trình .NET

- Trang học liệu: `dotnet/index.html`.
- 6 chương theo khung Lập trình .NET, 16 lab desktop và 160 bài tập.
- 6 bài mở rộng M1–M6 về ASP.NET Core, EF Core, kiểm thử, bảo mật, AI và Azure.
- Markdown lý thuyết và dữ liệu lab đã biên tập: `content/dotnet/`.
- Tái tạo 26 trang bằng `python3 tools/build_dotnet.py` (Python 3, không cần thư viện ngoài).
- HTML sinh ra được commit cùng nguồn; GitHub Pages phục vụ trực tiếp.
- Môi trường lab: Windows, .NET Framework 4.8, SQL Server; Crystal Reports tương thích cho bài báo cáo. Phần web dùng project .NET hiện đại riêng.
- Chỉ dùng nhận diện cá nhân HoangTN. Các tài liệu gốc có nhận diện tổ chức không được đưa vào chuyên mục. Nguồn đọc thêm được liên kết tại `dotnet/tai-lieu.html`.


## Công nghệ Java

- Trang học liệu: `java/index.html`, gồm 4 chương và 20 trang theo buổi, với 4 buổi review project cuối.
- Nguồn nội dung: `content/java/*.md`, lịch trình và nhiệm vụ bổ sung: `content/java/lich-trinh.json`.
- Lab 1 giữ 5 bài toán của lab được cung cấp; các bài thực hành còn lại được ghi rõ là bổ sung theo lịch trình.
- Mã mẫu Maven có thể tải từng tệp tại `java/examples/lab01/`.
- Tái tạo 29 trang và cập nhật riêng URL Java trong sitemap: `python3 tools/build_java.py` (Python 3, thư viện chuẩn).
- Phần Java SE dùng JDK 17/21; Jakarta EE dùng runtime Web Profile phù hợp; ví dụ Spring chọn Boot 3.5.x/JDK 21 và project riêng.
- Đã viết lại nội dung và đổi package sang nhận diện cá nhân. Không phát hành tài liệu gốc có nhận diện tổ chức, mẫu slide, biểu mẫu hay PDF thương mại.
- Trang nguồn tham khảo giải thích rõ phạm vi sử dụng sách Struts 1.x và Spring/Hibernate 2010, kèm liên kết tài liệu chính thức hiện hành.

## Kiểm tra mã Java bằng GitHub Actions

- Workflow `Java examples CI`: `.github/workflows/java-examples.yml`.
- JDK 17 và 21: chạy Maven verify, JUnit cho 5 bài toán và 12 kịch bản chạy JAR.
- Tự chạy khi thay đổi `java/examples/**`, script smoke hoặc workflow; hỗ trợ Run workflow thủ công.
- Lưu JAR và báo cáo cho mỗi JDK trong artifact 7 ngày. Workflow chỉ đọc repository.
- Hiện phạm vi kiểm tra là project Maven Lab 1; các đoạn minh họa Swing/Jakarta EE/Spring trong bài giảng chưa phải project chạy độc lập.
