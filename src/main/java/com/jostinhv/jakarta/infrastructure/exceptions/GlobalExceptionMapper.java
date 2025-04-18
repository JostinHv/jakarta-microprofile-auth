package com.jostinhv.jakarta.infrastructure.exceptions;


import com.jostinhv.jakarta.application.dto.response.ApiResponse;
import com.jostinhv.jakarta.domain.exceptions.AutenticacionException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<AutenticacionException> {

    @Override
    public Response toResponse(AutenticacionException exception) {
        ApiResponse<?> apiResponse = new ApiResponse<>(
                exception.getStatusCode(),
                exception.getMessage(),
                null,
                null
        );

        return Response.status(exception.getStatusCode())
                .entity(apiResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}