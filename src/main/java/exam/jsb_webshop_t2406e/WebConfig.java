package exam.jsb_webshop_t2406e;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.web.servlet.config.annotation.CorsRegistry;
// import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// // Mã viết bởi CoPilot, test vẫn không OK
// Cái này nó là CORS Policy Configuration ở mức Global
// ko cần thiết, bởi vì ở các Controller đã làm riêng specific CORS configuration
// @Configuration
// public class WebConfig implements WebMvcConfigurer {
//     // @Bean
//     @Override
//     public void addCorsMappings(CorsRegistry registry) {
//         registry.addMapping("/**")
//                 .allowedOrigins("http://localhost:3000") // React app origin
//                 .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
//                 .allowCredentials(true);
//     }
// }
