package com.hjhaju_web.controller.clientController;

import com.hjhaju_web.model.User;
import com.hjhaju_web.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class AuthController {
    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login(Model model) {
        model.addAttribute("googleLoginUrl", "/oauth2/authorization/google");
        return "client/auth/login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        System.out.println("/register");
        model.addAttribute("user", new User());
        return "client/auth/register";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        log.info("Processing POST /signup, user: username={}, email={}, fullName={}, password={}",
                user.getUsername(), user.getEmail(), user.getFullName(), user.getPassword());
        try {
            userService.registerUser(user, bindingResult);
            log.info("User registered successfully: {}", user.getEmail());
            return "redirect:/login?signupSuccess=true";
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage(), e);
            model.addAttribute("signupError", e.getMessage());
            model.addAttribute("user", user);
            return "client/auth/register";
        }
    }
    @GetMapping("/profile/edit")
    public String editProfile(Model model) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        model.addAttribute("user", user);
        return "client/auth/edit-profile";
    }

    @PostMapping("/profile/edit")
    public String updateProfile(@ModelAttribute User updatedUser) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Cập nhật fullName và lưu vào DB
        currentUser.setFullName(updatedUser.getFullName());
        userService.updateUser(currentUser);

        // Cập nhật SecurityContext
        Authentication newAuth = new UsernamePasswordAuthenticationToken(
                currentUser,
                currentUser.getPassword(),
                currentUser.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        return "redirect:/";
    }

    @GetMapping("/profile/change-password")
    public String changePasswordForm(Model model) {
        model.addAttribute("error", null);
        return "client/auth/change-password";
    }

    @PostMapping("/profile/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                 @RequestParam String newPassword,
                                 @RequestParam String confirmPassword,
                                 Model model) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = currentUser.getEmail();

        // kiểm tra mật khẩu mới và xác nhận mật khẩu có khớp không
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không khớp");
            return "client/auth/change-password";
        }

        try {
            userService.changePassword(email, oldPassword, newPassword);

            // cập nhật SecurityContext với mật khẩu mới
            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                    currentUser,
                    newPassword,
                    currentUser.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(newAuth);

            return "redirect:/";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "client/auth/change-password";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi hệ thống khi đổi mật khẩu");
            return "client/auth/change-password";
        }
    }

    @GetMapping("/forgot-password")
    public String forgotPasswordForm(Model model) {
        System.out.println("Accessing /forgot-password endpoint");
        model.addAttribute("error", null);
        model.addAttribute("message", null);
        return "client/auth/forgot-password";
    }

    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        System.out.println("Processing POST /forgot-password for email: " + email);
        try {
            userService.requestPasswordReset(email);
            model.addAttribute("message", "OTP đã được gửi đến email của bạn.");
            model.addAttribute("email", email);
            return "client/auth/verify-otp";
        } catch (UsernameNotFoundException e) {
            model.addAttribute("error", "Không tìm thấy email này.");
            return "client/auth/forgot-password";
        } catch (Exception e) {
            model.addAttribute("error", "Lỗi hệ thống khi gửi OTP.");
            return "client/auth/forgot-password";
        }
    }

    @GetMapping("/verify-otp")
    public String verifyOtpForm(@RequestParam("email") String email, Model model) {
        System.out.println("Accessing /verify-otp endpoint for email: " + email);
        model.addAttribute("email", email);
        model.addAttribute("error", null);
        model.addAttribute("message", null);
        return "client/auth/verify-otp";
    }

    @PostMapping("/verify-otp")
    public String processVerifyOtp(@RequestParam("email") String email,
                                   @RequestParam("otp") String otp,
                                   @RequestParam("newPassword") String newPassword,
                                   @RequestParam("confirmPassword") String confirmPassword,
                                   Model model) {
        System.out.println("Processing POST /verify-otp for email: " + email);
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận không khớp");
            model.addAttribute("email", email);
            return "client/auth/verify-otp";
        }
        try {
            userService.verifyOtpAndResetPassword(email, otp, newPassword, confirmPassword);
            model.addAttribute("message", "Mật khẩu đã được đặt lại thành công. Vui lòng đăng nhập.");
            return "client/auth/login";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("email", email);
            return "client/auth/verify-otp";
        }
    }
}