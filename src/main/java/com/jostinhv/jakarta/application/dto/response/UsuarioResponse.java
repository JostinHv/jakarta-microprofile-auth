package com.jostinhv.jakarta.application.dto.response;

import com.jostinhv.jakarta.domain.annotations.DataTransferObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DataTransferObject(type = DataTransferObject.DtoType.RESPONSE)
public class UsuarioResponse {

    private int usuario_id;
    private String username;
    private List<RolResponse> roles;

}
