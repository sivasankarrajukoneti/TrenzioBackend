package com.ecommerce.trenzio.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TrackingNumberGenerator {

    public static String generateTrackingNumber() {
        // Custom format: TRK-[yyyyMMdd]-[randomNumber]
        return "TRK-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")) +
                "-" + (int) (Math.random() * 100000);
    }
}
