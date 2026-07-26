package com.pipocaagil.feedback.security.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("API Feedback")
                        .description("Documentação da API do sistema de Feedback")
                        .version("1.0")
                        .contact(new Contact()
                                .name("Pipoca Ágil")
                                .email("contato@pipocaagil.com"))
                        .license(new License()
                                .name("MIT")))
                .externalDocs(new ExternalDocumentation()
                        .description("Repositório")
                        .url("https://github.com/RaiJheckinny"));
    }
}
