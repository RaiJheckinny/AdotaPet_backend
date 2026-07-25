package com.pipocaagil.feedback.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginUserDto(

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,
        @NotBlank(message = "Senha é obrigatório")
        String password

) {
}
