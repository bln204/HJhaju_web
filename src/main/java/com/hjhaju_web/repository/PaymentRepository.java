package com.hjhaju_web.repository;

import com.hjhaju_web.model.Payment;
import com.hjhaju_web.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrderCode(String orderCode);

    // Tìm đơn PENDING còn hiệu lực của user
    Optional<Payment> findByUserAndStatusAndCreatedAtAfter(
            User user, String status, LocalDateTime createdAfter);

    // Tìm các payment hết hạn
    List<Payment> findByStatusAndCreatedAtBefore(String status, LocalDateTime before);

}