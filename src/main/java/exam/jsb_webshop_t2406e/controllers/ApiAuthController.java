package exam.jsb_webshop_t2406e.controllers;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import exam.jsb_webshop_t2406e.models.user.ERole;
import exam.jsb_webshop_t2406e.models.user.Role;
import exam.jsb_webshop_t2406e.models.user.RoleRepository;
import exam.jsb_webshop_t2406e.models.user.User;
import exam.jsb_webshop_t2406e.models.user.UserRepository;
import exam.jsb_webshop_t2406e.payload.request.LoginRequest;
import exam.jsb_webshop_t2406e.payload.request.SignupRequest;
import exam.jsb_webshop_t2406e.payload.response.MessageResponse;
import exam.jsb_webshop_t2406e.payload.response.UserInfoResponse;
import exam.jsb_webshop_t2406e.security.jwt.JwtUtils;
import exam.jsb_webshop_t2406e.security.services.UserDetailsImpl;

//for Angular Client (withCredentials)
//@CrossOrigin(origins = "http://localhost:8081", maxAge = 3600, allowCredentials="true")
// https://stackoverflow.com/questions/34817617/should-jwt-be-stored-in-localstorage-or-cookie
// https://steemit.com/utopian-io/@alfarisi94/consuming-jwt-in-client-side-with-jquery
// https://security.stackexchange.com/questions/130548/should-jwt-token-be-stored-in-a-cookie-header-or-body
// @CrossOrigin(origins = "*", maxAge = 3600)
@CrossOrigin(origins = "http://localhost:3000", allowCredentials="true") // for ReactJS credentials included
@RestController
@RequestMapping("/api/auth") // test thử cơ chế bắt lỗi: http://localhost:6868/api/xxxyyy
public class ApiAuthController 
{
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager
        .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);

    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

    ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    // https://www.cyberchief.ai/2023/05/secure-jwt-token-storage.html
    // Set Cookie ở phía backend để FE ReactJS có thể nhận được !!!
    return ResponseEntity.ok()
                        .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                        .body(new UserInfoResponse(userDetails.getId(),
                                   userDetails.getUsername(),
                                   userDetails.getEmail(),
                                   roles)
                        );

    // localStorage: trả về json có chứa JWT
    // https://viblo.asia/p/huong-dan-spring-security-jwt-json-web-token-hibernate-oOVlYGmoK8W
    // return "
    //   {
    //     jwtStr: .....,
    //     ủe

    //   }
    // ";
  }

  /*
   * Đănng ký mới tài khoản
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new User(signUpRequest.getUsername(),
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
      roles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
          Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(adminRole);

          break;
        case "mod":
          Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(modRole);

          break;
        default:
          Role userRole = roleRepository.findByName(ERole.ROLE_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
          roles.add(userRole);
        }
      });
    }

    user.setRoles(roles);
    userRepository.save(user);

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }

  @PostMapping("/signout")
  public ResponseEntity<?> logoutUser() {
    ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
    return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
        .body(new MessageResponse("You've been signed out!"));
  }
}
