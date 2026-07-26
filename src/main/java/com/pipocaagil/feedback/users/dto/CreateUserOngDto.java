package com.pipocaagil.feedback.users.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.br.CNPJ;

public record CreateUserOngDto(

        @Schema(example = "ONG Esperança")
        @NotBlank(message = "Nome é obrigatório")
        String name,

        @Schema(example = "contato@ong.com")
        @Email(message = "E-mail inválido")
        @NotBlank(message = "E-mail é obrigatório")
        String email,

        @Schema(example = "Senha@123")
        @NotBlank(message = "Senha é obrigatória")
        String password,

        @Schema(example = "65275-970")
        @NotBlank(message = "CEP é obrigatório")
        String cep,

        @Schema(example = "11.222.333/0001-81")
        @NotBlank(message = "CNPJ é obrigatório")
        @CNPJ(message = "CNPJ inválido.")
        String cnpj,

        @Schema(example = "Proteção Animal")
        @NotBlank(message = "Área de atuação é obrigatória")
        String area_atuacao

) {}