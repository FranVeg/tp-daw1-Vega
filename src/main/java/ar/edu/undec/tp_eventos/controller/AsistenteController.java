package ar.edu.undec.tp_eventos.controller;

import ar.edu.undec.tp_eventos.model.Asistente;
import ar.edu.undec.tp_eventos.repository.AsistenteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


@RestController
@RequestMapping("/api/asistentes")
public class AsistenteController {

    private final AsistenteRepository asistenteRepository;

    public AsistenteController(AsistenteRepository asistenteRepository) {
        this.asistenteRepository = asistenteRepository;
    }

    @PostMapping
    public ResponseEntity<Asistente> crear(@Valid @RequestBody Asistente asistente) {
        Asistente creado = asistenteRepository.save(asistente);
        return ResponseEntity.created(URI.create("/api/asistentes/" + creado.getId())).body(creado);
    }
}