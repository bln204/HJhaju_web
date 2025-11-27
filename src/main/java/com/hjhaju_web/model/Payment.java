package com.hjhaju_web.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;



@Entity
@Table(name = "payments", indexes = {
        @Index(name = "idx_order_code", columnList = "orderCode"),
        @Index(name = "idx_user_status", columnList = "user_id, status"),
        @Index(name = "idx_created_at", columnList = "createdAt")
})
@Data
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, unique = true)
    private String orderCode;

    @Column(nullable = false)
    private Long amount;

    @Column(name = "actual_amount")
    private Long actualAmount; // Số tiền thực tế chuyển

    @Column(nullable = false)
    private Integer coin;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "qr_code_url", length = 500)
    private String qrCodeUrl; // Link QR Code

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
}
