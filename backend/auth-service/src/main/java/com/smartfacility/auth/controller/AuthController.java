package com.smartfacility.auth.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.smartfacility.auth.entity.Organization;
import com.smartfacility.auth.entity.Tenant;
import com.smartfacility.auth.entity.User;
import com.smartfacility.auth.jwt.JwtUtil;
import com.smartfacility.auth.repository.OrganizationRepository;
import com.smartfacility.auth.repository.TenantRepository;
import com.smartfacility.auth.repository.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository repo;
    
    @Autowired
    private OrganizationRepository orgRepo;
    
    @Autowired
    private TenantRepository tenantRepo;
    
    @Autowired
    private JavaMailSender mailSender;

    BCryptPasswordEncoder encoder =
        new BCryptPasswordEncoder();

    // NORMAL REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody User user
    ) {

        user.setPassword(
            encoder.encode(user.getPassword())
        );

        if(user.getRole() == null)
            user.setRole("ADMIN");

        if(user.getStatus() == null)
            user.setStatus("ACTIVE");

        if(user.getOrganizationId() == null)
            user.setOrganizationId(1L);

        repo.save(user);

        return ResponseEntity.ok(
            "Registered Successfully"
        );
    }

    // CREATE TENANT USER
    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(
        @RequestBody User user
    ) {

        user.setPassword(
            encoder.encode(user.getPassword())
        );

        user.setRole("TENANT");
        user.setStatus("ACTIVE");

        repo.save(user);

        return ResponseEntity.ok(
            "Tenant User Created"
        );
    }

    // LOGIN
    @PostMapping("/login")
    public Map<String,String> login(
    @RequestBody User req){

     User user =
     repo.findByEmail(req.getEmail())
     .orElseThrow(() ->
     new RuntimeException("Invalid Email"));

     // PASSWORD CHECK FIX
     if(!encoder.matches(
         req.getPassword(),
         user.getPassword()
     )){
       throw new RuntimeException("Invalid Password");
     }

     // STATUS CHECK
     if(!"ACTIVE".equalsIgnoreCase(user.getStatus())){
       throw new RuntimeException("Account Inactive");
     }

     Long orgId = 1L;
     Long tenantId = null;

     // USER / TENANT LOGIN
     if("USER".equalsIgnoreCase(user.getRole())
     || "TENANT".equalsIgnoreCase(user.getRole())){

       Tenant tenant =
       tenantRepo.findByEmail(
       user.getEmail()
       ).orElseThrow(() ->
       new RuntimeException("Tenant Not Found"));

       orgId = tenant.getOrganizationId();
       tenantId = tenant.getTenantId();

     } else {
       orgId = user.getOrganizationId();
     }

     String token =
     JwtUtil.generateToken(
     user.getEmail(),
     user.getRole(),
     orgId,
     tenantId
     );

     return Map.of("token", token);
    }

    // RESET PASSWORD
  /*  @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(
        @RequestBody Map<String,String> req
    ) {

        String email = req.get("email");
        String password = req.get("password");

        Optional<User> dbUser =
            repo.findByEmail(email);

        if(dbUser.isPresent()) {

            User user = dbUser.get();

            user.setPassword(
                encoder.encode(password)
            );

            repo.save(user);

            return ResponseEntity.ok(
                "Password Reset Success"
            );
        }

        return ResponseEntity
            .badRequest()
            .body("User Not Found");
    }*/
    
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(
    @RequestBody Map<String,String> req
    ){

       Organization org = new Organization();

       org.setOrganizationName(
          req.get("organizationName")
       );
       org.setEmail(req.get("email"));
       org.setPhone(req.get("phone"));
       org.setPlan(req.get("plan"));
       org.setStatus("ACTIVE");

       orgRepo.save(org);

       User user = new User();

       user.setFullName(req.get("adminName"));
       user.setEmail(req.get("email"));

       user.setPassword(
        new BCryptPasswordEncoder()
        .encode(req.get("password"))
       );

       user.setRole("ADMIN");
       user.setStatus("ACTIVE");
       user.setOrganizationId(
          org.getOrganizationId()
       );

       if(repo.findByEmail(req.get("email")).isPresent()){
    	   return ResponseEntity.badRequest()
    	   .body("Email already exists");
    	}
       repo.save(user);

       return ResponseEntity.ok(
        "Admin Registered Successfully"
       );
    }
    
    @PostMapping("/create-user")
    public String createUser(
    @RequestBody Map<String,Object> req
    ){

       User user = new User();

       user.setFullName(
         req.get("fullName").toString()
       );

       user.setEmail(
         req.get("email").toString()
       );

       user.setPassword(
         new BCryptPasswordEncoder()
         .encode(
           req.get("password").toString()
         )
       );

       user.setRole(
         req.get("role").toString()
       );

       user.setStatus("ACTIVE");

       user.setOrganizationId(
         Long.valueOf(
           req.get("organizationId").toString()
         )
       );
       if(repo.findByEmail(user.getEmail()).isPresent()){
    	   throw new RuntimeException("Email already exists");
    	}

       repo.save(user);

       return "User Created";
    }
    
    @PostMapping("/forgot-password")
    public String forgot(
    @RequestBody Map<String,String> req){

       String email = req.get("email");

      // User user = repo.findByEmail1(email);
       User user =
    		     repo.findByEmail(email)
    		     .orElseThrow(() ->
    		     new RuntimeException("Invalid Email"));

       if(user == null)
          return "Email Not Found";

       String otp =
       String.valueOf(
       (int)(Math.random()*900000)+100000
       );

       user.setOtp(otp);
       user.setOtpExpiry(
         LocalDateTime.now().plusMinutes(10)
       );

       repo.save(user);

       SimpleMailMessage msg =
       new SimpleMailMessage();

       msg.setTo(email);
       msg.setSubject("Password Reset OTP");
       msg.setText("Your OTP is: " + otp);

       mailSender.send(msg);

       return "OTP Sent";
    }
    
    @PostMapping("/reset-password")
    public ResponseEntity<String> reset(
    @RequestBody Map<String,String> req){

       String email = req.get("email");
       String otp = req.get("otp");
       String password = req.get("password");

       User user =
       repo.findByEmail(email)
       .orElseThrow(() ->
       new RuntimeException("Invalid Email"));

       if(!otp.equals(user.getOtp())) {
          return ResponseEntity.badRequest()
                 .body("Wrong OTP");
       }

       if(LocalDateTime.now()
          .isAfter(user.getOtpExpiry())) {

          return ResponseEntity.badRequest()
                 .body("OTP Expired");
       }

       user.setPassword(
          encoder.encode(password)
       );

       user.setOtp(null);
       user.setOtpExpiry(null);

       repo.save(user);

       return ResponseEntity.ok(
          "Password Updated"
       );
    }
    
    @GetMapping("/me")
    public User me(
    @RequestHeader("Authorization") String auth){

       String token = auth.substring(7);

       String email =
       JwtUtil.extractClaims(token).getSubject();

       return repo.findByEmail(email)
       .orElseThrow(() ->
       new RuntimeException("User Not Found"));
    }
    
    @PutMapping("/update-profile")
    public String updateProfile(
    @RequestHeader("Authorization") String auth,
    @RequestBody Map<String,String> req){

       String token = auth.substring(7);

       String email =
       JwtUtil.extractClaims(token).getSubject();

       User user =
       repo.findByEmail(email)
       .orElseThrow();

       user.setFullName(req.get("fullName"));

       repo.save(user);

       return "Profile Updated";
    }
    
    @PutMapping("/change-password")
    public String changePassword(
    @RequestHeader("Authorization") String auth,
    @RequestBody Map<String,String> req){

       String token = auth.substring(7);

       String email =
       JwtUtil.extractClaims(token).getSubject();

       User user =
       repo.findByEmail(email)
       .orElseThrow();

       if(!encoder.matches(
          req.get("oldPassword"),
          user.getPassword()
       )){
          return "Old Password Wrong";
       }

       user.setPassword(
          encoder.encode(
             req.get("newPassword")
          )
       );

       repo.save(user);

       return "Password Changed";
    }
    
    @PostMapping("/upload-photo")
    public String uploadPhoto(
    @RequestHeader("Authorization") String auth,
    @RequestParam("file") MultipartFile file
    ) throws IOException {

       String token = auth.substring(7);

       String email =
       JwtUtil.extractClaims(token).getSubject();

       User user =
       repo.findByEmail(email).orElseThrow();

       String folder =
       System.getProperty("user.dir")
       + "/uploads/profile/";

       File dir = new File(folder);

       if(!dir.exists()){
          dir.mkdirs();
       }

       String fileName =
       user.getId() + "_" +
       file.getOriginalFilename();

       Path path =
       Paths.get(folder + fileName);

       Files.write(path, file.getBytes());

       user.setProfilePhoto(fileName);

       repo.save(user);

       return "Photo Uploaded";
    }
    
    
}