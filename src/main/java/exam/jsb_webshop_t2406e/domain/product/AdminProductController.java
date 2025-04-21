package exam.jsb_webshop_t2406e.domain.product;

// Thư viện chuẩn: Java Standard Edition(JavaSE)
import java.util.List;
import java.time.LocalDate;

// Thư viện doanh nghiệp: Java Entprise Edition(JavaEE)
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
// import spring.jsb_web_shop.admin.Admin;

// Thư viện web: Java SpringBoot
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Chú Ý: Phong cách lập trình, viết mã phải gọn và tối giản.
 * Bỏ qua những phần không chính yếu, không cần thiết.
 * 
 * Cân nhắc hàm getById() để view details sản phẩm.
 * Hàm này nhìn qua thì không cần, nhưng khi  xét đến quyển (authorize, permission, role)
 * thì lại thấy cần thiết.
 */

@Controller
@RequestMapping("/admin/product")
public class AdminProductController 
{
    @Autowired
    private JpaProduct jpaProduct; // cung cấp các dịch vụ thao tác dữ liệu

    @Autowired
    private HttpServletRequest request; 

    @Autowired
    private HttpSession session;

    //@Autowired
    //private DvlBảngNgoại dvlBangNgoai; // cung cấp các dịch vụ thao tác dữ liệu

    
    @GetMapping({"/index","/list"})
    public String getList(Model model) 
    {

        // Đọc dữ liệu bảng 
        var list = jpaProduct.findAll(); 

        // Gửi danh sách sang giao diện View HTML
        model
        .addAttribute("list", list)
        .addAttribute("title", "Quản Lý Sản Phẩm")
        .addAttribute("content", "product/table.html"); 

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html"; 
    }

    @GetMapping("/add")
    public String getAdd(Model model) 
    {

        model
        .addAttribute("entity", new Product())
        .addAttribute("title", "Thêm Sản Phẩm")
        .addAttribute("content", "product/form.html"); 

        return "layout/layout-admin.html"; 
    }

    @GetMapping("/edit")
    public String getEdit(Model model, @RequestParam("id") int id) 
    {

        // Gửi đối tượng dữ liệu sang giao diện (ui view) html form
        model
        .addAttribute("entity", jpaProduct.findById(id).get())
        .addAttribute("title", "Sửa Sản Phẩm")
        .addAttribute("content", "product/form.html"); // sua.html

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html"; // layout.html
    }

    // @GetMapping("/delete")
    // public String getDelete(Model model, @RequestParam(value = "id") int id) 
    // {

    //    // Tìm bản ghi theo mã định danh trong csdl
    //    var entity = jpaProduct.findById(id).get();

    //    // Mô hình dữ liệu của view HTML
    //    model
    //     .addAttribute("product", entity)
    //         //    .addAttribute("listTinhThanh", jpaTinhThanh.findAll())
    //     .addAttribute("content", "/product/delete.html");

    //    // Bố cục chung
    //    return "/layout/layout-admin.html";
    // }

    /**
     * Đường dẫn của phần xem chi tiết sẽ hơi khác chút so với sửa, xóa
     * ví dụ:
     *  http://localhost:6868/qdl/thucthe/xem/3
     * nghĩa là xem chi tiết thực thể có mã định danh = 3
     * @param model
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public String getById(Model model, @PathVariable(value = "id") int id) 
    {

        // Gửi đối tượng dữ liệu sang giao diện html form
        model
        .addAttribute("product", jpaProduct.findById(id).get())
        .addAttribute("content", "product/view.html")
        ; 

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html";    // layout.html
    }

        /**
     * @abstract Hàm này sử dụng chung cho các tình huống: Thêm, Sửa, Context Menu
     * @param cauThu
     * @return
     */
    @PostMapping("/save")
    public String postSave(@ModelAttribute Product entity) 
    {
        // if (cauThu.getConHopDong() == null)
        //     cauThu.setConHopDong(false);

        var successMessage = (entity.getId() >= 1) // hoặc jpaCauThu.existsById(cauThu.getId())
                ? "Đã xong việc chỉnh sửa."
                : "Đã xong việc thêm mới.";


        // Lưu entity vào csdl (không cần biế t là đang thêm mới hay sửa cũ)
        jpaProduct.save(entity);

        // Gửi thông báo thành công sang bên View HTML
        session.setAttribute("SuccessMessage", successMessage);

        // Điều hướng sang trang danh sách
        return "redirect:/admin/cauthu";
    }

    // @PostMapping("/add")
    // public String postAdd(@ModelAttribute Product entity) 
    // {
        
    //     // todo: Kiểm duyệt dữ liệu cho nó sạch, trước khi lưu vào csdl

    //     jpaProduct.save(entity); // thêm mới dữ liệu vào csdl

    //     // Gửi thông báo thành công từ giao diện Thêm Mới sang giao diện Duyệt
    //     session.setAttribute("SuccessMessage", "Successfully added new entity Product !");

    //     return "redirect:/admin/product/list";
    // }

    // /**
    //  * 
    //  * @param dl Dữ Liệu mà HTM Form gửi lên
    //  * @return
    //  */
    // @PostMapping("/edit")
    // public String postEdit(@ModelAttribute Product entity) 
    // {
        
    //     //todo: Kiểm duyệt dữ liệu cho nó sạch, trước khi lưu vào csdl

    //     jpaProduct.save(entity); // cập nhật dữ liệu vào csdl

    //     // Gửi thông báo thành công từ view Add/Edit sang view List
    //     session.setAttribute("SuccessMessage", "Successfully updated the entity Product !");

    //     return "redirect:/admin/product/list";
    // }

    // request param phải khớp với name="Id" của thẻ html input
    // Khi xóa thì Java chỉ cần biết mã định danh của bản ghi cần xóa
    @PostMapping("/delete")
    public String postDelete(Model model, @RequestParam("id") int id) 
    {
        // Xoá dữ liệu
        jpaProduct.deleteById(id);;

        // Gửi thông báo thành công từ view Add/Edit sang view List
        session
        .setAttribute("SuccessMessage", "Đã xong việc xóa !");

        // Điều hướng sang trang giao diện
        return "redirect:/admin/product/list";
    }

    @GetMapping("/add/ajax")
    public String getAddAjax(Model model) {
        // Tạo mới thực thể
        var entity = new Product();
        // với một vài giá trị mặc định (do lập trình tự quyết)
        // entity.setConHopDong(true);
        // entity.setChieuCao(1.8f);
        // entity.setNhomMau('B');

        // Gửi thực thể mới đó sang HTM Form để end user có thể chỉnh sửa dễ dàng và
        // kiểm duyệt
        model
        .addAttribute("entity", entity);
                // Gửi thêm dữ liệu của bảng phụ
        // .addAttribute("listTinhThanh", jpaTinhThanh.findAll());
        // 2 câu lệnh hiển thị giao diện của Form Edit

        return "/product/form.html";
    }

    @GetMapping("/edit/ajax")
    public String getEditAjax(
            Model model, // mô hình dữ liệu của view html
            @RequestParam("id") int id // mã định của bản ghi cần sửa
    ) {
        // Tìm thực thể dữ liệu đang có trong csdl, theo mã định danh
        var entity = jpaProduct.findById(id).get();

        // Gửi thực thể cũ đó sang view html form để sửa
        model.addAttribute("entity", entity);
                // Gửi thêm dữ liệu của bảng phụ
                // .addAttribute("listTinhThanh", jpaTinhThanh.findAll());
        // 2 câu lệnh hiển thị giao diện của Form Edit

        return "/product/form.html";
    }

}// end class

