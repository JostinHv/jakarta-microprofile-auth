package com.jostinhv.jakarta.infrastructure.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.enterprise.context.ApplicationScoped;
import lombok.Getter;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class RateLimiter {
    private static final LoadingCache<String, RateLimitInfo> limiterCache;

    static {
        limiterCache = CacheBuilder.newBuilder()
                .expireAfterWrite(1, TimeUnit.HOURS) // Cache general
                .build(new CacheLoader<>() {
                    @Override
                    public RateLimitInfo load(String key) {
                        return new RateLimitInfo();
                    }
                });
    }

    public boolean tryAcquire(String key, int maxAttempts, int windowMinutes) {
        RateLimitInfo info = limiterCache.getUnchecked(key);
        return info.tryAcquire(maxAttempts, windowMinutes);
    }

    public RateLimitInfo getRateLimitInfo(String key) {
        return limiterCache.getUnchecked(key);
    }

    public static class RateLimitInfo {
        @Getter
        private int attempts;
        private long windowStart;
        private int windowMinutes;
        private final Object lock = new Object();

        public RateLimitInfo() {
            this.windowStart = Instant.now().getEpochSecond();
            this.attempts = 0;
            this.windowMinutes = 1; // Valor predeterminado
        }

        public boolean tryAcquire(int maxAttempts, int windowMinutes) {
            synchronized (lock) {
                // Actualiza el valor de la ventana para reflejar la configuración actual
                this.windowMinutes = windowMinutes;
                long now = Instant.now().getEpochSecond();
                long windowEnd = windowStart + (windowMinutes * 60L);
                if (now >= windowEnd) {
                    // Reset if window expired
                    attempts = 1;
                    windowStart = now;
                    return true;
                }

                if (attempts >= maxAttempts) {
                    return false;
                }

                attempts++;
                return true;
            }
        }

        public long getRetryAfter() {
            long now = Instant.now().getEpochSecond();
            return Math.max(0, (windowStart + (windowMinutes * 60L)) - now);
        }

        public Instant getAvailableAt() {
            return Instant.ofEpochSecond(windowStart + (windowMinutes * 60L));
        }
    }
}