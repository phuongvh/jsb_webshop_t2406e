package exam.jsb_webshop_t2406e.domain.category;

import java.util.HashMap;
// Thư viện chuẩn: Java Standard Edition(JavaSE)
import java.util.List;
import java.sql.Date;
import java.time.LocalDate;

import jakarta.servlet.http.HttpServletRequest;
// Thư viện doanh nghiệp: Java Entprise Edition(JavaEE)
// import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

// Thư viện web: Java SpringBoot
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import exam.jsb_webshop_t2406e.domain.product.Product;

@Controller
@RequestMapping("/admin/category")
public class AdminCategoryController 
{
    @Autowired
    private JpaCategory jpaCategory; // cung cấp các dịch vụ thao tác dữ liệu

    @Autowired
    private HttpServletRequest request; 

    @Autowired
    private HttpSession session;

    //@Autowired
    //private DvlBảngNgoại dvlBangNgoai; // cung cấp các dịch vụ thao tác dữ liệu

    // todo: Phân Trang, Lọc
    @GetMapping
    public String getAll(Model model) 
    {
        model
        .addAttribute("listCategory", jpaCategory.findAll()) 
        .addAttribute("title", "Category")
        .addAttribute("content", "category/table.html")
        ; 

        return "layout/layout-admin.html"; 
    }

        // Hàm này sẽ làm hơi khác chút so với edit/delete
    @GetMapping("/{id}")
    public String view( // details
                Model model, // mô hình dữ liệu của view html
                @PathVariable("id") int id // mã định của bản ghi cần sửa
        ) {
            // Tìm thực thể dữ liệu đang có trong csdl, theo mã định danh
            var entity = jpaCategory.findById(id).get();
    
            // Gửi thực thể cũ đó sang view html form để sửa
            model
                    .addAttribute("entity", entity)
                    // Gửi thêm dữ liệu của bảng phụ
                    // 2 câu lệnh hiển thị giao diện của Form Edit
                    .addAttribute("title", "View Details")
                    .addAttribute("content", "/category/view.html");
    
            return "/layout/layout-admin.html";
    }

    @GetMapping({"/add", "/create"})
    public String getAdd(Model model) 
    {

        model
        .addAttribute("entity", new Category())
        .addAttribute("content", "category/form.html"); 

        return "layout/layout-admin.html"; 
    }

    @GetMapping("/add/ajax")
    public String getAddAjax(Model model) 
    {

        model
        .addAttribute("entity", new Category())
        ;

        return "category/form.html"; 
    }

    @GetMapping("/edit")
    public String getEdit(Model model, @RequestParam("id") int id) 
    {

        model
        .addAttribute("entity", jpaCategory.findById(id).get())
        .addAttribute("content", "category/form.html")
        ; 

        return "layout/layout-admin.html"; 
    }

    @GetMapping("/edit/ajax")
    public String getEditAjax(Model model, @RequestParam("id") int id) 
    {

        model
        .addAttribute("entity", jpaCategory.findById(id).get())
        ; 

        return "category/form.html"; 
    }

    @GetMapping("/delete")
    public String getDelete(Model model, @RequestParam(value = "id") int id) 
    {
        model
        .addAttribute("entity", jpaCategory.findById(id).get())
        .addAttribute("content", "category/delete.html")
        ; 

        return "layout/layout-admin.html"; 
    }



    /**
     * @abstract Hàm này sử dụng chung cho các tình huống: Thêm, Sửa, Context Menu
     * <input type="date" id="ngayTao" hidden th:field="${entity.dateCreated}" readonly/> 
     * Bỏ đi vì nó gây chết  lúc add/create: 400 Bad Request
     * @param cauThu
     * @return
     */
    @PostMapping("/save")
    public String postSave(@ModelAttribute Category entity) 
    {
        
        var successMessage = "Successfully created !";

        // if( jpaCategory.existsById(entity.getId()) )
        // if( entity.getId() != null )
        // if (entity.getId() != null && entity.getId() > 0)
        if (entity.getId() > 0)
        // if (entity.getDateCreated() != null) // lỗi ko tương  thích khi thêm mới: client là String, Server là Date
        {
            successMessage = "Successfully updated !";

            // <input hidden readOnly/> //Lỗi 400 Bad Request vì khi thêm mới nó gửi lên NULL
            entity.setDateCreated(jpaCategory.findById(entity.getId()).get().getDateCreated());
        }

        entity.setDateUpdated(new Date(System.currentTimeMillis())); // rất cần vì @PrePersist method chỉ gọi khi tạo mới
        if(entity.getActive()==null)
            entity.setActive(false);
        if(entity.getFeatured()==null)
            entity.setFeatured(false);
        if(entity.getTop()==null)
            entity.setTop(false);

        // Lưu entity vào csdl (không cần biế t là đang thêm mới hay sửa cũ)
        jpaCategory.save(entity);

        // Gửi thông báo thành công sang bên View HTML
        session.setAttribute("SuccessMessage", successMessage);

        // Điều hướng sang trang danh sách
        return "redirect:/admin/category";
    }


    // request param phải khớp với name="Id" của thẻ html input
    // Khi xóa thì Java chỉ cần biết mã định danh của bản ghi cần xóa
    @PostMapping("/delete")
    public String postDelete(Model model, @RequestParam("id") int id) 
    {

        // Xoá dữ liệu
        jpaCategory.deleteById(id);

        // Gửi thông báo thành công từ view Add/Edit sang view List
        session.setAttribute("SuccessMessage", "Successfully deleted !");

        // Điều hướng sang trang giao diện
        return "redirect:/admin/category";
    }


    @GetMapping(path = "/list/json/smui", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListJsonForSemanticUI() 
    {
        // Get data from service layer into entityList.

        var filter_ten  = request.getParameter("q");

        // var list = jpaCauThu.findAll();
        // var list = jpaCauThu.findByTenDayDuContaining(filter_ten); // ok, chạy tốt
        var list = jpaCategory.findByNameContainingIgnoreCase(filter_ten); // ok, chạy tốt

        // list.forEach(entity->entity.setMessages(null));

        // Loại bỏ các trường thông tin không cần thiết khỏi quá trình 
        // chuyển đổi, sinh ra chuỗi: JSON Serialization
        // list.forEach(entity->{
        //     entity.setMessages(null);
        //     entity.setTinhThanh(null);
        // });

        java.util.
        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("results", list);
        data.put("totalRecordCount", list.size());

        return new ResponseEntity<>(data, HttpStatus.OK);
        // return new ResponseEntity<Object>(data, HttpStatus.OK);
    }

    @GetMapping(path = "/list/json", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListJson() 
    {
        // Get data from service layer into entityList.

        var filter_ten  = request.getParameter("q");

        // var list = jpaCauThu.findAll();
        // var list = jpaCauThu.findByTenDayDuContaining(filter_ten); // ok, chạy tốt
        // var list = jpaCategory.findByNameContainingIgnoreCase(filter_ten); // ok, chạy tốt
        var list = jpaCategory.findAll(); // ok, chạy tốt

        // list.forEach(entity->entity.setMessages(null));

        // Loại bỏ các trường thông tin không cần thiết khỏi quá trình 
        // chuyển đổi, sinh ra chuỗi: JSON Serialization
        // list.forEach(entity->{
        //     entity.setMessages(null);
        //     entity.setTinhThanh(null);
        // });

        return new ResponseEntity<>(list, HttpStatus.OK);
        // return new ResponseEntity<Object>(data, HttpStatus.OK);
    }

}// end class
