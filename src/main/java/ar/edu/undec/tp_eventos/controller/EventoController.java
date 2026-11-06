package ar.edu.undec.tp_eventos.controller;

import ar.edu.undec.tp_eventos.dto.OcupacionDTO;
import ar.edu.undec.tp_eventos.model.Entrada;
import ar.edu.undec.tp_eventos.model.Evento;
import ar.edu.undec.tp_eventos.service.EntradaService;
import ar.edu.undec.tp_eventos.service.EventoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;
    private final EntradaService entradaService;

    public EventoController(EventoService eventoService, EntradaService entradaService) {
        this.eventoService = eventoService;
        this.entradaService = entradaService;
    }

    @GetMapping
    public List<Evento> listar(@RequestParam(required = false) String nombre) {
        if (nombre != null && !nombre.isBlank()) {
            return eventoService.buscarPorNombre(nombre);
        }
        return eventoService.buscarTodos();
    }

    @GetMapping("/{id}")
    public Evento buscarPorId(@PathVariable Long id) {
        return eventoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<Evento> crear(@Valid @RequestBody Evento evento) {
        Evento creado = eventoService.crear(evento);
        return ResponseEntity.created(URI.create("/api/eventos/" + creado.getId())).body(creado);
    }

    @PutMapping("/{id}")
    public Evento modificar(@PathVariable Long id, @Valid @RequestBody Evento evento) {
        return eventoService.modificar(id, evento);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        eventoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/entradas")
    public List<Entrada> buscarEntradas(@PathVariable Long id) {
        return entradaService.buscarPorEvento(id);
    }

    @GetMapping("/{id}/ocupacion")
    public OcupacionDTO verOcupacion(@PathVariable Long id) {
        return eventoService.calcularOcupacion(id);
    }
}