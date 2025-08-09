package com.forohub.controller;

import com.forohub.domain.respuesta.Respuesta;
import com.forohub.domain.respuesta.RespuestaRepository;
import com.forohub.domain.respuesta.dto.DatosDetalleRespuesta;
import com.forohub.domain.respuesta.dto.DatosListadoRespuesta;
import com.forohub.domain.respuesta.dto.DatosRegistroRespuesta;
import com.forohub.domain.topico.TopicoRepository;
import com.forohub.domain.usuario.Usuario;
import com.forohub.domain.usuario.UsuarioRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import java.net.URI;

@RestController
@RequestMapping("/respuestas")
@SecurityRequirement(name = "bearer-key")
public class RespuestaController {

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleRespuesta> crearRespuesta(
            @RequestBody @Valid DatosRegistroRespuesta datosRegistro,
            UriComponentsBuilder uriBuilder) {

        if (!topicoRepository.existsById(datosRegistro.topicoId())) {
            return ResponseEntity.badRequest().build();
        }

        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByLogin(loginUsuario);

        var topico = topicoRepository.getReferenceById(datosRegistro.topicoId());
        Respuesta respuesta = new Respuesta(datosRegistro, topico, usuario);
        respuestaRepository.save(respuesta);

        DatosDetalleRespuesta datosDetalle = new DatosDetalleRespuesta(respuesta);

        URI url = uriBuilder
                .path("/respuestas/{id}")
                .buildAndExpand(respuesta.getId())
                .toUri();

        return ResponseEntity.created(url).body(datosDetalle);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoRespuesta>> listarRespuestas(
            @PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(
                respuestaRepository.findAll(paginacion).map(DatosListadoRespuesta::new)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleRespuesta> obtenerRespuesta(@PathVariable Long id) {
        if (!respuestaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Respuesta respuesta = respuestaRepository.getReferenceById(id);
        return ResponseEntity.ok(new DatosDetalleRespuesta(respuesta));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarRespuesta(@PathVariable Long id) {
        if (!respuestaRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = (Usuario) usuarioRepository.findByLogin(loginUsuario);

        Respuesta respuesta = respuestaRepository.getReferenceById(id);

        if (!respuesta.getAutor().getId().equals(usuarioAutenticado.getId())) {
            return ResponseEntity.status(403).build();
        }

        respuestaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}