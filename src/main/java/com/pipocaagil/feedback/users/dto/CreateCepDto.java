package com.pipocaagil.feedback.users.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateCepDto (
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "CEP inválido. Use o formato 12345678 ou 12345-678.")
    String cep
) {
}
