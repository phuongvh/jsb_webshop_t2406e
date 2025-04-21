package exam.jsb_webshop_t2406e.domain.manufacturer;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

// JPA = Java Persistence API
// Thư viện hàm java xử lý bảng tinh_thanh trong MySQL.
// Thư Viện Hàm Truy Vấn & Thao Tác Dữ Liệu Nhà Sản Xuất (manufacturer table) trên MySQL Server
public interface JpaManufacturer 
extends 
JpaRepository<Manufacturer, Integer>
{
    // Query Methods of Spring DATA JPA

    // Tìm các thực thể theo cột 'tên'
    // phục vụ cho hàm kiểm tra xem bản ghi mới có bị trùng tên không ?
    List<Manufacturer> findByName(String name);
    List<Manufacturer> findByNameContainingIgnoreCase(String query);

    // SELECT FROM cau_thu WHERE ten_day_du LIKE '%tu khoa%'

    /**
     * Viết JPQL, JPA Query Method để lọc tìm những nhà sản xuất theo
     * bộ tiêu chí sau:
     * - tên (name)
     * - 
     */
}
