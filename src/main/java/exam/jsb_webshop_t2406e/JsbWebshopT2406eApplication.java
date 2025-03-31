package exam.jsb_webshop_t2406e;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class JsbWebshopT2406eApplication {

	public static void main(String[] args) {
		SpringApplication.run(JsbWebshopT2406eApplication.class, args);
	}

	// https://spring.io/guides/gs/rest-service-cors
	// https://stackoverflow.com/questions/49049312/cors-error-when-connecting-local-react-frontend-to-local-spring-boot-middleware
	// CORS error when connecting local React frontend to local Spring Boot middleware application
	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
				                .allowedOrigins("http://localhost:3000") // React app origin
				                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
				                // .allowCredentials(true)
								;
			}
		};
	}

}
