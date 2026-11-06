package ar.edu.undec.tp_eventos.repository;

import ar.edu.undec.tp_eventos.model.Asistente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AsistenteRepository extends JpaRepository<Asistente, Long> {
}