## Phạm vi và cách tổ chức nhóm

Chọn một bài toán quản lý có dữ liệu quan hệ, ví dụ thư viện, bán hàng, sinh viên hoặc đặt lịch. Mục tiêu là giải thích được luồng nghiệp vụ và xây dựng được sản phẩm chạy lặp lại từ mã nguồn. Chọn một hướng chính: desktop Swing/JDBC, Jakarta EE hoặc Spring Boot. Không ghép cả ba vào một ứng dụng nếu đề tài không cần.

Mỗi nhóm thống nhất chức năng bắt buộc, chức năng mở rộng, phân công và tiêu chí hoàn thành. Dùng issue hoặc bảng công việc để theo dõi người phụ trách, trạng thái và bằng chứng. Mỗi thành viên phải hiểu phần mình làm và luồng chung của sản phẩm. Tránh chia việc theo kiểu một người viết toàn bộ code, các thành viên còn lại chỉ làm báo cáo.

Chức năng cốt lõi gợi ý: CRUD ít nhất hai thực thể có quan hệ, tìm kiếm/lọc, validation, lưu dữ liệu, xử lý lỗi và một quy trình nghiệp vụ. Với ứng dụng web, thêm đăng nhập, phân quyền và bảo vệ các thao tác thay đổi dữ liệu. Với desktop, xác định rõ mô hình người dùng và môi trường chạy thay vì áp dụng máy móc yêu cầu web.

## Buổi 17. Review phân tích và thiết kế

Trình bày người sử dụng, vấn đề cần giải quyết, các luồng chính và phạm vi ngoài đề tài. Mô tả mỗi chức năng bằng đầu vào, bước xử lý, đầu ra và trường hợp lỗi. Thiết kế ERD với khóa chính, khóa ngoại và ràng buộc, rồi kiểm tra xem dữ liệu có hỗ trợ được luồng nghiệp vụ không.

- Chuẩn bị đề cương ngắn, danh sách chức năng, use case, ERD và giao diện mẫu.
- Demo một hành trình từ màn hình danh sách đến biểu mẫu chi tiết bằng prototype hoặc trang mẫu.
- Trả lời: nếu xóa một bản ghi đang có dữ liệu liên quan thì hệ thống xử lý thế nào?
- Sau review, chốt chức năng bắt buộc, chia task và ghi các thay đổi được thống nhất.

Sản phẩm cần đạt là một phạm vi có thể hoàn thành trong thời gian còn lại. Một chức năng quan trọng phải có người phụ trách và cách kiểm tra rõ, không chỉ có tên trong danh mục.

## Buổi 18. Review kiến trúc và chức năng lõi

Chạy sản phẩm từ máy đã chuẩn bị, trình bày cấu trúc package và theo dõi một yêu cầu từ giao diện tới CSDL. Đối chiếu vai trò controller, service, DAO/repository và entity. Nếu service chỉ chuyển tiếp lời gọi, hãy giải thích quy tắc nghiệp vụ thực sự nằm ở đâu.

- Demo CRUD và tìm kiếm với dữ liệu mẫu có quan hệ.
- Chỉ ra điểm kiểm tra dữ liệu, xử lý ngoại lệ và ranh giới transaction.
- Thử mã trùng, khóa ngoại không hợp lệ, bản ghi bị xóa trước khi sửa và lỗi kết nối.
- Sau review, sửa lỗi kiến trúc và hoàn thành các luồng bắt buộc trước khi thêm chức năng phụ.

Sản phẩm cần đạt: một luồng nghiệp vụ đi trọn từ đầu tới cuối, dữ liệu lưu đúng sau khi khởi động lại. Video hoặc ảnh riêng lẻ không thay thế được khả năng chạy ứng dụng.

## Buổi 19. Review chất lượng, bảo mật và báo cáo

Lập bảng test case gồm mã ca, điều kiện trước, thao tác, dữ liệu, kết quả mong đợi, kết quả thực tế và trạng thái. Ưu tiên luồng chính, dữ liệu biên và tình huống thất bại. Ví dụ với đăng nhập: đúng tài khoản, sai password, chưa đăng nhập mở URL riêng, USER mở URL ADMIN và đăng xuất rồi mở lại trang.

- Kiểm tra validation phía server, escape nội dung hiển thị và truy vấn tham số hóa.
- Với web, thử CSRF, phân quyền qua URL và hành động thay đổi dữ liệu; với desktop, kiểm tra tác vụ nền và lỗi CSDL không làm treo form.
- Kiểm tra README, script CSDL, dữ liệu demo, cấu hình môi trường và khả năng build từ bản clone sạch.
- Hoàn thiện báo cáo phân tích, thiết kế, thực hiện và kiểm thử; ghi rõ giới hạn còn tồn tại.

Sản phẩm cần đạt: bảng test có bằng chứng, danh sách lỗi với mức ảnh hưởng và bản ứng viên demo. Nếu chưa sửa được một lỗi, mô tả cách tái hiện và phạm vi ảnh hưởng thay vì ghi chung “đã hoàn thành”.

## Buổi 20. Demo cuối và bàn giao

Demo theo một kịch bản nghiệp vụ liền mạch: tạo dữ liệu, thao tác chính, tìm kiếm, kiểm tra ràng buộc và quan sát dữ liệu sau khi khởi động lại. Mỗi thành viên giải thích được một quyết định thiết kế và một lỗi đã xử lý. Chuẩn bị dữ liệu giả lập, không dùng thông tin cá nhân thật.

- Nộp source code, `pom.xml`, README, script/migration CSDL và cấu hình mẫu.
- Nộp báo cáo, slide, bảng test, phân công và phiên bản/commit dùng để demo.
- Không đưa thư mục `target`, mật khẩu, file cấu hình bí mật hoặc database thật vào repository.
- Ghi JDK, server/Boot, CSDL, lệnh build/run, URL và cách tạo tài khoản demo.

## Gợi ý tự đánh giá trước khi nộp

Đây là checklist tự rà soát, không thay thế phiếu chấm chính thức: yêu cầu và dữ liệu rõ; chức năng chính chạy; kiến trúc phân tách; xử lý đầu vào và quyền truy cập; có test và bằng chứng; có thể chạy lại từ README; giải thích được mã nguồn. Cơ cấu điểm chính thức do giảng viên thông báo theo học phần.
