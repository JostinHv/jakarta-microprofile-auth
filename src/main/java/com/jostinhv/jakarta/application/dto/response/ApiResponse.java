package com.jostinhv.jakarta.application.dto.response;

import com.jostinhv.jakarta.domain.annotations.DataTransferObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DataTransferObject(type = DataTransferObject.DtoType.RESPONSE)
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;
    private Object metadata;
}