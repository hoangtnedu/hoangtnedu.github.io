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
