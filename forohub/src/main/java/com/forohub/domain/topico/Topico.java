package com.forohub.domain.topico;

import com.forohub.domain.respuesta.Respuesta;
import com.forohub.domain.topico.dto.DatosActualizarTopico;
import com.forohub.domain.topico.dto.DatosRegistroTopico;
import com.forohub.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "topicos")
@Entity(name = "Topico")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Topico {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String mensaje;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    private Boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "autor_id")
    private Usuario autor;

    private String curso;

    @OneToMany(mappedBy = "topico", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Respuesta> respuestas;

    public Topico(DatosRegistroTopico datosRegistroTopico, Usuario autor) {
        this.titulo = datosRegistroTopico.titulo();
        this.mensaje = datosRegistroTopico.mensaje();
        this.fechaCreacion = LocalDateTime.now();
        this.status = true;
        this.autor = autor;
        this.curso = datosRegistroTopico.curso();
    }

    public void actualizarDatos(DatosActualizarTopico datosActualizarTopico) {

        if (datosActualizarTopico.titulo() != null && !datosActualizarTopico.titulo().trim().isEmpty()) {
            this.titulo = datosActualizarTopico.titulo();
            System.out.println("Título actualizado: " + this.titulo);
        }

        if (datosActualizarTopico.mensaje() != null && !datosActualizarTopico.mensaje().trim().isEmpty()) {
            this.mensaje = datosActualizarTopico.mensaje();
            System.out.println("Mensaje actualizado: " + this.mensaje);
        }
    }
}