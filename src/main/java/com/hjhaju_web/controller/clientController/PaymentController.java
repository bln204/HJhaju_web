package com.hjhaju_web.controller.clientController;

import com.hjhaju_web.model.Payment;
import com.hjhaju_web.model.User;
import com.hjhaju_web.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${sepay.bank.account.number}")
    private String bankAccountNumber;

    @Value("${sepay.bank.account.name}")
    private String bankAccountName;

    /**
     * 🔹 Trang nạp xu
     */
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public String showTopUpPage(Authentication authentication, Model model) {
        User user = (User) authentication.getPrincipal();
        model.addAttribute("user", user);
        return "client/home/payment";
    }

    /**
     * 🔹 Tạo đơn nạp tiền
     */
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createPayment(@RequestBody Map<String, Object> req,
                                           Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        try {
            long amount = Long.parseLong(req.get("amount").toString());

            // Validate amount
            if (amount < 10000 || amount > 50000000) {
                return ResponseEntity.badRequest().body(Map.of(
                        "error", "Số tiền phải từ 10,000 VNĐ đến 50,000,000 VNĐ"
                ));
            }

            Payment payment = paymentService.createPayment(user, amount);

            return ResponseEntity.ok(Map.of(
                    "orderCode", payment.getOrderCode(),
                    "amount", payment.getAmount(),
                    "coin", payment.getCoin(),
                    "qrCodeUrl", payment.getQrCodeUrl(),
                    "bankAccount", bankAccountNumber,
                    "bankName", "TPBank",
                    "accountHolder", bankAccountName,
                    "expiresIn", 15,
                    "message", "Tạo đơn thành công!"
            ));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "error", "Số tiền không hợp lệ"
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * 🔹 Kiểm tra trạng thái thanh toán
     */
    @GetMapping("/status/{orderCode}")
    @ResponseBody
    public ResponseEntity<?> checkStatus(@PathVariable String orderCode,
                                         Authentication authentication) {
        User user = (User) authentication.getPrincipal();

        return paymentService.findByOrderCode(orderCode)
                .filter(p -> p.getUser().getId().equals(user.getId()))
                .map(p -> ResponseEntity.ok(Map.of(
                        "status", p.getStatus(),
                        "amount", p.getAmount(),
                        "coin", p.getCoin()
                )))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 🔹 Webhook callback từ SePay
     */
    @PostMapping("/webhook")
    @ResponseBody
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) {
        System.out.println("🔔 Webhook received: " + payload);

        try {
            // Lấy nội dung giao dịch (tương thích cả hai kiểu)
            String transactionContent = (String) payload.getOrDefault("transaction_content", payload.get("content"));
            if (transactionContent == null) {
                System.err.println("❌ Không tìm thấy 'content' trong payload!");
                return ResponseEntity.badRequest().body("Missing content field");
            }

            // Lấy số tiền nhận được (tương thích cả hai kiểu)
            String amountStr = String.valueOf(payload.getOrDefault("amount_in", payload.get("transferAmount")));
            if (amountStr == null) {
                System.err.println("❌ Không tìm thấy 'amount_in' hoặc 'transferAmount' trong payload!");
                return ResponseEntity.badRequest().body("Missing amount field");
            }

            BigDecimal actualAmount = new BigDecimal(amountStr);
            Long actualAmountLong = actualAmount.longValue();
            String orderCode = extractOrderCode(transactionContent);

            System.out.println("✅ Extracted order code: " + orderCode);
            System.out.println("✅ Amount received: " + actualAmount);

            if (orderCode == null) {
                System.err.println("❌ Không tìm thấy mã đơn hàng hợp lệ trong nội dung: " + transactionContent);
                return ResponseEntity.badRequest().body("Invalid or missing order code");
            }

            paymentService.markPaymentSuccess(orderCode, actualAmountLong);
            System.out.println("🎉 Payment updated successfully for " + orderCode);

            return ResponseEntity.ok("Webhook processed successfully");

        } catch (Exception e) {
            System.err.println("❌ Lỗi xử lý webhook: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error");
        }
    }


    /**
     * Tách orderCode từ nội dung CK
     */
    private String extractOrderCode(String content) {
        if (content == null) return null;

        // Tìm pattern NX + số
        Pattern pattern = Pattern.compile("NX\\d+");
        Matcher matcher = pattern.matcher(content.toUpperCase());

        if (matcher.find()) {
            return matcher.group();
        }

        return null;
    }
}