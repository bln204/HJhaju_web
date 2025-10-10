package com.hjhaju_web.service;

import com.hjhaju_web.model.User;
import com.hjhaju_web.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Validator validator;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
    }

    @Transactional
    public void registerUser(User user, BindingResult bindingResult) throws Exception {
        log.info("Attempting to register user: {}", user.getEmail());
        log.debug("User details: username={}, email={}, fullName={}, password={}",
                user.getUsername(), user.getEmail(), user.getFullName(), user.getPassword());

        // Kiểm tra validation
        validator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .reduce("", (a, b) -> a + ", " + b);
            log.error("Validation errors: {}", errors);
            throw new IllegalArgumentException("Dữ liệu không hợp lệ: " + errors);
        }

        // Kiểm tra email và username trùng lặp
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            log.error("Email already exists: {}", user.getEmail());
            bindingResult.rejectValue("email", "error.user", "Email đã tồn tại");
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            log.error("Username already exists: {}", user.getUsername());
            bindingResult.rejectValue("username", "error.user", "Tên người dùng đã tồn tại");
            throw new IllegalArgumentException("Tên người dùng đã tồn tại");
        }

        // Mã hóa mật khẩu và gán vai trò
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        log.info("Saving user: {}", user.getEmail());
        try {
            User savedUser = userRepository.save(user);
            userRepository.flush();
            log.info("User saved successfully: {}, ID: {}", savedUser.getEmail(), savedUser.getId());
        } catch (Exception e) {
            log.error("Failed to save user: {}, error: {}", user.getEmail(), e.getMessage(), e);
            throw new Exception("Lỗi khi lưu người dùng: " + e.getMessage(), e);
        }
    }

    @Transactional
    public User registerOrUpdateGoogleUser(String email, String fullName) {
        log.info("Registering or updating Google user with email: {}", email);
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setUsername(email);
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            user.setFullName(fullName != null ? fullName : "Người dùng Google");
        }
        user.setRole("USER");
        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode("google-auth-" + email));
        }
        try {
            User savedUser = userRepository.save(user);
            userRepository.flush();
            log.info("Google user saved successfully: {}, ID: {}", savedUser.getEmail(), savedUser.getId());
            return savedUser;
        } catch (Exception e) {
            log.error("Failed to save Google user: {}, error: {}", email, e.getMessage(), e);
            throw new RuntimeException("Lỗi khi lưu người dùng Google: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateUser(User user) {
        log.info("Updating user: {}", user.getEmail());
        try {
            userRepository.save(user);
            userRepository.flush();
            log.info("User updated successfully: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to update user: {}, error: {}", user.getEmail(), e.getMessage(), e);
            throw new RuntimeException("Lỗi khi cập nhật người dùng: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        log.info("Attempting to change password for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
        if (user.getPassword() == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.error("Invalid old password for user: {}", email);
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userRepository.save(user);
            userRepository.flush();
            log.info("Password changed successfully for user: {}", email);
        } catch (Exception e) {
            log.error("Failed to change password for user: {}, error: {}", email, e.getMessage(), e);
            throw new RuntimeException("Lỗi khi đổi mật khẩu: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Transactional
    public void requestPasswordReset(String email) throws Exception {
        log.info("Requesting password reset for email: {}", email);
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = generateOtp();
            user.setOtp(otp);
            user.setOtpExpiry(new Date(System.currentTimeMillis() + 10 * 60 * 1000));
            userRepository.save(user);
            userRepository.flush();
            sendOtpEmail(user.getEmail(), otp);
            log.info("OTP sent successfully to: {}", email);
        } else {
            log.error("Email not found: {}", email);
            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);
        }
    }

    @Transactional
    public void verifyOtpAndResetPassword(String email, String otp, String newPassword, String confirmPassword) throws Exception {
        log.info("Verifying OTP for email: {}", email);
        if (!newPassword.equals(confirmPassword)) {
            log.error("Password and confirm password do not match for email: {}", email);
            throw new IllegalArgumentException("Mật khẩu mới và xác nhận mật khẩu không khớp");
        }
        if (!isPasswordStrong(newPassword)) {
            log.error("New password is too weak for email: {}", email);
            throw new IllegalArgumentException("Mật khẩu mới phải có ít nhất 8 ký tự, bao gồm chữ hoa và số");
        }
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getOtp() == null || !user.getOtp().equals(otp)) {
                log.error("Invalid OTP for email: {}", email);
                throw new IllegalArgumentException("OTP không hợp lệ");
            }
            if (user.getOtpExpiry().before(new Date())) {
                log.error("OTP expired for email: {}", email);
                throw new IllegalArgumentException("OTP đã hết hạn");
            }
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setOtp(null);
            user.setOtpExpiry(null);
            userRepository.save(user);
            userRepository.flush();
            log.info("Password reset successfully for email: {}", email);
        } else {
            log.error("Email not found: {}", email);
            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOtpEmail(String email, String otp) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(email);
        helper.setSubject("OTP để đặt lại mật khẩu");
        helper.setText("Mã OTP của bạn là: <b>" + otp + "</b>. Mã này có hiệu lực trong 10 phút.", true);
        mailSender.send(message);
    }

    private boolean isPasswordStrong(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*");
    }
}