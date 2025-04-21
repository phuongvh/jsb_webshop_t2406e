package exam.jsb_webshop_t2406e.domain.manufacturer;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
// import lombok.experimental.var;

import org.springframework.web.bind.annotation.RequestParam;


// http://localhost:8080/admin/manufacturer
@Controller
@RequestMapping("/admin/manufacturer")
public class AdminManufacturerController 
{
    // có nên @Autowired vào đây không ?
    private final SecurityFilterChain adminFilterChain;

    @Autowired
    private JpaManufacturer jpaManufacturer; // cung cấp các dịch vụ thao tác dữ liệu
    // todo: có thể phải quan tâm đến jpa bảng phụ

    @Autowired
    private HttpServletRequest request; 

    @Autowired
    private HttpSession session;

    AdminManufacturerController(SecurityFilterChain adminFilterChain) {
        this.adminFilterChain = adminFilterChain;
    }

            /**
     * Trả danh sách về cho client front end uix: Semantic UI
     * Thử cho ngay_sinh = NULL để kiểm tra json trả về
     * UPDATE `cau_thu` SET ngay_sinh=NULL WHERE id=8;
     * 
     * đã test thử: ok, null field đã bị loại khỏi chuỗi json trả về.
     * 
     * sau đó cho ngay_sinh quay lại giá trị cũ
     * UPDATE `cau_thu` SET ngay_sinh='1995-10-02' WHERE id=8;
     * 
     * Cấu trúc json trả về cho Semantic UI không nhất thiết phải theo chuẩn của Semantic UI
     * vì bạn có thể chuyển đổi JSOn đó sang định dạng mà Semantic UI yêu cầu
     * 
     * $('.ui.search').search({
            source: data.results.map(item => ({ 
                    title: item.tenDayDu,
                    image: item.anh,
                    description: item.tinhThanh ? item.tinhThanh.ten : '', // Safely access tinhThanh.ten
                    price: item.chieuCao+'(m)',
                    url: '/admin/cauthu/view/' + item.id
                })
            )
      });
     */
    @GetMapping(path = "/list/json/smui", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> getListJsonForSemanticUI() 
    {
        // Get data from service layer into entityList.

        var filter_ten  = request.getParameter("q");

        // var list = jpaCauThu.findAll();
        // var list = jpaCauThu.findByTenDayDuContaining(filter_ten); // ok, chạy tốt
        var list = jpaManufacturer.findByNameContainingIgnoreCase(filter_ten); // ok, chạy tốt

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
        data.put("TotalRecordCount", list.size());

        return new ResponseEntity<>(data, HttpStatus.OK);
        // return new ResponseEntity<Object>(data, HttpStatus.OK);
    }

    // @Autowired cho phép coder không cần phải viết tường minh hàm khởi tạo
    // và khởi tạo 3 biến đặc biệt ở trên: jpa, request, session
    // Spring Boot sẽ làm tự động giúp chúng ta.

    // Viết ra lộ trình (route) đến trang danh sách (list page)
    // Khai báo chương trình con chịu trách nhiệm hiển thị trang html có chứa danh sách
    @GetMapping({"/list", "/index"})
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
    @GetMapping("/add")
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

    
    @PostMapping("/add")
    public String postAdd(@ModelAttribute Manufacturer entity) 
    {
        // todo: Làm sao phòng chống việc form không submit giá trị null, undefined lên
        // Tinh chỉnh lại giá trị NULL mà form submit lên
        if(entity.getOrderNumber()==null)
            entity.setOrderNumber(0);

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

        session.setAttribute("SUCCESS_MESSAGE", "Successfully added new manufacturer !");

        return "redirect:/admin/manufacturer/list";
        // return "redirect:/add-ok.htm";
    }

    // http://localhost:8080/admin/manufacturer/delete?id=123
    @GetMapping("/delete")
    public String getDelete(Model model, @RequestParam(value = "id") int id) 
    {
        // Lấy ra bản ghi cần xóa
        var entity = jpaManufacturer.findById(id).get();

        // Gửi sang view
        // Gửi đối tượng dữ liệu sang giao diện html form xác nhận xóa
        model.addAttribute("entity", entity);

        // Hiển thị giao diện xác nhận xóa
        // Nội dung riêng của trang...
        model.addAttribute("content", "manufacturer/delete.html"); // xoa.html

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html"; // layout.html
    }

        // request param phải khớp với name="Id" của thẻ html input
    // Khi xóa thì Java chỉ cần biết mã định danh của bản ghi cần xóa
    @PostMapping("/delete")
    public String postXoa(Model model, @RequestParam("id") int id) 
    {

        // Xoá dữ liệu
        jpaManufacturer.deleteById(id);

        // Gửi thông báo thành công từ view Add/Edit sang view List
        session.setAttribute("SUCCESS_MESSAGE", "Đã xóa nhà sản xuất !");

        // Điều hướng sang trang giao diện
        return "redirect:/admin/manufacturer/list";
    }
    
    @GetMapping("/edit")
    public String getEdit(Model model, @RequestParam("id") int id) 
    {

        // Lấy ra bản ghi theo mã định danh
        var entity = jpaManufacturer.findById(id).get();

        // Gửi đối tượng dữ liệu sang giao diện (ui view) html form
        model.addAttribute("entity", entity);
        model.addAttribute("action", "/admin/manufacturer/edit");

        // Hiển thị giao diện view html
        // Nội dung riêng của trang...
        // model.addAttribute("content", "manufacturer/edit.html"); // edit.html
        model.addAttribute("content", "manufacturer/form.html"); // edit.html

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html"; // layout.html
    }

    @PostMapping("/edit")
    public String postEdit(@ModelAttribute Manufacturer dl) 
    {
        // Một số thông tin cập nhật do hệ thống làm
        // bên cạnh dữ liệu mà user gõ trên Form Edit/Update
        // dl.setNgayedit(LocalDate.now());

        // int id = Integer.parseInt(request.getParameter("id"));
        
        // dl.setId(id); // Đề phòng Ajax Form xử lý id không mượt

        jpaManufacturer.save(dl);

        // Gửi thông báo thành công từ view Add/Edit sang view List
        session.setAttribute("SUCCESS_MESSAGE", "Successfully updated manufacturer !");

        return "redirect:/admin/manufacturer/list";
    }

        // Chú Ý: Nếu hàm getPhanTrang() có các @RequestParam,thì bắt buộc
    // trên URL nó phải có các tham số đấy
    // Về mặt phong cách lập trình
    // thì nó khá giống với thư viện MVC Paging của .Net
    // Đem lại sự tiện lợi cho lập trình viên (Less Code, Do More)
    // @GetMapping("/page/{pageNo}")
    // //localhost:6868/nhansu/phantrang/3?sortField=ten&sortDirection=asc
    // Kiểm thử link Search & Pagination
    // http://localhost:6868/admin/manufacturer/phantrang/1?pageSize=10&sortField=ten&sortDir=desc&keyword=Apple
    // http://localhost:6868/admin/manufacturer/phantrang/1?pageSize=10&sortField=ten&sortDir=desc&keyword=av
    // @update: 2024.08.20 16h08 ITPlus K2022
    // bắt tham số phân trang, lọc, sắp xếp, bên trong hàm(), chứ không dùng @RequestParam vì nó cứng nhắc
    // và không tổng quát (Nó ép cho URL của mình phải có tham số đấy)
    // http://localhost:6868/admin/manufacturer/phantrang?pageSize=4
    // @GetMapping("/admin/manufacturer/phantrang")
    @GetMapping("/pagedlist")
    public String getPagedList(Model model) 
    {
        String sortField; // tên cột sắp xếp
        String sortDir;   // sort direction, chiều sắp xếp: asc, desc
        String sortRev;   // sort reversion, đảo chiều sắp xếp

        int pageNumber; // current page number, no: số thứ tự của trang hiện tại
        int pageSize; // kích thước của mỗi trang (số phần tử (tối đa)trên mỗi trang).
        // int pageCount; // đếm số phần tử thực tế của trang hiện tại

        // Tiếp nhận các tham số phân trang từ URL, link của máy khách
        try {
            pageNumber = request.getParameter("pageNumber") == null ? 1 :
                Integer.parseInt(request.getParameter("pageNumber"));
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        }catch(Exception e)
        {
            pageNumber = 1;
            pageSize = 5;
            // @todo: Liệu có đọc được ra từ setting ???
        }

        // Chuẩn hóa triệt để, không bao giờ để tham số phân trang bị null hoặc Zero
        if(pageNumber < 1) pageNumber = 1;
        if(pageSize < 1)   pageSize = 5;

        // bắt các tham số sắp xếp trên URL
        sortField = request.getParameter("sortField");
        sortDir   = request.getParameter("sortDir");

        // Nếu phía máy khách không chỉ rõ cột sắp xếp
        // thì sử dụng một cột mặc định bất kì nào đấy, thường là cột "tên"
        // 1. là sortField ko xuất hiện trên url params
        // 2. là sortField có xuất hiện, nhưng ko có giá trị
        if(sortField==null || sortField.trim().isEmpty())
            sortField = "Name";

        // Tinh chỉnh, chuẩn hóa giá trị của sortDir và sortRev: 
        // xác định chiều sắp xếp:
        if(sortDir == null || sortDir.trim().isEmpty()) 
        {
            sortDir = "asc";
            sortRev = "desc";
        }
        else if (sortDir.equals("asc")) 
        {
            sortRev = "desc";
        }
        else if (sortDir.equals("desc")) 
        {
            sortRev = "asc";
        }
        else 
        { // url có chứa sortDir, nhưng giá trị không đúng, ko phù hợp
            sortDir = "asc";
            sortRev = "desc";
        }

        // Các đường link gửi sang bênview
        // Link sắp xếp theo cột: ten
        String linkSortTen = "/admin/manufacturer/pagedlist?pageNumber=" + pageNumber + "&pageSize="+pageSize + "&sortField=ten&sortDir=" + sortRev;
        // Link gắn vào các nút phân trang, có số trang là đang chờ để lắp ghép bên view
        String linkPage    = "/admin/manufacturer/pagedlist?sortField=" + sortField + "&sortDir=" + sortDir + "&pageSize="+pageSize+"&pageNumber=";

        Page<Manufacturer> page; // biến mô phỏng thông tin trang hiện tại
        List<Manufacturer> list; // danh sách các thực thể sẽ xuất hiện trên trang hiện tại (current page)

        // Khối lệnh thực hiện việc phân trang
        // (đáng lẽ ra phải đóng gói nó trong một hàm của lớp Service)
        {
            // Tiến hành phân trang bảng dữ liệu
            // page = service.listginated(pageNo, pageSize, sortField, sortDir);
            // page = service.getPaged(pageNumber, pageSize, sortField, sortDir);

            // Sắp xếp theo chiều nào, cột nào ?
            // Nếu giá trị của biến sortDirection mà giống với chuỗi "ASC", "asc", "aSc"
            // thì tiến hành sắp xếp tăng,
            // ngược lại thì sắp xếp giảm
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending()
            : Sort.by(sortField).descending();

            // Truyền mẩu tin sắp xếp đấy vào quá trình phân trang
            // chỉ số từ 0 của trang: pageIndex = pageNumber - 1
            // kích thước trang
            // thông tin sắp xếp chứa trong object: sort
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
            page = jpaManufacturer.findAll(pageable);
            list = page.getContent();
        }
        

        // Gửi các thông tin phân trang sang cho View
        // Bên dưới bảng dữ liệu sẽ là Pagination Controls
        model.addAttribute("currentPage", pageNumber); // trang hiện tại
        model.addAttribute("pageNumber", pageNumber); // trang hiện tại
        model.addAttribute("pageSize", pageSize); // kích thước mỗi trang
        model.addAttribute("pageItems", list); // các phần tử (object) trên trang
        // model.addAttribute("page", list.size()); // các phần tử (object) trên trang
        model.addAttribute("pageCount", list.size()); // các phần tử (object) trên trang
        model.addAttribute("totalPages", page.getTotalPages()); // tổng số trang
        model.addAttribute("totalItems", page.getTotalElements()); // tổng số phần tử tìm thấy
        model.addAttribute("totalElements", page.getTotalElements()); // tổng số phần tử tìm thấy

        // Các thông tin cài cắm vào đường link của các nút phân trang
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("sortRev", sortRev);

        // Các thông tin cài cắm vào link cột sắp xếp
        // Chiều sắp xếp sẽ ngược lại so với chiều hiện tại
        model.addAttribute("linkSortTen", linkSortTen);
        model.addAttribute("linkPage", linkPage);

        // Gửi danh sách sang giao diện View HTML
        model.addAttribute("list", list);
        
        // model.addAttribute("ds", service.list());
        // Gửi object sang Modal Form thêm mới hiện ngay trên trang duyệt
        // (ràng buộc new object java vào Modal Form trên giao diện html)
        model.addAttribute("dl", new Manufacturer());
        // Gửi dữ liệu bảng ngoại sang cho thẻ select của Modal Form nhúng vào trang
        // duyet
        // model.addAttribute("ds", this.dvlPhongBan.duyet());

        // Nội dung riêng của trang...
        model.addAttribute("title", "Phân Trang Nhà Sản Xuất"); // duyet.html
        model.addAttribute("content", "manufacturer/pagedlist.html")  ;//phan-trang-bs4.html"); // duyet.html

        // ...được đặt vào bố cục chung của toàn website
        // return "layout.html";
        return "layout/layout-admin.html"; //-bs4.html";
    }

    /**
     * @author T2406E, Fpt Aptech
     * @update 2025.04.02 18h29
     * Hàm chịu trách nhiệm Lọc Tìm / Sắp Xếp / Phân Trang: Nhà Sản Xuất
     * @param model
     * @return
     */
    @GetMapping
    public String getFilteredPagedList(Model model) 
    {
        
        //#region THAM SỐ PHÂN TRANG
        int pageNumber; // current page number, no: số thứ tự của trang hiện tại
        int pageSize; // kích thước của mỗi trang (số phần tử (tối đa)trên mỗi trang).
        // int pageCount; // đếm số phần tử thực tế của trang hiện tại

        // Tiếp nhận các tham số phân trang từ URL, link của máy khách
        try {
            pageNumber = request.getParameter("pageNumber") == null ? 1 :
                Integer.parseInt(request.getParameter("pageNumber"));
            pageSize = Integer.parseInt(request.getParameter("pageSize"));
        }catch(Exception e)
        {
            pageNumber = 1;
            pageSize = 5;
            // @todo: Liệu có đọc được ra từ setting ???
        }

        // Chuẩn hóa triệt để, không bao giờ để tham số phân trang bị null hoặc Zero
        if(pageNumber < 1) pageNumber = 1;
        if(pageSize < 1)   pageSize = 5;
        //#endregion

        //#region THAM SỐ SẮP XẾP TĂNG GIẢM THEO CỘT NÀO
        String sortField; // tên cột sắp xếp
        String sortDir;   // sort direction, chiều sắp xếp: asc, desc
        String sortRev;   // sort reversion, đảo chiều sắp xếp
        // bắt các tham số sắp xếp trên URL
        sortField = request.getParameter("sortField");
        sortDir   = request.getParameter("sortDir");

        // Nếu phía máy khách không chỉ rõ cột sắp xếp
        // thì sử dụng một cột mặc định bất kì nào đấy, thường là cột "tên"
        // 1. là sortField ko xuất hiện trên url params
        // 2. là sortField có xuất hiện, nhưng ko có giá trị
        if(sortField==null || sortField.trim().isEmpty())
            sortField = "Name";

        // Tinh chỉnh, chuẩn hóa giá trị của sortDir và sortRev: 
        // xác định chiều sắp xếp:
        if(sortDir == null || sortDir.trim().isEmpty()) 
        {
            sortDir = "asc";
            sortRev = "desc";
        }
        else if (sortDir.equals("asc")) 
        {
            sortRev = "desc";
        }
        else if (sortDir.equals("desc")) 
        {
            sortRev = "asc";
        }
        else 
        { // url có chứa sortDir, nhưng giá trị không đúng, ko phù hợp
            sortDir = "asc";
            sortRev = "desc";
        }
        //#endregion

        // Các đường link gửi sang bênview
        // Link sắp xếp theo cột: ten
        String linkSortTen = String.format("/admin/manufacturer?pageNumber=%d&pageSize=%d&sortField=ten&sortDir=%s", 
                                    pageNumber, pageSize, sortRev);
        // Link gắn vào các nút phân trang, có số trang là đang chờ để lắp ghép bên view
        String linkPage = String.format("/admin/manufacturer?sortField=%s&sortDir=%s&pageSize=%d&pageNumber=", 
                                    sortField, sortDir, pageSize);

        Page<Manufacturer> page; // biến mô phỏng thông tin trang hiện tại
        List<Manufacturer> list; // danh sách các thực thể sẽ xuất hiện trên trang hiện tại (current page)

        // Khối lệnh thực hiện việc phân trang
        {

            // Sắp xếp theo chiều nào, cột nào ?
            // Nếu giá trị của biến sortDirection mà giống với chuỗi "ASC", "asc", "aSc"
            // thì tiến hành sắp xếp tăng,
            // ngược lại thì sắp xếp giảm
            // org.springframework.data.domain.
            Sort sort = sortDir.equalsIgnoreCase(Sort.Direction.ASC.name()) 
                        ? Sort.by(sortField).ascending()
                        : Sort.by(sortField).descending();

            // Truyền mẩu tin sắp xếp đấy vào quá trình phân trang
            // chỉ số từ 0 của trang: pageIndex = pageNumber - 1
            // kích thước trang
            // thông tin sắp xếp chứa trong object: sort
            // org.springframework.data.domain.
            Pageable pageable = PageRequest.of(pageNumber - 1, pageSize, sort);
            page = jpaManufacturer.findAll(pageable);
            list = page.getContent();
        }
        
        // Gửi các thông tin phân trang sang cho View
        // Bên dưới bảng dữ liệu sẽ là Pagination Controls
        model
        .addAttribute("currentPage", pageNumber) // trang hiện tại
        .addAttribute("pageNumber", pageNumber) // trang hiện tại
        .addAttribute("pageSize", pageSize) // kích thước mỗi trang
        .addAttribute("pageItems", list) // các phần tử (object) trên trang
        // model.addAttribute("page", list.size()); // các phần tử (object) trên trang
        .addAttribute("pageCount", list.size()) // các phần tử (object) trên trang
        .addAttribute("totalPages", page.getTotalPages()) // tổng số trang
        .addAttribute("totalItems", page.getTotalElements()) // tổng số phần tử tìm thấy
        .addAttribute("totalElements", page.getTotalElements()) // tổng số phần tử tìm thấy

        // Các thông tin cài cắm vào đường link của các nút phân trang
        .addAttribute("sortField", sortField)
        .addAttribute("sortDir", sortDir)
        .addAttribute("sortRev", sortRev)

        // Các thông tin cài cắm vào link cột sắp xếp
        // Chiều sắp xếp sẽ ngược lại so với chiều hiện tại
        .addAttribute("linkSortTen", linkSortTen)
        .addAttribute("linkPage", linkPage)

        // Gửi danh sách sang giao diện View HTML
        .addAttribute("list", list)
        
        // model.addAttribute("ds", service.list());
        // Gửi object sang Modal Form thêm mới hiện ngay trên trang duyệt
        // (ràng buộc new object java vào Modal Form trên giao diện html)
        .addAttribute("dl", new Manufacturer())
        // Gửi dữ liệu bảng ngoại sang cho thẻ select của Modal Form nhúng vào trang
        // duyet
        // model.addAttribute("ds", this.dvlPhongBan.duyet());

        // Nội dung riêng của trang...
        .addAttribute("title", "Lọc Tìm Nhà Sản Xuất") 
        .addAttribute("content", "manufacturer/filtered-paged-list.html")  //phan-trang-bs4.html"); // duyet.html
        ;

        // ...được đặt vào bố cục chung của toàn website
        return "layout/layout-admin.html"; 
    }
    
}// end class
