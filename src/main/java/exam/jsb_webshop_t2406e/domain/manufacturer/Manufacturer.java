package exam.jsb_webshop_t2406e.domain.manufacturer;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.springframework.web.multipart.MultipartFile;

// JPA = Java Persistence API 9+
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Data;
// Tạo tự động các phương thức getter, setter, toString, equals, hashCode

/** tại sao lại không phải cài thư viện com.fasterxml.jackson trong maven
 vì nó đã có sẵn trong SpringBoot
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL) // Loại bỏ các thuộc tính có giá trị null ra khỏi JSON
@Data @Entity
public class Manufacturer 
{
    // Tránh đặt tên trường thông tin trùng từ khóa của SQL
    // Ví dụ: order, group, user, like, by, where...
    @Id // Khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tăng tự động từ 1,2,3,...
    private Integer           id; // mã định danh
    private Integer  orderNumber; // // Thứ tự sắp xếp tăng dần từ 1,2,3,...

    // @DateTimeFormat(pattern = "yyyy-MM-dd") // Liệu có phải dùng hay không ???
    private Date     dateCreated; // theo dõi xem bản ghi được tạo khi nào (đặt đơn hàng)
    private Date     dateUpdated; // theo dõi xem bản ghi bị sửa khi nào (đổi trạng thái đơn hàng)
    private Boolean       active; // Cho phép xuất hiện trên trang Public, Store Front hay không
    
    private String          name; //ten; //tuaDe; //tieuDe
    private String         image; // địa chỉ tuyệt đối ảnh Online, 
                                  // hoặc địa chỉ bán tuyệt đối trỏ tới ảnh nằm cục bộ trên server đang chạy ứng dụng
    private String          link; // địa chỉ internet của nhà sản xuất

    @Column(columnDefinition="LONGTEXT")
    private String   description; //moTa; // html form textarea, jquery plugin, rich text editor (summernote)

    @Transient // Loại bỏ trường này trong json trả về cho phía máy khách ?
    private MultipartFile mtFile; // Phục vụ việc upload file, ảnh đại diện của thực thể

    // todo: getActiveVi() nếu cần
    public String getActiveText() 
    {
        if(this.active==null)
            this.active = false;

        return this.active ? "Active" : "Deactivated";
    }

        
    /**
     * Viết hàm get() để lấy dữ liệu, thông tin phái sinh từ dữ liệu gốc.
     * cách sử dụng bên view;
     * <input type="date" name="dateCreated" id="dateCreated" hidden th:value="${entity.dateCreated}" readonly/>
    // public String getNgayTaoSQL() 
    // {
        // return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(this.ngayTao);
    // }
     * @return
     */
    public String getDateCreatedVi()
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(this.dateCreated);
    }

    public String getDateUpdatedVi()
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(this.dateCreated);
    }
    
}
