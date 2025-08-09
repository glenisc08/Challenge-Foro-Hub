package com.forohub.domain.respuesta.dto;

import com.forohub.domain.respuesta.Respuesta;
import java.time.LocalDateTime;

public record DatosDetalleRespuesta(
        Long id,
        String mensaje,
        Long idTopico,
        LocalDateTime fechaCreacion,
        String autor,
        Boolean solucion
) {
    public DatosDetalleRespuesta(Respuesta respuesta) {
        this(
                respuesta.getId(),
                respuesta.getMensaje(),
                respuesta.getTopico().getId(),
                respuesta.getFechaCreacion(),
                respuesta.getAutor().getLogin(),
                respuesta.getSolucion()
        );
    }
}