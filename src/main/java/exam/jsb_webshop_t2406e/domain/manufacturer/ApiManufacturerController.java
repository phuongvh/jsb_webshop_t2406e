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
// @CrossOrigin(origins = "http://localhost:3000")
// @CrossOrigin(origins = "*", maxAge = 3600)
// @CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials="true")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
// chấp nhận tên miền khác của máy khách gửi yêu cầu
// serverCuaTui.com -> serverCuaBan.com
@RequestMapping("/api/manufacturer") // http://leminhhoa.com/api5/Manufacturer
@RestController
public class ApiManufacturerController
{
    @Autowired
    private JpaManufacturer jpaManufacturer;

    //CRUDS: Create, Read, Update, Search(searchById, searchByText, searchByFilter, searchByCriteria)

    @GetMapping("/{id}") // http://quanlyManufacturer/api5/Manufacturer/5
    public ResponseEntity<Manufacturer> ReadById(@PathVariable("id") int id)
    {

        var Manufacturer = jpaManufacturer.findById(id).orElse(null);
        if(Manufacturer==null)
        {
            System.out.println("Không tìm thấy cầu thủ này !");
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<Manufacturer>(Manufacturer,HttpStatus.OK); // 200
    }
    
    @CrossOrigin(origins = "http://localhost:3000", allowCredentials="true")
    @PostMapping
    // public ResponseEntity<String> Create(@RequestBody Manufacturer Manufacturer, UriComponentsBuilder uricBuilder) 
    public ResponseEntity<Manufacturer> Create(
        @RequestBody 
        Manufacturer Manufacturer
    ) 
    {
        System.out.println("Đang thêm cầu thủ mới...");

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

        // Lưu dữ liệu mới vào csdl MySQL
        jpaManufacturer.save(Manufacturer);
        

        // Trả về dữ liệu json cho phía máy khách
        // HttpHeaders headers = new HttpHeaders();
        // headers.setLocation(uricBuilder.path("/api/manufacturer/{id}").buildAndExpand(Manufacturer.getId()).toUri());
        // return new ResponseEntity<String>(headers, HttpStatus.CREATED); // 201 // gây chết ReactJS
        // return new ResponseEntity<String>(HttpStatus.CREATED); // 201
        return new ResponseEntity<Manufacturer>(HttpStatus.CREATED); // 201
    }

    /**
     * GET http://localhost:6868/api/manufacturer
     * {
            "path": "/api/manufacturer",
            "error": "Unauthorized",
            "message": "Full authentication is required to access this resource",
            "status": 401
        }
     * @return
     */
    @GetMapping
    public ResponseEntity<List<Manufacturer>> Read() 
    {
        System.out.println("Đang duyệt danh sách, để trả về json...");
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
    public ResponseEntity<Manufacturer> Update(
        @RequestBody Manufacturer Manufacturer
    ) 
    {
        // System.out.println("Đang sửa cầu thủ có mã số: " + id);

        // Manufacturer.setId(id); // đảm bảo là không thêm mới !
        if(Manufacturer==null){
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404
        }   

        var dl = jpaManufacturer.findById(Manufacturer.getId()).orElse(null);
        if(dl==null){
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404
        }

        // @todo Kiểm tra xem có bị trùng tên cầu thủ cũ trên hệ thống ?

        // @todo Kiểm tra xem dữ liệu có hợp lệ không ?

        // Lưu dữ liệu mới vào csdl MySQL
        jpaManufacturer.save(Manufacturer);

        return new ResponseEntity<Manufacturer>(HttpStatus.OK); // 200
    }

    /*
     * Đã chạy thành công !
     * Chứng tỏ là phương thức Http DELTE có chấp nhận RequestBody
     */
    @DeleteMapping
    public ResponseEntity<Manufacturer> Delete(@RequestBody Manufacturer Manufacturer) 
    {
        // System.out.println("Đang xóa cầu thủ có mã số: " + id);

         // Manufacturer.setId(id); // đảm bảo là không thêm mới !
         if(Manufacturer==null){
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404
        }   

        var dl = jpaManufacturer.findById(Manufacturer.getId()).orElse(null);
        if(dl==null){
            return new ResponseEntity<Manufacturer>(HttpStatus.NOT_FOUND); // 404
        }

        // Xóa dữ liệu trong csdl MySQL
        jpaManufacturer.deleteById(Manufacturer.getId());

        return new ResponseEntity<Manufacturer>(HttpStatus.OK); // 200

    }// hàm đã hoạt động tốt !!!
    
}// end ApiController
