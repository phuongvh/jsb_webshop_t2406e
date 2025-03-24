package exam.jsb_webshop_t2406e.domain.manufacturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// http://localhost:8080/admin/manufacturer
@Controller
public class AdminManufacturerController 
{
    @Autowired
    private JpaManufacturer jpaManufacturer; // cung cấp các dịch vụ thao tác dữ liệu
    // todo: có thể phải quan tâm đến jpa bảng phụ

    @Autowired
    private HttpServletRequest request; 

    @Autowired
    private HttpSession session;

    // @Autowired cho phép coder không cần phải viết tường minh hàm khởi tạo
    // và khởi tạo 3 biến đặc biệt ở trên: jpa, request, session
    // Spring Boot sẽ làm tự động giúp chúng ta.

    // Chú ý đánh dấu @Controller cho lớp này
    // để Java Spring Boot biết đây là một Controller
    @GetMapping("/admin/manufacturer/add")
    public String getAdd(Model model) 
    {
        var entity = new Manufacturer();

        // Gửi đối tượng dữ liệu sang bên view
        // để còn ràng buộc vào html form
        // object dl này mới toanh, id=0 nên Java Spring đủ thông minh
        // để hiểu là đang thêm mới hay là sửa ?
        // vì vậy thẻ <input name="id"> bên form html không cần phải có disabled
        model.addAttribute("entity", entity);
        model.addAttribute("action", "/admin/manufacturer/add");

        // Nội dung riêng của trang...
        // model.addAttribute("content", "manufacturer/add.html"); 
        // model.addAttribute("content", "manufacturer/add-bs4.html"); 
        model.addAttribute("content", "manufacturer/form.html"); // sử dụng chung form với sửa
//        todo gửi dữ liệu bảng phụ sang thẻ select nếu cần.

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html"; 
    }
}
