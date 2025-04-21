package exam.jsb_webshop_t2406e;

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
// @Configuration
// public class WebMvcConfig implements WebMvcConfigurer 
// {
//     // @Bean
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("http://localhost:3000") // React app origin
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                 .allowCredentials(true);
//     }
// }

/*
 * Lỗi gặp phải do lớp này gây ra:
  : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: java.lang.IllegalArgumentException: When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.] with root cause

java.lang.IllegalArgumentException: When allowCredentials is true, allowedOrigins cannot contain the special value "*" since that cannot be set on the "Access-Control-Allow-Origin" response header. To allow credentials to a set of origins, list them explicitly or consider using "allowedOriginPatterns" instead.


 */