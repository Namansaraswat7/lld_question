package org.lld.combinedratelimiter;

import java.util.List;
import java.util.Set;

public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {
        CombinedRateLimiter limiter = new CombinedRateLimiter();

        // Granular: /login has stricter limit
        limiter.addEndpointRule(new EndpointRule("/login",
                List.of(PeriodLimit.perMinute(5)),
                AlgorithmType.TOKEN_BUCKET));

        // Multi-period: /search has 100/min AND 500/day
        limiter.addEndpointRule(new EndpointRule("/search",
                List.of(PeriodLimit.perMinute(100), PeriodLimit.perDay(500)),
                AlgorithmType.SLIDING_WINDOW));

        // Aggregate: /upload and /download share 50/hour
        limiter.addAggregateRule(new AggregateRule("storage-io",
                Set.of("/upload", "/download"),
                List.of(PeriodLimit.perHour(50)),
                AlgorithmType.TOKEN_BUCKET));

        String clientId = "user-1";

        System.out.println("=== Granular: /login (5/min) ===");
        for (int i = 0; i < 7; i++) {
            boolean ok = limiter.allowRequest(clientId, "/login");
            System.out.println("  /login #" + (i + 1) + ": " + (ok ? "allowed" : "denied"));
        }

        System.out.println("\n=== Multi-period: /search (100/min, 500/day) ===");
        int allowed = 0;
        for (int i = 0; i < 105; i++) {
            if (limiter.allowRequest(clientId, "/search")) allowed++;
        }
        System.out.println("  First 105 requests: " + allowed + " allowed (expect 100)");

        System.out.println("\n=== Aggregate: /upload + /download combined 50/hour ===");
        int uploadAllowed = 0, downloadAllowed = 0;
        for (int i = 0; i < 30; i++) {
            if (limiter.allowRequest(clientId, "/upload")) uploadAllowed++;
        }
        for (int i = 0; i < 30; i++) {
            if (limiter.allowRequest(clientId, "/download")) downloadAllowed++;
        }
        System.out.println("  30 uploads: " + uploadAllowed + " allowed");
        System.out.println("  30 downloads (after uploads): " + downloadAllowed + " allowed");
        System.out.println("  Total storage-io: " + (uploadAllowed + downloadAllowed) + " (expect <= 50)");
    }
}
