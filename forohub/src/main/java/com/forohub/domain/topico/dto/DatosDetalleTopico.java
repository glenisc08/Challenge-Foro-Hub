package com.forohub.domain.topico.dto;

import com.forohub.domain.respuesta.dto.DatosRespuesta;
import com.forohub.domain.topico.Topico;
import java.time.LocalDateTime;
import java.util.List;

public record

DatosDetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String autor,
        String curso,
        Boolean status,
        List<DatosRespuesta> respuestas
) {
    public DatosDetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getAutor().getLogin(),
                topico.getCurso(),
                topico.getStatus(),
                topico.getRespuestas() != null ?
                        topico.getRespuestas().stream()
                                .map(DatosRespuesta::new)
                                .toList() :
                        List.of()
        );
    }
}