package ar.edu.undec.tp_eventos.controller;

import ar.edu.undec.tp_eventos.dto.EntradaRequestDTO;
import ar.edu.undec.tp_eventos.dto.EstadoRequestDTO;
import ar.edu.undec.tp_eventos.model.Entrada;
import ar.edu.undec.tp_eventos.model.EstadoEntrada;
import ar.edu.undec.tp_eventos.service.EntradaService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/entradas")
public class EntradaController {

    private final EntradaService entradaService;

    public EntradaController(EntradaService entradaService) {
        this.entradaService = entradaService;
    }

    @PostMapping
    public ResponseEntity<Entrada> crear(@Valid @RequestBody EntradaRequestDTO dto) {
        Entrada creada = entradaService.crear(dto.eventoId(), dto.asistenteId());
        return ResponseEntity.created(URI.create("/api/entradas/" + creada.getId())).body(creada);
    }

    @PatchMapping("/{id}/estado")
    public Entrada cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequestDTO dto) {
        EstadoEntrada nuevoEstado;
        try {
            nuevoEstado = EstadoEntrada.valueOf(dto.estado().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido");
        }
        return entradaService.cambiarEstado(id, nuevoEstado);
    }

    @PostMapping("/validar")
    public Entrada validarCodigo(@RequestBody Map<String, String> request) {
        if (!request.containsKey("codigo")) {
            throw new IllegalArgumentException("Debe enviar el código");
        }
        UUID codigo = UUID.fromString(request.get("codigo"));
        return entradaService.validarCodigo(codigo);
    }

    @PatchMapping("/{id}/transferir")
    public Entrada transferir(@PathVariable Long id, @RequestBody Map<String, Long> request) {
        if (!request.containsKey("asistenteId")) {
            throw new IllegalArgumentException("Debe enviar asistenteId");
        }
        return entradaService.transferir(id, request.get("asistenteId"));
    }
}