package exam.jsb_webshop_t2406e.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// Mã viết bởi CoPilot, test vẫn không OK
// Cái này nó là CORS Policy Configuration ở mức Global
// ko cần thiết, bởi vì ở các Controller đã làm riêng specific CORS configuration
// Mã này là CoPilot gợi ý để ReactJS submit Form thành công, nhưng vẫn failed !
// Lớp này làm CORS ở mức toàn cục (global) cho nên nó sẽ gây chết BpiXXXController
// Tốt hơn hết là: làm CORS Policy ở mức Controller, tức là cục bộ ở những chỗ cần làm thì sẽ tốt hơn !!!
// CHÚ Ý: Làm Cors Policy ở mức Global hay hơn
/**
 * The CORS (Cross-Origin Resource Sharing) error occurs because the backend server is not configured to allow requests from your ReactJS frontend (http://localhost:3000). To resolve this issue, you need to configure the backend to handle CORS properly.

Steps to Fix the CORS Issue
1. Configure CORS in the Backend
If you're using Spring Boot as your backend, you can configure CORS globally or for specific endpoints.

Global CORS Configuration
Add a configuration class to enable CORS for all endpoints:
 */
// @Configuration
// public class WebMvcConfig // OK, chạy tốt, đã test 2025.04.25 nhưng tạm thời dùng CorsConfig
// implements 
// WebMvcConfigurer 
// {
//     // @Bean
//     @Override
//     public void addCorsMappings(CorsRegistry registry) 
//     {
//         registry
//         // .addMapping("/**")
//         .addMapping("/api/**")
//         .allowedOrigins("http://localhost:3000") // React app origin
//         .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//         .allowCredentials(true)
//         ;
//     }
// }

/*
 * Lỗi gặp phải do lớp này gây ra:
  : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception 
  [Request processing failed: java.lang.IllegalArgumentException: When allowCredentials is true, 
  allowedOrigins cannot contain the special value "*" since that cannot be set on the 
  "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, 
  list them explicitly or consider using "allowedOriginPatterns" instead.] with root cause

java.lang.IllegalArgumentException: When allowCredentials is true, 
allowedOrigins cannot contain the special value "*" since that cannot be set on the 
"Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, 
list them explicitly or consider using "allowedOriginPatterns" instead.


 */