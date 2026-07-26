package com.pipocaagil.feedback.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CreateUserOngDto(

        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        @NotBlank(message = "Senha é obrigatória")
        String password,

        @NotBlank(message = "CEP é obrigatório")
        String cep,

        @NotBlank(message = "CNPJ é obrigatório")
        @CNPJ(message = "CNPJ inválido.")
        String cnpj,

        @NotBlank(message = "Área de atuação é obrigatória")
        String area_atuacao

) {}