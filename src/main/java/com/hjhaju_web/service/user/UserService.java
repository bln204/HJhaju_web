package com.hjhaju_web.service.user;

import com.hjhaju_web.model.User;
import com.hjhaju_web.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
    public User registerOrUpdateGoogleUser(String email, String fullName) {
        User user = userRepository.findByEmail(email).orElse(new User());
        user.setEmail(email);
        user.setUsername(email);
        user.setFullName(fullName);
        user.setRole("USER");
        if (user.getPassword() == null) {
            user.setPassword(passwordEncoder.encode("google-auth-" + email));
        }
        return userRepository.save(user);
    }
}
