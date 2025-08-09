package com.forohub.infra.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("API Foro Hub")
                        .description("API REST para la gestión de un foro de discusión parecido al de Alura. " +
                                "Permite la creación de: consulta, actualización y eliminación de tópicos y respuestas. " +
                                "Incluye sistema de autenticación con JWT.")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Glenis Corona")
                                .email("glenisc163@gmail.com")
                                .url("https://github.com/glenisc08"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")));
    }
}