package exam.jsb_webshop_t2406e.domain.manufacturer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.experimental.var;

import org.springframework.web.bind.annotation.RequestParam;


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

    // Viết ra lộ trình (route) đến trang danh sách (list page)
    // Khai báo chương trình con chịu trách nhiệm hiển thị trang html có chứa danh sách
    @GetMapping("/admin/manufacturer/list")
    public String 
    getList(Model model) 
    {
        // SQL Server -> (Java Server -> FE/UiX Server) -> Browser
        // Lấy dữ liệu các bản ghi trong CSDL
        var list = jpaManufacturer.findAll();

        // Gửi dữ liệu sang view HTML
        model.addAttribute("list", list);

        // Chỉ định view HTML nào sẽ hiển thị dữ liệu
        model.addAttribute("content", "manufacturer/list.html");

        // Chỉ định bố cục cho trang (layout)
        return "layout/layout-admin.html";
        // return new String("layout/layout-admin.html");
    }
    
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

    
    @PostMapping("/admin/manufacturer/add")
    public String postAdd(@ModelAttribute Manufacturer entity) 
    {

        // Một số thông tin thêm mới do hệ thống làm
        // bên cạnh dữ liệu mà user gõ trên Form Add New
        // dl.setNgayTao(LocalDate.now());
        // dl.setNgayedit(LocalDate.now());

        // Bản ghi mới mà Html Form submit lên, không có thời gian hiện tại
        // Nên chỗ này phải làm thủ công: Gán nhãn thời gian cho thực thể mới...
        // Chú Ý: không lấy thông tin này bên view HTMl vì sẽ có độ trễ lớn, thậm chí bị hack
        entity.setDateCreated(new java.sql.Date(System.currentTimeMillis()));
        entity.setDateUpdated(new java.sql.Date(System.currentTimeMillis()));
        
        // ...rồi mới lưu thực thể mới vào csdl
        jpaManufacturer.save(entity);

        // Gửi thông báo thành công từ giao diện Thêm Mới sang giao diện Duyệt
        session.setAttribute("SUCCESS_MESSAGE", "Successfully added new user !");

        // return "redirect:/admin/manufacturer/list";
        return "redirect:/add-ok.htm";
    }
}// end class
