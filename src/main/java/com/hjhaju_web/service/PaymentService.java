package com.hjhaju_web.service;

import com.hjhaju_web.model.Payment;
import com.hjhaju_web.model.User;
import com.hjhaju_web.repository.PaymentRepository;
import com.hjhaju_web.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;

    @Value("${sepay.bank.account.number}")
    private String bankAccountNumber;

    @Value("${sepay.bank.code}")
    private String bankCode;

    /**
     * 🔹 Kiểm tra user có đơn PENDING không
     */
    public Optional<Payment> getActivePendingPayment(User user) {
        LocalDateTime fifteenMinutesAgo = LocalDateTime.now().minusMinutes(15);
        return paymentRepository.findByUserAndStatusAndCreatedAtAfter(
                user, "PENDING", fifteenMinutesAgo);
    }

    /**
     * 🔹 Tìm payment theo orderCode
     */
    public Optional<Payment> findByOrderCode(String orderCode) {
        return paymentRepository.findByOrderCode(orderCode);
    }

    /**
     * 🔹 Tạo đơn nạp tiền với QR (KHÔNG CẦN GỌI API)
     */
    public Payment createPayment(User user, long amount) {
        // Kiểm tra đơn cũ còn PENDING
        Optional<Payment> existingPayment = getActivePendingPayment(user);
        if (existingPayment.isPresent()) {
            return existingPayment.get();
        }

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setAmount(amount);
        payment.setCoin((int) (amount / 10));

        // Tạo mã orderCode
        String orderCode = String.format("NX%d%d",
                user.getId(),
                System.currentTimeMillis() % 1000000);

        payment.setOrderCode(orderCode);
        payment.setStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());

        // Tạo link QR trực tiếp (KHÔNG CẦN GỌI API)
        String qrUrl = String.format(
                "https://qr.sepay.vn/img?acc=%s&bank=%s&amount=%d&des=%s",
                bankAccountNumber,
                bankCode,
                amount,
                orderCode
        );
        payment.setQrCodeUrl(qrUrl);

        paymentRepository.save(payment);
        return payment;
    }

    /**
     * 🔹 Xử lý webhook từ SePay
     */
    @Transactional
    public void markPaymentSuccess(String orderCode, Long actualAmount) {
        paymentRepository.findByOrderCode(orderCode).ifPresent(payment -> {

            if ("SUCCESS".equals(payment.getStatus())) {
                return;
            }

            if (!payment.getAmount().equals(actualAmount)) {
                payment.setStatus("AMOUNT_MISMATCH");
                payment.setActualAmount(actualAmount);
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);
                return;
            }

            payment.setStatus("SUCCESS");
            payment.setActualAmount(actualAmount);
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);

            User user = payment.getUser();
            user.setCoinBalance(user.getCoinBalance() + payment.getCoin());
            user.setTotalTopup(user.getTotalTopup() + payment.getAmount());
            userRepository.save(user);
        });
    }

    /**
     * 🔹 Hủy đơn hết hạn
     */
    @Scheduled(fixedRate = 300000)
    @Transactional
    public void cancelExpiredPayments() {
        LocalDateTime timeout = LocalDateTime.now().minusMinutes(15);
        List<Payment> expiredPayments = paymentRepository
                .findByStatusAndCreatedAtBefore("PENDING", timeout);

        for (Payment payment : expiredPayments) {
            payment.setStatus("EXPIRED");
            payment.setUpdatedAt(LocalDateTime.now());
            paymentRepository.save(payment);
        }
    }
}
