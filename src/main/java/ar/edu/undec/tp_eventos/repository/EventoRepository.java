package ar.edu.undec.tp_eventos.repository;

import ar.edu.undec.tp_eventos.model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventoRepository extends JpaRepository<Evento, Long> {
    
    // Para la consulta: GET /api/eventos?nombre=rock[cite: 364]
    List<Evento> findByNombreContainingIgnoreCase(String nombre);
}