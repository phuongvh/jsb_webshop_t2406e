package exam.jsb_webshop_t2406e.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

// Mã nguồn này ko fix lỗi thực sự bằng 'no-cors'
// @Configuration
// public class CorsConfig {

//     @Bean
//     public CorsFilter corsFilter() {
//         UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//         CorsConfiguration config = new CorsConfiguration();

//         config.setAllowCredentials(true); // Allow cookies and credentials
//         config.addAllowedOrigin("http://localhost:3000"); // Allow this origin
//         config.addAllowedHeader("*"); // Allow all headers
//         config.addAllowedMethod("*"); // Allow all HTTP methods (GET, POST, PUT, DELETE, etc.)

//         source.registerCorsConfiguration("/**", config); // Apply to all endpoints
//         return new CorsFilter(source);
//     }
// }