package com.forohub.controller;

import com.forohub.domain.topico.Topico;
import com.forohub.domain.topico.TopicoRepository;
import com.forohub.domain.topico.dto.DatosActualizarTopico;
import com.forohub.domain.topico.dto.DatosDetalleTopico;
import com.forohub.domain.topico.dto.DatosListadoTopico;
import com.forohub.domain.topico.dto.DatosRegistroTopico;
import com.forohub.domain.usuario.Usuario;
import com.forohub.domain.usuario.UsuarioRepository;
import com.forohub.infra.errores.ValidacionException;
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
import java.util.Objects;

@RestController
@RequestMapping("/topicos")
@SecurityRequirement(name = "bearer-key")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity<DatosDetalleTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datosRegistroTopico,
            UriComponentsBuilder uriComponentsBuilder) {

        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuario = (Usuario) usuarioRepository.findByLogin(loginUsuario);

        if (topicoRepository.existsByTituloAndMensaje(datosRegistroTopico.titulo(), datosRegistroTopico.mensaje())) {
            throw new ValidacionException("Ya existe un tópico igual a este");
        }

        Topico topico = new Topico(datosRegistroTopico, usuario);
        topicoRepository.save(topico);

        DatosDetalleTopico datosDetalleTopico = new DatosDetalleTopico(topico);
        URI url = uriComponentsBuilder
                .path("/topicos/{id}")
                .buildAndExpand(topico.getId())
                .toUri();

        return ResponseEntity.created(url).body(datosDetalleTopico);
    }

    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listadoTopicos(
            @PageableDefault(size = 10) Pageable paginacion) {
        return ResponseEntity.ok(
                topicoRepository.findAll(paginacion).map(DatosListadoTopico::new)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> retornaDatosTopico(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        Topico topico = topicoRepository.getReferenceById(id);
        var datosDetalleTopico = new DatosDetalleTopico(topico);
        return ResponseEntity.ok(datosDetalleTopico);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosDetalleTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datosActualizarTopico) {


        if (!topicoRepository.existsById(id)) {
            System.out.println("Tópico no encontrado: " + id);
            return ResponseEntity.notFound().build();
        }

        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = (Usuario) usuarioRepository.findByLogin(loginUsuario);

        System.out.println("Usuario autenticado: " + loginUsuario);
        System.out.println("ID del usuario autenticado: " + usuarioAutenticado.getId());

        Topico topico = topicoRepository.getReferenceById(id);
        System.out.println("Autor del tópico: " + topico.getAutor().getLogin());
        System.out.println("ID del autor del tópico: " + topico.getAutor().getId());

        if (!Objects.equals(topico.getAutor().getId(), usuarioAutenticado.getId())) {
            System.out.println("Usuario no autorizado para modificar este tópico");
            return ResponseEntity.status(403).build();
        }

        topico.actualizarDatos(datosActualizarTopico);
        System.out.println("Tópico actualizado correctamente");
        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarTopico(@PathVariable Long id) {
        if (!topicoRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        String loginUsuario = SecurityContextHolder.getContext().getAuthentication().getName();
        Usuario usuarioAutenticado = (Usuario) usuarioRepository.findByLogin(loginUsuario);

        Topico topico = topicoRepository.getReferenceById(id);

        if (!topico.getAutor().getId().equals(usuarioAutenticado.getId())) {
            return ResponseEntity.status(403).build();
        }

        topicoRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}