package com.pipocaagil.feedback.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(

        @Schema(example = "contato@ong.com")
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        @Schema(example = "Senha@123")
        @NotBlank(message = "Senha é obrigatório")
        String password

) {
}
