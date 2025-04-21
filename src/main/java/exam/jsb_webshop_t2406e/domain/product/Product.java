package exam.jsb_webshop_t2406e.domain.product;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.time.format.DateTimeFormatter;

import org.springframework.web.multipart.MultipartFile;

import exam.jsb_webshop_t2406e.domain.category.Category;
import exam.jsb_webshop_t2406e.domain.manufacturer.Manufacturer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import lombok.Data;

// Mất 1 buổi sáng 2 tiếng để làm đủ một nghiệp vụ: thêm, sửa, xóa
@Data @Entity
public class Product
{
    @Id // Khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tăng tự động từ 1,2,3,...
    private Integer id;

    private Integer       orderNumber; //thuTu; // Thứ tự sắp xếp tăng dần từ 1,2,3,...
    private Date dateCreated; //ngayTao; // theo dõi xem bản ghi được tạo khi nào (đặt đơn hàng)
    private Date dateUpdated; //ngaySua; // theo dõi xem bản ghi bị sửa khi nào (đổi trạng thái đơn hàng)
    private Boolean   active; //satus; //choPhep; // Cho phép xuất hiện trên trang Public, Store Front hay không
    
    private String    name; //ten; //tuaDe; //tieuDe
    private String    image; //anh; // địa chỉ tuyệt đối ảnh Online, 
                           // hoặc địa chỉ bán tuyệt đối trỏ tới ảnh cục bộ
    private String    link; //link;

    @Column(columnDefinition="LONGTEXT")
    private String    description; //moTa; // html form textarea, jquery plugin, rich text editor (summernote)

    @Transient
    private MultipartFile mtFile; // Phục vụ việc upload file, ảnh đại diện của thực thể
    // <form method="post" enctype="multipart/form-data" action="/qdl/thucthe/them">

    private String            tag;  // danh sách các từ khóa cách nhau bởi dấu phảy
    private String          model; // là tên sản phẩm cộng với chi tiết về cấu hình
                                // model là nguyên nhân vì sao cần có nút copy
                                // có lẽ chỉ ghi cấu hình phần cứng vào cho phần model thôi
    private float           price; //donGia;
    private Boolean     available; //trangThai=true; // còn hàng, hết hàng, hàng cũ vs hàng mới
    private Date dateExpired; //ngayHetHan;
    private Boolean   bestSelling; //banChay;
    private Boolean      featured; //noiBat;, nổi bật
    
    private int    manufacturerId; //maNhaSanXuat;
    // Ràng buộc dữ liệu: Khóa Ngoại
    // Quan hệ thực thể: Nhiều <--> Một
    // Mỗi sản phẩm phải thuộc về 1 nhà sản xuất
    @ManyToOne @JoinColumn(name="manufacturerId",insertable=false,updatable=false)
    private Manufacturer manufacturer;

    // 2025 Data Design: Một sản phẩm thuộc về 1 loại thôi.
    private int    categoryId;
    @ManyToOne @JoinColumn(name="categoryId",insertable=false,updatable=false)
    private Category category;
    // #endregion


    @PrePersist
    public void prePersist() 
    {
        // Khi mà html form có checkbox thì nó sẽ không gửi dữ liệu về server
        // if (this.getFieldBoolean() == null)
        //     this.setFieldBoolean(false);

        if(this.getId()>0) {
            // Nếu id > 0 thì là bản ghi đã tồn tại trong csdl rồi
        } else {
            // Nếu id = 0 thì là bản ghi mới, chưa có trong csdl
            this.setDateCreated(new java.sql.Date(System.currentTimeMillis()));
        }
        // this.setDateCreated(new java.sql.Date(System.currentTimeMillis()));

        // Khi thêm mới, hay chỉnh sửa thì cũng đều phải lưu lại ngày tháng cập nhật.
        this.setDateUpdated(new java.sql.Date(System.currentTimeMillis()));
    }
    // trả về đơn giá có dấu phảy ngăn cách
    // ví dụ: 1,000,000 $
    public String getPriceText()
    {
        return String.format("%,d", Math.round(this.getPrice()));
    }

    // public String getChoPhepVi() 
    // {
    //     if(this.choPhep==null)
    //         this.choPhep = false;

    //     return this.choPhep ? "Được Phép" : "Bị Cấm";
    // }

    // public String getStatusText() 
    // {
    //     if(this.Status==null)
    //         this.Status = false;

    //     return this.Status ? "Enable" : "Disable";
    // }

    public String getActiveText() 
    {
        if(this.active == null)
            this.active = false;

        return this.active ? "Active" : "Deactive";
    }


    // cách sử dụng bên view;
    // <input type="date" name="ngayTao" id="ngayTao" hidden th:value="${dl.ngayTaoSQL}" readonly/>
    // public String getNgayTaoSQL() 
    // {
    //     return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(this.ngayTao);
    // }

    public String getCreatedAtSQL() 
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.dateCreated);
    }

    public String getDateCreatedVi()
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(this.dateCreated);
    }

    public String getDateUpdatedVi()
    {
        return new SimpleDateFormat("dd/MM/yyyy").format(this.dateCreated);
    }

    // getDateExpired(): ngày hết hạn

    public String getExpiredDateSQL() 
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.dateCreated);
    }


    // public Boolean KhongHopLe() {
    //     var khl = false;

    //     if (this.Ten.length() < 2) {
    //         khl = true;
    //         System.out.print("\n Lỗi->Tên phải từ 2 kí tự trở lên: ");
    //     }

    //     if (this.Ten.length() > 22) {
    //         khl = true;
    //         System.out.print("\n Lỗi->Tên phải không quá 22 kí tự. ");
    //     }

    //     return khl;
    // }

}// end class

