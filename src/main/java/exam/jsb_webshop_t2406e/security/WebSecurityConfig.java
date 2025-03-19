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
// Spring xxx Boot 2.7
@Configuration
//@EnableWebSecurity
@EnableMethodSecurity
//(securedEnabled = true,
//jsr250Enabled = true,
//prePostEnabled = true) // by default
@Order(1)
public class WebSecurityConfig { // extends WebSecurityConfigurerAdapter {
  
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

//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.cors().and().csrf().disable()
//      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//      .authorizeRequests().antMatchers("/api/auth/**").permitAll()
//      .antMatchers("/api/test/**").permitAll()
//      .anyRequest().authenticated();
//
//    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//  }
  
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

  @Bean
  @Order(3)
  SecurityFilterChain adminFilterChain(HttpSecurity http) throws Exception 
  {
      http.securityMatcher("/admin/**")
          .authorizeHttpRequests( auth -> {
            auth.requestMatchers("/admin/**").authenticated()
                // .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/admin/loginform").permitAll()
                .requestMatchers("/admin/logout").permitAll()
            ;
          })
          // tùy biến form đăng nhập cho ai muốn vào khu vực /admin/xxx (ví dụ: http://localhost:8080/admin/bimat)
          // đăng nhập xong sẽ quay lại trang mà người dùng cố ý truy cập
          // Nếu cố tình gõ vào đường dẫn /admin/loginform thì sẽ 
          // điều hướng đến /admin/test sau khi đăng nhập thành công.
          .formLogin(form->form.loginPage("/admin/loginform")
                               .defaultSuccessUrl("/admin/test") 
                               .permitAll()
          )
          
      ;
      return http.build();
  } // OK, đã điều hướng được /admin/test vào /admin/login form
    // https://stackoverflow.com/questions/73666236/default-login-form-for-secured-methodes-in-spring-security
    // https://stackoverflow.com/questions/77207127/spring-security-err-too-many-redirects-form-login
    // https://stackoverflow.com/questions/76206733/spring-boot-3-form-login-cannot-complete-the-process
    // https://github.com/spring-projects/spring-security/issues/13285
    // https://stackoverflow.com/questions/55829669/how-to-call-controller-post-method-with-custom-spring-boot-login-form

    // @todo: Nếu khách hàng truy cập vào trang tài khoản khách hàng (Customer Account) thì phải đăng nhập
    // Nếu dùng chung bảng User, thì thử sử dụng chung cơ chế đăng nhập, đăng thoát giống admin xem sao.
    // http://localhost:8080/customer/login
    // http://localhost:8080/customer/logout
    // http://localhost:8080/customer/register
    // http://localhost:8080/customer/account

    // THỰC RA THÌ CHỈ CẦN TẠO SecurityFilterChain CHO CÁC MODULE CẦN SỰ KIỂM SOÁT
    // TRUY CẬP THÔI, chứ khu vực nào để tự do ra vào thì chả cần !!!
    // https://github.com/spring-projects/spring-security/issues/15220

    // Các trang /home /about /contact ...phải để vào tự do.
    // Trang /customer/login và customer/logout phải để tự do (customer authentication sẽ làm riêng, không chung chạ với admin)
  // @Bean
  // @Order(2)
  // public SecurityFilterChain appFilterChain(HttpSecurity http) throws Exception {
  //   http.securityMatcher("/app/**")
  //       .authorizeHttpRequests(
  //         auth -> auth.requestMatchers("/app/**").permitAll()
  //       )
        
  //   ;

  //   return http.build();
  // } // đã chạy OK, truy cập được vào http://localhost:8080/app/test
  //   // permitAll() cẩn thận, ko có nó chặn cả trang thymeleaf html

  // bài viết này hay:
  // https://github.com/spring-projects/spring-security/issues/15220
  // Nó giải thích vì sao mình cứ bị cái vòng luần quẩn !!! lỗi khi làm multiple filterChain cho ApiController và MvcController, AdminController

  // Tham Khảo Thêm:
  
  // @Order(0)
  // @Bean
  // SecurityFilterChain managementOnlySecurityFilterChain(HttpSecurity http) throws Exception {
  //       http.securityMatcher("/actuator/**", "/startup-report/**")
  //           .authorizeHttpRequests(auth -> {
  //             auth.requestMatchers(mvc.pattern(HEALTH_ENDPOINT)).permitAll();
  //             // ...
  //             auth.anyRequest().authenticated();
  //           });
  // @Bean
  // @Order(Ordered.HIGHEST_PRECEDENCE)
  // SecurityFilterChain permitAllSecurityFilterChain(HttpSecurity http) throws Exception {
  //     http.authorizeHttpRequests(auth -> {
  //       auth.requestMatchers("/*/authenticationRequired").authenticated();
  //       auth.anyRequest().permitAll();
  //     });
  // }
  
}// end class

