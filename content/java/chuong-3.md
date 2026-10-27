## 1. Kiến trúc ứng dụng Jakarta EE

Ứng dụng web tiếp nhận HTTP request, xử lý nghiệp vụ, truy cập dữ liệu và trả response. Trình duyệt giữ giao diện phía người dùng, còn server xử lý các quy tắc cần được kiểm soát tập trung. Khác với desktop, nhiều người dùng có thể đồng thời gửi yêu cầu; dữ liệu riêng của một request không được đặt vào biến thành viên dùng chung của servlet.

Chọn một runtime tương thích Jakarta EE 10 Web Profile cho chuỗi thực hành, chẳng hạn một bản phát hành tương ứng của GlassFish hoặc Payara. Ghi chính xác phiên bản server và JDK trong README. Tomcat đơn thuần cung cấp Servlet/JSP nhưng không tự cung cấp toàn bộ CDI, Faces và JPA cần cho các buổi tiếp theo. Với bài chỉ Servlet/JSP có thể dùng container servlet; với chuỗi đầy đủ, dùng runtime Web Profile giúp giảm cấu hình bổ sung.

Dự án web dùng đóng gói WAR. Dependency API Jakarta EE đặt phạm vi `provided` vì server cung cấp implementation. Maven dependency API không biến chương trình thành một server. Khi deploy phải xem log server, context path và URL thực tế.

## 2. CDI và phạm vi sống của đối tượng

Dependency Injection đưa phụ thuộc vào đối tượng thông qua container. Ví dụ controller cần service, service cần DAO. Khi container quản lý vòng đời, ta có thể thay implementation của DAO mà không sửa logic controller. Annotation chỉ có hiệu lực nếu đối tượng thật sự thuộc môi trường quản lý của container.

`@RequestScoped` phù hợp dữ liệu của một yêu cầu; `@SessionScoped` giữ trạng thái trong phiên người dùng; `@ApplicationScoped` dùng chung toàn ứng dụng. Đặt một danh sách có thể thay đổi vào application scope cần xét đồng thời truy cập. Bean session/view scope thường phải đáp ứng yêu cầu serialization của container.

```java
@jakarta.enterprise.context.ApplicationScoped
public class GreetingService {
    public String message() { return "Hoc Jakarta EE cung HoangTN"; }
}
```

Ví dụ service không giữ trạng thái thay đổi nên dễ dùng chung. Trong servlet được quản lý, có thể inject bằng `@jakarta.inject.Inject GreetingService service;`. Nếu tự tạo `new GreetingService()`, đối tượng đó không đi qua quy trình injection của container.

## 3. Servlet, JSP và luồng request/response

Servlet làm controller tiếp nhận yêu cầu; JSP là view tạo HTML. `request.setAttribute` chuyển dữ liệu tới view khi forward trong cùng request. Redirect gửi phản hồi để trình duyệt tạo request mới; request attribute không đi theo request mới đó. Sau POST thành công, redirect giúp hạn chế gửi lại biểu mẫu khi người dùng refresh.

Ví dụ servlet hoàn chỉnh sau cần Jakarta Servlet API của runtime. Đặt JSP ở `src/main/webapp/WEB-INF/views/students.jsp` để người dùng không mở trực tiếp view bằng URL.

```java
package io.github.hoangtnedu.java.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;

@WebServlet("/students")
public class StudentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("count", 3);
        request.getRequestDispatcher("/WEB-INF/views/students.jsp")
               .forward(request, response);
    }
}
```

```html
<%@ page contentType="text/html; charset=UTF-8" %>
<!doctype html><html lang="vi"><head><meta charset="utf-8">
<title>Sinh viên</title></head><body>
<h1>Danh sách sinh viên</h1><p>Số bản ghi minh họa: ${count}</p>
</body></html>
```

Giá trị `3` chỉ minh họa truyền dữ liệu, chưa truy vấn CSDL. Với nội dung do người dùng nhập, dùng cơ chế escape đầu ra thích hợp, chẳng hạn Jakarta Tags `c:out`. EL không tự bảo đảm mọi chuỗi hiển thị đều an toàn. Không viết SQL hoặc mật khẩu trong JSP.

## 4. Session, filter và xử lý lỗi

Session liên kết nhiều request của cùng người dùng. Sau đăng nhập thành công, cần đổi session ID để hạn chế session fixation, dùng thời gian hết hạn phù hợp và hủy session khi đăng xuất. Không giữ password, connection hoặc EntityManager trong session.

Filter thực hiện công việc chung như kiểm tra truy cập và encoding trước controller. Khi từ chối yêu cầu, trả response hoặc redirect rồi kết thúc xử lý; không tiếp tục gọi chain ngoài ý muốn. Filter chỉ là một phần của cơ chế bảo mật: cần kiểm soát quyền trên tài nguyên ở server, kiểm tra dữ liệu và bảo vệ yêu cầu làm thay đổi trạng thái.

Lỗi nhập liệu nên quay lại form với thông báo dễ hiểu và giữ dữ liệu hợp lệ. Lỗi hệ thống được ghi log phía server với mã truy vết; trang lỗi không hiển thị stack trace, chuỗi kết nối hay thông tin bí mật cho trình duyệt.

## 5. Jakarta Faces (JSF), Facelets và validation

Jakarta Faces xây dựng giao diện theo component và vòng đời xử lý, dùng Facelets cho view. Với mã hiện đại, bean thường dùng CDI `@Named` kết hợp scope phù hợp; tránh sao chép nguyên `@ManagedBean` từ giáo trình JSF cũ. Form binding nối trường nhập với thuộc tính bean. Validation chạy trước khi action hợp lệ cập nhật dữ liệu nghiệp vụ.

Đoạn Facelets dưới minh họa một biểu mẫu khi đã có bean `studentBean` với thuộc tính `name` và phương thức `save`. Phương thức `save` chỉ lưu khi dữ liệu hợp lệ. Trang cần khai báo namespace Faces đúng với runtime đang dùng.

```xml
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:h="jakarta.faces.html">
<h:head><title>Sinh viên</title></h:head>
<h:body><h:form>
  <h:outputLabel for="name" value="Họ tên"/>
  <h:inputText id="name" value="#{studentBean.name}" required="true"/>
  <h:message for="name"/>
  <h:commandButton value="Lưu" action="#{studentBean.save}"/>
</h:form></h:body></html>
```

Template giúp dùng chung header, menu và vùng nội dung. Khi thiết kế điều hướng, phân biệt action outcome và URL. Không dùng một bean session lớn chứa toàn bộ trạng thái của tất cả màn hình vì nhiều tab trình duyệt có thể ghi đè dữ liệu nhau.

## 6. Persistence, transaction và Servlet Security

Entity ánh xạ dữ liệu quan hệ sang đối tượng. `@Id` xác định khóa; quan hệ thể hiện bằng annotation tương ứng. Entity cần constructor không tham số theo yêu cầu JPA. EntityManager làm việc trong persistence context, không tương đương một collection thông thường. Thao tác ghi phải có transaction phù hợp với môi trường quản lý của server.

Tách lớp dịch vụ chứa ranh giới giao dịch. Ví dụ ghi phiếu và chi tiết phải thành công cùng nhau; nếu chi tiết lỗi, không để phiếu dở dang. Với Servlet Security, khai báo role, ràng buộc URL và cấu hình identity store/realm phù hợp server. `@ServletSecurity` không tự tạo tài khoản hoặc lưu mật khẩu cho ứng dụng.

Buổi 12 kết hợp JPA, bảo mật và chốt đề project. Hoàn thành một luồng CRUD có lưu dữ liệu, mô tả quyền USER/ADMIN, lập danh sách ca kiểm tra rồi chuẩn bị đề cương nhóm. Khối lượng này được chia trong [hướng dẫn buổi học](buoi-12.html).

## 7. Câu hỏi củng cố

- Vì sao request attribute mất sau redirect? Dữ liệu nào nên đặt trong session?
- Một servlet có thể nhận đồng thời hai request không? Biến thành viên có thể gây lỗi gì?
- Phân biệt API JPA, Hibernate implementation và Spring Data JPA.
- Kiểm tra người dùng không có role bằng cách truy cập URL trực tiếp, thay vì chỉ quan sát menu đã bị ẩn.

## Nguồn đọc theo chủ đề

Sách Programming Jakarta Struts được cung cấp dùng Struts 1.x: tham khảo tư duy MVC/Model 2, không dùng như cấu hình Jakarta EE hiện hành. Đối chiếu tài liệu chính thức về [Servlet](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/web/servlets/servlets.html), [CDI](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/cdi/cdi-basic/cdi-basic.html), [Faces](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/web/faces-intro/faces-intro.html) và [Persistence](https://jakarta.ee/learn/docs/jakartaee-tutorial/current/persist/persistence-intro/persistence-intro.html).
