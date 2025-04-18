package com.jostinhv.jakarta.application.dto.request;

import com.jostinhv.jakarta.domain.annotations.DataTransferObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@DataTransferObject(type = DataTransferObject.DtoType.REQUEST)
public class RegisterRequest {
    @Pattern(regexp = "^[a-zA-Z0-9._-]{3,}$", message = "Username inválido")
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    private String username;
    @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres")
    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;

}
