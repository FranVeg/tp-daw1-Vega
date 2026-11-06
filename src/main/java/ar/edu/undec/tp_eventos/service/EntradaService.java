package ar.edu.undec.tp_eventos.service;

import ar.edu.undec.tp_eventos.model.*;
import ar.edu.undec.tp_eventos.repository.*;
import ar.edu.undec.tp_eventos.exception.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EntradaService {

    private final EntradaRepository entradaRepository;
    private final EventoRepository eventoRepository;
    private final AsistenteRepository asistenteRepository;

    public EntradaService(EntradaRepository entradaRepository, EventoRepository eventoRepository, AsistenteRepository asistenteRepository) {
        this.entradaRepository = entradaRepository;
        this.eventoRepository = eventoRepository;
        this.asistenteRepository = asistenteRepository;
    }

    public Entrada crear(Long eventoId, Long asistenteId) {
        Evento evento = eventoRepository.findById(eventoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evento no encontrado"));
        Asistente asistente = asistenteRepository.findById(asistenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asistente no encontrado"));

        long entradasOcupadas = entradaRepository.countByEventoIdAndEstadoNot(evento.getId(), EstadoEntrada.ANULADA);
        if (entradasOcupadas >= evento.getCapacidad()) {
            throw new ReglaNegocioException("La capacidad del evento está agotada");
        }

        if (entradaRepository.existsByAsistenteIdAndEventoIdAndEstadoNot(asistente.getId(), evento.getId(), EstadoEntrada.ANULADA)) {
            throw new ReglaNegocioException("El asistente ya tiene una entrada activa para este evento");
        }

        Entrada entrada = new Entrada();
        entrada.setEvento(evento);
        entrada.setAsistente(asistente);
        entrada.setEstado(EstadoEntrada.RESERVADA);
        entrada.setFechaReserva(LocalDateTime.now());
        return entradaRepository.save(entrada);
    }

    public Entrada cambiarEstado(Long id, EstadoEntrada nuevoEstado) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrada no encontrada"));
        
        EstadoEntrada actual = entrada.getEstado();
        boolean transicionValida = (actual == EstadoEntrada.RESERVADA && (nuevoEstado == EstadoEntrada.PAGADA || nuevoEstado == EstadoEntrada.ANULADA)) ||
                                   (actual == EstadoEntrada.PAGADA && (nuevoEstado == EstadoEntrada.USADA || nuevoEstado == EstadoEntrada.ANULADA));
        
        if (!transicionValida) {
            throw new ReglaNegocioException("Transición de estado no permitida");
        }

        // Preventa
        if (nuevoEstado == EstadoEntrada.PAGADA) {
            if (LocalDateTime.now().isBefore(entrada.getEvento().getFecha().minusDays(7))) {
                entrada.setImportePagado(entrada.getEvento().getPrecio() * 0.80);
            } else {
                entrada.setImportePagado(entrada.getEvento().getPrecio());
            }
        }
        
        entrada.setEstado(nuevoEstado);
        return entradaRepository.save(entrada);
    }

    public List<Entrada> buscarPorEvento(Long eventoId) {
        if (!eventoRepository.existsById(eventoId)) {
            throw new RecursoNoEncontradoException("Evento no encontrado");
        }
        return entradaRepository.findByEventoId(eventoId);
    }

    public Entrada validarCodigo(UUID codigo) {
        Entrada entrada = entradaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Código no válido"));
        if (entrada.getEstado() != EstadoEntrada.PAGADA) {
            throw new ReglaNegocioException("La entrada no está pagada o ya fue anulada/usada");
        }
        entrada.setEstado(EstadoEntrada.USADA);
        return entradaRepository.save(entrada);
    }

    public Entrada transferir(Long id, Long nuevoAsistenteId) {
        Entrada entrada = entradaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrada no encontrada"));
        if (entrada.getEstado() != EstadoEntrada.PAGADA) {
            throw new ReglaNegocioException("Solo se pueden transferir entradas pagadas");
        }
        Asistente nuevoAsistente = asistenteRepository.findById(nuevoAsistenteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asistente no encontrado"));
        if (entradaRepository.existsByAsistenteIdAndEventoIdAndEstadoNot(nuevoAsistente.getId(), entrada.getEvento().getId(), EstadoEntrada.ANULADA)) {
            throw new ReglaNegocioException("El nuevo asistente ya tiene una entrada activa");
        }
        entrada.setAsistente(nuevoAsistente);
        return entradaRepository.save(entrada);
    }

    @Scheduled(fixedRate = 3600000)
    public void anularEntradasVencidas() {
        LocalDateTime limite = LocalDateTime.now().minusHours(48);
        List<Entrada> vencidas = entradaRepository.findByEstadoAndFechaReservaBefore(EstadoEntrada.RESERVADA, limite);
        for (Entrada e : vencidas) {
            e.setEstado(EstadoEntrada.ANULADA);
            entradaRepository.save(e);
        }
    }
}