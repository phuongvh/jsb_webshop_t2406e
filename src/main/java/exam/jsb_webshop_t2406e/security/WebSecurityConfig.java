package exam.jsb_webshop_t2406e.security;

// package com.bezkoder.spring.login.security;

import java.net.http.HttpRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import exam.jsb_webshop_t2406e.security.jwt.AuthEntryPointJwt;
import exam.jsb_webshop_t2406e.security.jwt.AuthTokenFilter;
import exam.jsb_webshop_t2406e.security.services.UserDetailsServiceImpl;

// Spring 6 Boot 3
@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
// @Order(1)
public class WebSecurityConfig 
{ 
  
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter();
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
       
      authProvider.setUserDetailsService(userDetailsService);
      authProvider.setPasswordEncoder(passwordEncoder());
   
      return authProvider;
  }
  
  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  
  @Bean
  @Order(0)
  // https://github.com/spring-projects/spring-security/issues/12950
  // đọc để hiểu cú pháp của SecurityFilterChain, cách SpringBoot phân quyền cho url như nào
  public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception 
  {
    http.securityMatcher("/api/**")
        .csrf(csrf -> csrf.disable())
        .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> 
          auth.requestMatchers("/api/auth/**").permitAll()
              .requestMatchers(HttpMethod.GET, "/api/test/**").permitAll()
              .anyRequest().authenticated()
        )
        ;
    
    http.authenticationProvider(authenticationProvider());
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  //   @Bean
  // @Order(XXX)
  // SecurityFilterChain resourcesFilterChain(HttpSecurity http) throws Exception 
  // {
  //     http.securityMatcher("/resources/**")
  //         .authorizeHttpRequests( auth -> {
  //           auth
  //               .requestMatchers("/resources/**").permitAll()
  //           ;
  //         })
          
          
  //     ;
  //     return http.build();
  // }

  // @Bean
  // @Order(YYY)
  // SecurityFilterChain staticFilterChain(HttpSecurity http) throws Exception 
  // {
  //     http.securityMatcher("/favicon.ico")
  //         .authorizeHttpRequests( auth -> {
  //           auth
  //               .requestMatchers("/favicon.ico").permitAll()
  //           ;
  //         })
          
          
  //     ;
  //     return http.build();
  // }

  @Bean
  @Order(1)
  SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception 
  {
      http.securityMatcher("/admin/**")
          .authorizeHttpRequests( auth -> auth
                .requestMatchers("/admin/loginform").permitAll()
                .requestMatchers("/admin/logout").permitAll()
                .requestMatchers("/admin/**").authenticated()
            
          )
          // tùy biến form đăng nhập cho ai muốn vào khu vực /admin/xxx (ví dụ: http://localhost:8080/admin/bimat)
          // đăng nhập xong sẽ quay lại trang mà người dùng cố ý truy cập
          // Nếu cố tình gõ vào đường dẫn /admin/loginform thì sẽ 
          // điều hướng đến /admin/test sau khi đăng nhập thành công.
          // .formLogin(form->form.loginPage("/admin/login") // OK, khi mà nó chỉ trả về ResponseBody Text
          .formLogin(form->form.loginPage("/admin/loginform") // dễ failed do lỗi forward .html
                               .defaultSuccessUrl("/admin/test") 
                               .permitAll()
          )
          
      ;
      return http.build();
  } 
  // OK, đã điều hướng được /admin/test vào /admin/login form

    // @todo: Nếu khách hàng truy cập vào trang tài khoản khách hàng (Customer Account) thì phải đăng nhập
    // Nếu dùng chung bảng User, thì thử sử dụng chung cơ chế đăng nhập, đăng thoát giống admin xem sao.
    // Nếu Customer đăng nhập bằng email, thì có thể sử dụng cơ chế đăng nhập riêng cho Customer
    // Không chung chạ với User, Admin
    // http://localhost:8080/customer/login
    // http://localhost:8080/customer/logout
    // http://localhost:8080/customer/register
    // http://localhost:8080/customer/account

  @Bean
  @Order(2)
  SecurityFilterChain customerFilterChain(HttpSecurity http) throws Exception 
  {
      // Muốn url nào truy cập mà không cần tài khoản, thì phải gọi hàm permitAll() tường minh !!!
      // Không gọi là Spring Security sẽ chặn và bắt đăng nhập.
      http.securityMatcher("/customer/**")
          .authorizeHttpRequests( auth -> auth
                .requestMatchers("/customer/test").permitAll()
                // .requestMatchers("/customer/public").permitAll() // bị authenticated, mặc dù đã comment
                .requestMatchers("/customer/login").permitAll()
                // .requestMatchers("/customer/loginpost").permitAll()
                // .requestMatchers(HttpMethod.POST, "/customer/loginpost").permitAll() // vẫn failed
                .requestMatchers("/customer/logout").permitAll()
                // .requestMatchers("/customer/**").authenticated() // bắt chước admin
                // .requestMatchers("/customer/bimat").authenticated() // không ăn thua, phải permitAll() tường minh
                .requestMatchers("/customer/**").authenticated()
            
          )
          .formLogin(form->form
              .loginPage("/customer/login") // Hiển thị thành công form
              .defaultSuccessUrl("/customer/test") 
              .permitAll()
          )
          
      ;
      return http.build();
  } 

  
}// end class

