package exam.jsb_webshop_t2406e.domain.category;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
// import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
// import jakarta.persistence.JoinColumn;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import org.springframework.web.multipart.MultipartFile;

@Data @Entity
public class Category
{
    @Id // Khóa chính
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tăng tự động từ 1,2,3,...
    private Integer id;

    private Integer   orderNumber; //thuTu; // Thứ tự sắp xếp tăng dần từ 1,2,3,...
    private Date   dateCreated; //ngayTao; // theo dõi xem bản ghi được tạo khi nào (đặt đơn hàng)
    private Date   dateUpdated; //ngaySua; // theo dõi xem bản ghi bị sửa khi nào (đổi trạng thái đơn hàng)
    private Boolean        active; //choPhep; // Cho phép xuất hiện trên trang Public, Store Front hay không
    
    private String           name; //ten; //tuaDe; //tieuDe
    private String          image; //anh; // địa chỉ tuyệt đối ảnh Online, 
                                   // hoặc địa chỉ bán tuyệt đối trỏ tới ảnh cục bộ
    private String           link; //link;, ví dụ: https://apple.com, https://samsung.com, https://lg.com

    @Column(columnDefinition="LONGTEXT")
    private String    description; //moTa; // html form textarea, jquery plugin, rich text editor (summernote)

    @Transient
    private MultipartFile  mtFile; // Phục vụ việc upload file, ảnh đại diện của thực thể
    // <form method="post" enctype="multipart/form-data" action="/xxx/save">

    // Kết thúc các trường thông tin chung, hay gặp

    // Dưới đây là ví dụ các trường thông tin riêng:
    private Integer        column; //cot; // số cột chia ra các loại con trên MegaMenu
    private Integer       parentId; //loaiCha = 0; // loại cha ???
    private Boolean            top; // được hiển thị trên Menu Top hay không ? IsTop
    private Boolean       featured; //noiBat; // được hiển thị trên Menu Top hay không ? IsFeatured
    
    public String getFeaturedText() 
    {
        if(this.featured==null)
            this.featured = false;

        return this.featured ? "Featured" : "Not Featured";
    }

    // public String getTopVi() 
    // {
    //     return this.top ? "Top" : "Dưới Top";
    // }

    public String getTopText() 
    {
        return this.top ? "Top Menu" : "";
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



    // #endregion



    /**
     * Hàm này là không cần thiết khi làm Java Web Spring Boot
     * bởi vì, HTML Form Validation đã được thực thi bằng JavaScript.
     * Mã này là di sản của: Java Console ORM MySQL
     */
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
