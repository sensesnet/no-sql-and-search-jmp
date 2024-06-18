package com.epam.jmp.redislab.configuration.ratelimit;

import java.time.temporal.ChronoUnit;

public enum RateLimitTimeInterval {

    MINUTE,
    HOUR;

    public ChronoUnit toChronoUnit() {
        switch (this) {
            case MINUTE: return ChronoUnit.MINUTES;
            case HOUR: return ChronoUnit.HOURS;
            default: return ChronoUnit.FOREVER;
        }
    }
}
