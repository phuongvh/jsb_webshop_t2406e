package exam.jsb_webshop_t2406e.domain.category;

import java.sql.Date;
import java.text.SimpleDateFormat;

import lombok.Data;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
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
    private int id;

    private Integer   orderNumber; //thuTu; // Thứ tự sắp xếp tăng dần từ 1,2,3,...
    //   @DateTimeFormat(pattern = "yyyy-MM-dd") chỉ áp dụng cho LocalDate
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
    private Integer        columns; //cot; // số cột chia ra các loại con trên MegaMenu
    private Integer       parentId; //loaiCha = 0; // loại cha ???
    private Boolean            top; // được hiển thị trên Menu Top hay không ? IsTop
    private Boolean       featured; //noiBat; // được hiển thị trên Menu Top hay không ? IsFeatured

    public Category()
    {
        setDateCreated(new Date(System.currentTimeMillis()));
        setParentId(0);
        setTop(false);
        setFeatured(false);
        setActive(true);

                // this.setImage("https://upload.wikimedia.org/wikipedia/commons/1/14/No_Image_Available.jpg");
        // facebook style:
        // setImage("https://i.pinimg.com/564x/7b/12/2b/7b122bfb0391eea8a55c6b331471b7db.jpg"); 
        // ronaldo style:
        // setImage("https://upload.wikimedia.org/wikipedia/commons/d/d7/Cristiano_Ronaldo_playing_for_Al_Nassr_FC_against_Persepolis%2C_September_2023_%28cropped%29.jpg"); // facebook style
        // football player icon style:
        // setImage("https://static.vecteezy.com/system/resources/previews/017/558/182/non_2x/soccer-and-football-player-logo-design-dribbling-ball-logo-icon-design-vector.jpg"); // facebook style
        // setImage("https://cdn-icons-png.flaticon.com/512/149/149071.png"); // Empty Profile, not Emtpy Image

    }

    // Hàm này không bị gọi khi lưu entity cũ
    @PrePersist
    public void prePersist() 
    {
        this.setDateCreated(new Date(System.currentTimeMillis()));

        System.out.println("Đã chuẩn hóa dữ liệu trước khi lưu vào db");
    }
    
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

    public String getTopIcon() 
    {
        return this.top ? "icon check circle green" : "";
    }

    public String getFeaturedIcon() 
    {
        return this.top ? "icon star circle green" : "";
    }

    // cách sử dụng bên view;
    // <input type="date" name="ngayTao" id="ngayTao" hidden th:value="${dl.ngayTaoSQL}" readonly/>
    public String getCreatedAtSQL() 
    {
        return new SimpleDateFormat("yyyy-MM-dd").format(this.dateCreated);
    }


    public String getDateCreatedVi()
    {
        if(this.dateCreated==null)
            return "NULL";
        return new SimpleDateFormat("dd/MM/yyyy").format(this.dateCreated);
    }

    public String getDateUpdatedVi()
    {
        if(this.dateUpdated==null)
            return "NULL";
        return new SimpleDateFormat("dd/MM/yyyy").format(this.dateUpdated);
    }



    // #endregion





}// end class
