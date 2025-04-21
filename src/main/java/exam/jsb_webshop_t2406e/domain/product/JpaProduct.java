package exam.jsb_webshop_t2406e.domain.product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


public interface JpaProduct extends JpaRepository<Product, Integer>
{
    List<Product> findByFeatured(Boolean featured); // Tìm các sản phẩm nổi bật

    // List<Product> findByFeaturedIgnoreCase(Boolean featured);
    // bổ sung IgnoreCase cũng không ăn thua
    // Caused by: org.hibernate.query.sqm.PathElementException: 
    // Could not resolve attribute 'featured' of 'spring.jsb_web_shop.product.Product'
}
