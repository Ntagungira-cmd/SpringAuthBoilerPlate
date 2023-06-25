package rw.rca.springsecuritytemplate.controllers;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rw.rca.springsecuritytemplate.Enums.ERole;
import rw.rca.springsecuritytemplate.Models.Role;
import rw.rca.springsecuritytemplate.Models.User;
import rw.rca.springsecuritytemplate.Pojos.Request.LoginRequest;
import rw.rca.springsecuritytemplate.Pojos.Request.SignupRequest;
import rw.rca.springsecuritytemplate.Pojos.Response.MessageResponse;
import rw.rca.springsecuritytemplate.Pojos.Response.UserInfoResponse;
import rw.rca.springsecuritytemplate.Repositories.RoleRepository;
import rw.rca.springsecuritytemplate.Repositories.UserRepository;
import rw.rca.springsecuritytemplate.Security.Jwt.JwtUtils;
import rw.rca.springsecuritytemplate.Security.UserDetailsImpl;
import rw.rca.springsecuritytemplate.Services.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
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
    @Autowired
    UserService userService;

    //signup
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        MessageResponse res = userService.createUser(signUpRequest);
        return ResponseEntity.ok(res);
    }

    //login
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateJwtToken(userDetails);

        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, "Bearer "+jwtToken)
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles,
                        jwtToken));
    }
}
