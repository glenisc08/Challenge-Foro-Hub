package com.forohub.domain.usuario;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DatosRegistroUsuario(
        @NotBlank(message = "Login obligatorio")
        @Size(min = 3, max = 50)
        String login,

        @NotBlank(message = "Clave obligatoria")
        @Size(min = 6)
        String clave,

        @NotBlank(message = "Email obligatorio")
        String email
) {}