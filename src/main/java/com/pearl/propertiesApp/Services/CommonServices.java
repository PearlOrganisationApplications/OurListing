package com.pearl.propertiesApp.Services;

import com.pearl.propertiesApp.DTOs.RequestDTO;
import com.pearl.propertiesApp.Entities.Users;
import com.pearl.propertiesApp.Repositories.UsersRepository;
import com.pearl.propertiesApp.Utilities.EmailService;
import com.pearl.propertiesApp.Utilities.JwtTokenUtil;
import com.pearl.propertiesApp.Utilities.MailTemplates;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.SecureRandom;
import java.util.Optional;

@Slf4j
@Service
public class CommonServices {
    SecureRandom random = new SecureRandom();

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private EmailService emailService;

    @Transactional
    public ResponseEntity<?> register(RequestDTO.registerRequestDTO request) throws MessagingException, UnsupportedEncodingException {

        Users user = usersRepository.findByEmail(request.getEmail()).orElse(new Users());
        String otp = String.valueOf(random.nextInt(100000, 999999));
        Optional<Users> usersOptional = usersRepository.findByNumber(request.getNumber());
        if (usersOptional.isPresent()) {
            user = usersOptional.get();
            if (user.getIsVerified()) return ResponseEntity.badRequest()
                    .body("Phone Number is Registered to Another User");
        }

        if (user.getIsVerified()) return ResponseEntity.badRequest().body("User Already Exists");

        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setNumber(request.getNumber());
        log.info("OTP: " + otp);
        user.setOtp(passwordEncoder.encode(otp));
        user.setToken(jwtTokenUtil.generateToken(user.getEmail(), String.valueOf(user.getRole())));
        user.setAddress(request.getAddress());
        user.setRole(Users.role.valueOf(request.getRole()));
        if (user.getRole().equals(Users.role.ADMIN)) throw new RuntimeException("Invalid Role");
//        emailService.sendMail(user.getEmail(),
//                MailTemplates.registrationEmail(user.getName(), otp),
//                "Your OTP is for propertyAPP is " + otp,
//                null);
        return ResponseEntity.ok(usersRepository.save(user));
    }

    public ResponseEntity<?> login(RequestDTO.loginRequestDTO request) {
        Users user = usersRepository.findByEmail(request.getEmail()).orElse(new Users());
        if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            user.setToken(jwtTokenUtil.generateToken(user.getEmail(),
                    String.valueOf(user.getRole())));
            return ResponseEntity.ok(usersRepository.save(user));
        } else return ResponseEntity.badRequest().body("Invalid Credentials");
    }

    public ResponseEntity<?> loginGET(String substring) {
        Users user = usersRepository.findByToken(substring)
                .orElseThrow(() -> new RuntimeException("Session Expired"));
        return ResponseEntity.ok(user);
    }

    public ResponseEntity<?> sendOTP(RequestDTO.registerRequestDTO request)
            throws MessagingException, UnsupportedEncodingException {
        String otp = String.valueOf(random.nextInt(100000, 999999));

        Users user = usersRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("User not Found"));
        user.setOtp(passwordEncoder.encode(otp));
        emailService.sendMail(user.getEmail(),
                MailTemplates.OTP(otp),
                "Your OTP is for propertyAPP is " + otp,
                null);
        return ResponseEntity.ok(usersRepository.save(user));
    }

    public ResponseEntity<?> resetPassword(RequestDTO.registerRequestDTO request) throws MessagingException, UnsupportedEncodingException {
        Users user = usersRepository.findByEmail(request.getEmail()).orElseThrow(() ->
                new RuntimeException("User not Found"));
        if (passwordEncoder.matches(request.getOtp(), user.getOtp())) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setOtp(null);
        }
        emailService.sendMail(user.getEmail(),
                MailTemplates.passwordReset(),
                "Your Password has been reset successfully",
                null);
        return ResponseEntity.ok(usersRepository.save(user));
    }

    public ResponseEntity<?> logout(String substring) {
        Users user = usersRepository.findByToken(substring)
                .orElseThrow(() -> new RuntimeException("Session Expired"));
        user.setToken(null);
        usersRepository.save(user);
        return ResponseEntity.ok("Logged out successfully");
    }

    public ResponseEntity<?> verifyOTP(RequestDTO.registerRequestDTO request) {
        Users user = usersRepository.findByEmail(request.getEmail()).orElse(new Users());
        if (passwordEncoder.matches(request.getOtp(), user.getOtp())) {
            user.setIsVerified(true);
            user.setOtp(null);
            return ResponseEntity.ok(usersRepository.save(user));
        } else return ResponseEntity.badRequest().body("Invalid OTP");
    }
}
