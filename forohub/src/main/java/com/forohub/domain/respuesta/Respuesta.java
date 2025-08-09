package com.forohub.domain.respuesta;

import com.forohub.domain.respuesta.dto.DatosRegistroRespuesta;
import com.forohub.domain.topico.Topico;
import com.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "respuestas")
@Entity(name = "Respuesta")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Respuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255)
    private String mensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    private Boolean solucion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "topico_id")
    private Topico topico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    public Respuesta(DatosRegistroRespuesta datosRegistro, Topico topico, Usuario autor) {
        String mensajeValidado = validarMensaje(datosRegistro.mensaje());

        this.mensaje = mensajeValidado;
        this.fechaCreacion = LocalDateTime.now();
        this.solucion = false;
        this.topico = topico;
        this.autor = autor;
    }

    private String validarMensaje(String mensaje) {
        if (mensaje == null) {
            throw new IllegalArgumentException("El mensaje no puede ser nulo");
        }

        if (mensaje.length() > 255) {
            return mensaje.substring(0, 255);
        }

        return mensaje;
    }
}