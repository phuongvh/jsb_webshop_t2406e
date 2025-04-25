package exam.jsb_webshop_t2406e.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// Mã nguồn này do AI CoPilot gợi ý.
/**
 Lỗi 415 Unsupported Media Type xảy ra khi máy chủ (Java Spring Boot backend) 
 không thể xử lý kiểu dữ liệu (Content-Type) mà client (ReactJS) gửi trong yêu cầu HTTP. Điều này thường xảy ra khi:

Content-Type không khớp với dữ liệu được gửi:

Backend mong đợi dữ liệu ở định dạng application/json, nhưng client không gửi đúng định dạng hoặc không đặt tiêu đề Content-Type chính xác.
Backend không hỗ trợ kiểu dữ liệu được gửi:

Backend chưa được cấu hình để xử lý application/json.

Cách khắc phục
1. Đảm bảo Content-Type là application/json
Khi gửi dữ liệu từ ReactJS, bạn cần đặt tiêu đề Content-Type là application/json để backend biết rằng dữ liệu được gửi ở định dạng JSON.

fetch("http://localhost:6868/api/manufacturer", {
    method: "POST",
    headers: {
        "Content-Type": "application/json", // Đặt Content-Type
    },
    credentials: "include", // Nếu cần gửi cookie
    body: JSON.stringify(formData), // Chuyển đổi dữ liệu thành JSON
})

2. Cấu hình Backend để hỗ trợ application/json
Đảm bảo rằng backend Spring Boot được cấu hình để xử lý dữ liệu JSON.

Kiểm tra Controller:
Đảm bảo rằng phương thức trong Controller có chú thích @RequestBody để nhận dữ liệu JSON.

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manufacturer")
public class ManufacturerController {

    @PostMapping
    public ResponseEntity<Manufacturer> addManufacturer(@RequestBody Manufacturer manufacturer) {
        // Xử lý logic thêm Manufacturer
        System.out.println("Received Manufacturer: " + manufacturer);
        return ResponseEntity.ok(manufacturer);
    }
}

Kiểm tra Entity:
Đảm bảo rằng lớp Manufacturer được ánh xạ chính xác với dữ liệu JSON.

3. Kiểm tra dữ liệu gửi từ ReactJS
Đảm bảo rằng dữ liệu được gửi từ ReactJS là JSON hợp lệ.
Ví dụ, formData phải là một đối tượng JSON:

const formData = {
    name: "T2307A",
    orderNumber: 0,
    active: true,
    image: "http://anh.com/t2307a.jpg",
    link: "http://t2307a.company.com",
    description: "Công ty gia đình",
};

5. Xử lý CORS (nếu cần)
Nếu backend và frontend chạy trên các domain khác nhau (ví dụ: localhost:6868 và localhost:3000), 
bạn cần cấu hình CORS trong Spring Boot:


The CORS (Cross-Origin Resource Sharing) error occurs because the backend server is not configured to allow requests from your ReactJS frontend (http://localhost:3000). To resolve this issue, you need to configure the backend to handle CORS properly.

Steps to Fix the CORS Issue
1. Configure CORS in the Backend
If you're using Spring Boot as your backend, you can configure CORS globally or for specific endpoints.

Global CORS Configuration
Add a configuration class to enable CORS for all endpoints:

*/
@Configuration  // CORS Config for the whole Web App (globally not locally on each controller)
public class CorsConfig // OK, chạy tốt, đã test 2025.04.25
{

    @Bean
    public CorsFilter corsFilter()
    {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // Allow cookies and credentials
        config.addAllowedOrigin("http://localhost:3000"); // Allow this origin
        config.addAllowedHeader("*"); // Allow all headers
        config.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)

        // source.registerCorsConfiguration("/**", config); // Apply to all endpoints
        source.registerCorsConfiguration("/api/**", config); // Apply to all endpoints
        return new CorsFilter(source);
    }

}