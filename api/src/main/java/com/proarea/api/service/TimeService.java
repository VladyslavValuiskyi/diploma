package com.proarea.api.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
public class TimeService {

    public Long getCurrentTimeInMills() {
        return Instant.now().toEpochMilli();
    }

    public LocalDateTime getInstantNow() {
        return getInstantNow("UTC");
    }

    public LocalDateTime getInstantNow(Long timestamp) {
        return Instant.ofEpochMilli(timestamp)
                .atZone(ZoneId.of("UTC"))
                .toLocalDateTime();
    }

    public LocalDateTime getInstantNow(String timeZone) {
        return Instant.ofEpochMilli(getCurrentTimeInMills())
                .atZone(ZoneId.of(timeZone))
                .toLocalDateTime();
    }

    public Long getMills(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        ZonedDateTime zdt = time.atZone(ZoneId.of("UTC"));
        return zdt.toInstant().toEpochMilli();
    }
}

