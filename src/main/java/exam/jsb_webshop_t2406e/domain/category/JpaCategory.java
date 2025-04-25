package exam.jsb_webshop_t2406e.domain.category;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// Giao diện hàm chức năng quản lý Loại (sản phẩm)
public interface JpaCategory extends JpaRepository<Category, Integer>
{
        // Tìm các thực thể theo tenDayDu, mà có chứa từ khóa 'xxx' (không phân biệt hoa thường)
    // AI CoPilot: spring jpa query method findByTenDayDuContaining ignore case
    List<Category> findByNameContainingIgnoreCase(String ten); // ok
}