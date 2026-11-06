package ar.edu.undec.tp_eventos.repository;

import ar.edu.undec.tp_eventos.model.Entrada;
import ar.edu.undec.tp_eventos.model.EstadoEntrada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EntradaRepository extends JpaRepository<Entrada, Long> {

    long countByEventoIdAndEstadoNot(Long eventoId, EstadoEntrada estado);
    boolean existsByAsistenteIdAndEventoIdAndEstadoNot(Long asistenteId, Long eventoId, EstadoEntrada estado);
    boolean existsByEventoId(Long eventoId);
    List<Entrada> findByEventoId(Long eventoId);
    
    Optional<Entrada> findByCodigo(UUID codigo);
    List<Entrada> findByEstadoAndFechaReservaBefore(EstadoEntrada estado, LocalDateTime fecha);
}