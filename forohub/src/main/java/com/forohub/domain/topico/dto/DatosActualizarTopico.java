package com.forohub.domain.topico.dto;

import jakarta.validation.constraints.NotBlank;

public record DatosActualizarTopico(
        String titulo,
        String mensaje
) {
    public boolean tieneContenido() {
        return (titulo != null && !titulo.trim().isEmpty()) ||
                (mensaje != null && !mensaje.trim().isEmpty());
    }
}