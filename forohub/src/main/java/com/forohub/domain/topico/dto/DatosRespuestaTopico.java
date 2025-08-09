package com.forohub.domain.topico.dto;

import com.forohub.domain.topico.Topico;
import com.forohub.domain.respuesta.dto.DatosRespuesta;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        String autor,
        String curso,
        List<DatosRespuesta> respuestas
) {
    public DatosRespuestaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getAutor().getLogin(),
                topico.getCurso(),
                topico.getRespuestas().stream().map(DatosRespuesta::new).collect(Collectors.toList())
        );
    }
}