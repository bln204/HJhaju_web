package com.hjhaju_web.Util;

import java.time.Duration;
import java.time.LocalDateTime;

public class TimeUtils {
    public static String timeAgo(LocalDateTime createdAt) {
        if (createdAt == null) return "";

        Duration duration = Duration.between(createdAt, LocalDateTime.now());

        long seconds = duration.getSeconds();
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        if (seconds < 60) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 30) {
            return days + " ngày trước";
        } else if (days < 365) {
            return (days / 30) + " tháng trước";
        } else {
            return (days / 365) + " năm trước";
        }
    }
}