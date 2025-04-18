package com.jostinhv.jakarta.application.dto.response;

import com.jostinhv.jakarta.domain.annotations.DataTransferObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DataTransferObject(type = DataTransferObject.DtoType.RESPONSE)
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
