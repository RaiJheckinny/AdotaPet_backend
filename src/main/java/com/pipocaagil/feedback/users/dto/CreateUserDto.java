package com.pipocaagil.feedback.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserDto(

        @Schema(example = "Joao Fernandes")
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Schema(example = "joao@email.com")
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        @Schema(example = "Senha@123")
        @NotBlank(message = "Senha é obrigatória")
        String password,

        @Schema(example = "65275-970")
        @NotBlank(message = "CEP é obrigatório")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP inválido. Use o formato 12345678 ou 12345-678.")
        String cep
) {}