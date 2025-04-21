package exam.jsb_webshop_t2406e.domain.manufacturer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;


// @todo: How to secured Api Web Services
// Client phải có username, password, secret key thì mới được
// gọi hàm dịch vụ của tôi
// secret token, secret key: mã bí mật (mã hóa thông tin tài khoản người dùng)
// JWT: Json Web Token

// @CrossOrigin(origins="*") 
@CrossOrigin(origins = "*", maxAge = 3600) // để jQuery.ajax(), window.fetch() có thể gọi được.
// chấp nhận tên miền khác của máy khách gửi yêu cầu
// serverCuaTui.com -> serverCuaBan.com
@RequestMapping("/ws/manufacturer") // http://localhost:6868/ws/manufacturer
@RestController
public class WsManufacturerController
{
    @Autowired
    private JpaManufacturer jpaManufacturer;

    //CRUDS: Create, Read, Update, Search(searchById, searchByText, searchByFilter, searchByCriteria)


    @GetMapping("/{id}") // http://localhost:6868/bpi/manufacturer/5
    public ResponseEntity<Manufacturer> ReadById(@PathVariable("id") int id)
    {

        var entity = jpaManufacturer.findById(id).orElse(null);
        if(entity==null)
        {
            System.out.
            println("Không tìm thấy nhà sản xuất này !");

            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Manufacturer>(entity,HttpStatus.OK); // 200
    }
    
    /**
     * Question: Tại sao lại có lỗi này ?
     /fix Could not resolve parameter [0] in public org.springframework.http.ResponseEntity<exam.jsb_webshop_t2406e.domain.manufacturer.Manufacturer> exam.jsb_webshop_t2406e.domain.manufacturer.ApiManufacturerController.Create(exam.jsb_webshop_t2406e.domain.manufacturer.Manufacturer): Content-Type 'text/plain;charset=UTF-8' is not supported

        CoPilot Answer:
     The issue is that the @RequestBody annotation expects the incoming request to have a Content-Type of application/json. To fix this, ensure that the client sends the correct Content-Type header, and also add @RequestMapping with consumes = "application/json" to explicitly specify the expected content type.
     */
    // @PostMapping(consumes = "application/json")
    public ResponseEntity<Manufacturer> Create(@RequestBody Manufacturer manufacturer) 
    {
        System.out.
        println("Đang thêm cầu thủ mới...");

        // @todo Kiểm tra xem dữ liệu có hợp lệ không ?
        // if(Manufacturer.KhongHopLe())
            // errorMessages += String.join("; ", Manufacturer.getMessages());

        // if(errorMessages.length() > 0)
        // {
        //     System.out.print(errorMessages); // In lỗi ra màn hình Terminal cho lập trình viên biết
        //     return new ResponseEntity<String>(errorMessages, HttpStatus.BAD_REQUEST); // In lỗi ra màn hình RestMAN cho khách biết
        // }
            

        // Nếu Manufacturer.getMaManufacturer() mà không tồn tại trong bảng phụ (ngoại
        // java.sql.SQLIntegrityConstraintViolationException:
        // if( !jpaManufacturer.existsById(Manufacturer.getMaManufacturer()) ){
        //     return new ResponseEntity<String>("Lỗi: Mã tỉnh thành không tồn tại !", HttpStatus.BAD_REQUEST);
        // }

        // Cập nhật ngày tạo, ngày sửa bản ghi
        manufacturer.setDateCreated(new java.sql.Date(System.currentTimeMillis()));
        manufacturer.setDateUpdated(new java.sql.Date(System.currentTimeMillis()));
        // Lưu dữ liệu mới vào csdl MySQL
        jpaManufacturer.save(manufacturer);
        

        // Trả về dữ liệu json cho phía máy khách
        // HttpHeaders headers = new HttpHeaders();
        // headers.setLocation(uricBuilder.path("/api/manufacturer/{id}").buildAndExpand(Manufacturer.getId()).toUri());
        // return new ResponseEntity<String>(headers, HttpStatus.CREATED); // 201 // gây chết ReactJS
        // return new ResponseEntity<String>(HttpStatus.CREATED); // 201
        return new ResponseEntity<Manufacturer>(HttpStatus.CREATED); // 201
    }

    /**
     * GET http://localhost:6868/bpi/manufacturer
     * {
            "path": "/api/manufacturer",
            "error": "Unauthorized",
            "message": "Full authentication is required to access this resource",
            "status": 401
        }
     */
    @GetMapping
    public ResponseEntity<List<Manufacturer>> Read() 
    {
        System.out.
        println("Đang duyệt danh sách, để trả về json...");
        // Đọc dữ liệu bảng, rồi chứa vào biến tạm
        List<Manufacturer> list = jpaManufacturer.findAll();

        // Kiểm tra xem danh sách có dữ liệu hay không
        if(list==null || list.isEmpty())
        {
            return new ResponseEntity<List<Manufacturer>>(HttpStatus.NO_CONTENT);
        }

        // Trả về dữ liệu json cho phía máy khách
        return new ResponseEntity<List<Manufacturer>>(list, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<Manufacturer> Update(@RequestBody Manufacturer entity) 
    {
        // System.out.println("Đang sửa cầu thủ có mã số: " + id);

        // Manufacturer.setId(id); // đảm bảo là không thêm mới !
        if(entity==null)
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404
        

        var oldEntity = jpaManufacturer.findById(entity.getId()).orElse(null);
        if(oldEntity==null)
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404
        

        // @todo Kiểm tra xem có bị trùng tên cầu thủ cũ trên hệ thống ?

        // @todo Kiểm tra xem dữ liệu có hợp lệ không ?

        // Lưu dữ liệu mới vào csdl MySQL
        jpaManufacturer.save(entity);

        return new ResponseEntity<Manufacturer>(HttpStatus.OK); // 200
    }

    /*
     * Đã chạy thành công !
     * Chứng tỏ là phương thức Http DELTE có chấp nhận RequestBody
     */
    @DeleteMapping
    public ResponseEntity<Manufacturer> Delete(@RequestBody Manufacturer entity) 
    {
        // System.out.println("Đang xóa cầu thủ có mã số: " + id);

         // Manufacturer.setId(id); // đảm bảo là không thêm mới !
         if(entity==null)
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404

        var oldEntity = jpaManufacturer.findById(entity.getId()).get();
        if(oldEntity==null)
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404
        

        // Xóa dữ liệu trong csdl MySQL
        jpaManufacturer.deleteById(entity.getId());

        return new ResponseEntity<Manufacturer>(HttpStatus.OK); // 200

    }// hàm đã hoạt động tốt !!!
    
}// end BpiController
