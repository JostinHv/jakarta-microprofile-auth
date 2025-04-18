package com.jostinhv.jakarta.application.dto.response;


import com.jostinhv.jakarta.domain.annotations.DataTransferObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@DataTransferObject(type = DataTransferObject.DtoType.RESPONSE)
public class RateLimitExcedidoResponse {
    private boolean error = true;
    private String message = "Demasiados intentos";
    private RateLimitDetails details;

    @Data
    @AllArgsConstructor
    public static class RateLimitDetails {
        private long retryAfter;
        private String availableAt;
        private int attempts;
        private int limit;
    }

    public RateLimitExcedidoResponse(long retryAfter, String availableAt, int attempts, int limit) {
        this.details = new RateLimitDetails(retryAfter, availableAt, attempts, limit);
        this.message = "Demasiados intentos. Por favor, intente nuevamente después de " + formatDuration(retryAfter);
    }

    public static String formatDuration(long seconds) {
        long hours = seconds / 3600;
        long minutes = (seconds % 3600) / 60;
        long secs = seconds % 60;

        StringBuilder sb = new StringBuilder();
        if (hours > 0) sb.append(hours).append(" horas, ");
        if (minutes > 0) sb.append(minutes).append(" minutos, ");
        sb.append(secs).append(" segundos");

        return sb.toString();
    }

}