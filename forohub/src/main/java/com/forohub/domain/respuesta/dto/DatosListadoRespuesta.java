package com.forohub.domain.respuesta.dto;

import com.forohub.domain.respuesta.Respuesta;
import java.time.LocalDateTime;

public record DatosListadoRespuesta(
        Long id,
        String mensaje,
        String autor,
        LocalDateTime fechaCreacion
) {
    public DatosListadoRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getAutor().getLogin(),
                respuesta.getFechaCreacion()
        );
    }
}