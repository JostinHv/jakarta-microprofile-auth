package com.jostinhv.jakarta.infrastructure.filters;

import com.jostinhv.jakarta.application.dto.response.RateLimitExcedidoResponse;
import com.jostinhv.jakarta.infrastructure.annotations.RateLimit;
import com.jostinhv.jakarta.infrastructure.utils.RateLimiter;
import com.jostinhv.jakarta.infrastructure.utils.RateLimiter.RateLimitInfo;
import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.lang.reflect.Method;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class RateLimitingFilter implements ContainerRequestFilter {

    @Inject
    private RateLimiter rateLimiter;

    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        Method resourceMethod = resourceInfo.getResourceMethod();
        if (resourceMethod == null) {
            return;
        }

        RateLimit rateLimit = resourceMethod.getAnnotation(RateLimit.class);
        if (rateLimit == null) {
            return;
        }

        int limit = rateLimit.limit();
        int minutes = rateLimit.minutes();

        String key = generateKey(requestContext, resourceMethod);

        boolean allowed = rateLimiter.tryAcquire(key, limit, minutes);
        if (!allowed) {
            RateLimitInfo info = rateLimiter.getRateLimitInfo(key);
            long retryAfter = info.getRetryAfter();
            String availableAt = info.getAvailableAt().toString();
            int attempts = info.getAttempts();

            RateLimitExcedidoResponse response = new RateLimitExcedidoResponse(
                    retryAfter,
                    availableAt,
                    attempts,
                    limit
            );

            requestContext.abortWith(Response.status(Response.Status.TOO_MANY_REQUESTS)
                    .entity(response)
                    .type(MediaType.APPLICATION_JSON)
                    .build());
        }
    }

    private String generateKey(ContainerRequestContext requestContext, Method resourceMethod) {
        String clientIp = requestContext.getHeaderString("X-Forwarded-For");
        if (clientIp == null || clientIp.isEmpty()) {
            clientIp = requestContext.getUriInfo().getRequestUri().getHost();
        }
        String userAgent = requestContext.getHeaderString("User-Agent");

        return clientIp + ":" + resourceMethod.getDeclaringClass().getName() + ":" + userAgent + "#" + resourceMethod.getName();
    }
}
