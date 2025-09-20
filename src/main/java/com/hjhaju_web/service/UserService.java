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

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));
    }

    @Transactional
    public void registerUser(User user) throws Exception {
        log.info("Attempting to register user: {}", user.getEmail());
        // Kiểm tra dữ liệu đầu vào
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.error("Email is null or empty");
            throw new IllegalArgumentException("Email không được để trống");
        }
        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            log.error("Password is null or empty");
            throw new IllegalArgumentException("Mật khẩu không được để trống");
        }
        if (user.getUsername() == null || user.getUsername().isEmpty()) {
            log.error("Username is null or empty");
            throw new IllegalArgumentException("Tên người dùng không được để trống");
        }

        // Kiểm tra email và username trùng lặp
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            log.error("Email already exists: {}", user.getEmail());
            throw new IllegalArgumentException("Email đã tồn tại");
        }
        Optional<User> existingUsername = userRepository.findByUsername(user.getUsername());
        if (existingUsername.isPresent()) {
            log.error("Username already exists: {}", user.getUsername());
            throw new IllegalArgumentException("Tên người dùng đã tồn tại");
        }

        // Mã hóa mật khẩu và gán vai trò
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");
        log.info("Saving user: {}", user.getEmail());
        try {
            userRepository.save(user);
            log.info("User saved successfully: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to save user: {}", user.getEmail(), e);
            throw new Exception("Lỗi khi lưu người dùng: " + e.getMessage(), e);
        }
    }
    @Transactional
    public User registerOrUpdateGoogleUser(String email, String fullName) {
        log.info("Registering or updating Google user with email: {}", email);
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setUsername(email);

        // Chỉ set fullName nếu chưa tồn tại hoặc rỗng (không ghi đè nếu đã có)
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            user.setFullName(fullName != null ? fullName : "Người dùng Google");
        }

        user.setRole("USER");
        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode("google-auth-" + email));
        }
        try {
            user = userRepository.save(user);
            log.info("Google user saved successfully: {}", user.getEmail());
            return user;
        } catch (Exception e) {
            log.error("Failed to save Google user: {}", email, e);
            throw new RuntimeException("Lỗi khi lưu người dùng Google: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void updateUser(User user) {
        log.info("Updating user: {}", user.getEmail());
        try {
            userRepository.save(user);
            log.info("User updated successfully: {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to update user: {}", user.getEmail(), e);
            throw new RuntimeException("Lỗi khi cập nhật người dùng: " + e.getMessage(), e);
        }
    }
    @Transactional
    public void changePassword(String email, String oldPassword, String newPassword) {
        log.info("Attempting to change password for user: {}", email);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email));

        // Kiểm tra mật khẩu cũ
        if (user.getPassword() == null || !passwordEncoder.matches(oldPassword, user.getPassword())) {
            log.error("Invalid old password for user: {}", email);
            throw new IllegalArgumentException("Mật khẩu cũ không đúng");
        }

        // Cập nhật mật khẩu mới
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userRepository.save(user);
            log.info("Password changed successfully for user: {}", email);
        } catch (Exception e) {
            log.error("Failed to change password for user: {}", email, e);
            throw new RuntimeException("Lỗi khi đổi mật khẩu: " + e.getMessage(), e);
        }
    }

    public List<User> getAllUser(){
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
            user.setOtpExpiry(new Date(System.currentTimeMillis() + 10 * 60 * 1000)); // OTP hết hạn sau 10 phút
            userRepository.save(user);
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

        // Kiểm tra độ mạnh của mật khẩu mới
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
            log.info("Password reset successfully for email: {}", email);
        } else {
            log.error("Email not found: {}", email);
            throw new UsernameNotFoundException("Không tìm thấy người dùng với email: " + email);
        }
    }

    private String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Tạo OTP 6 chữ số
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
