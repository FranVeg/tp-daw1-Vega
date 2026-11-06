package ar.edu.undec.tp_eventos.service;

import ar.edu.undec.tp_eventos.dto.OcupacionDTO;
import ar.edu.undec.tp_eventos.model.EstadoEntrada;
import ar.edu.undec.tp_eventos.model.Evento;
import ar.edu.undec.tp_eventos.repository.EntradaRepository;
import ar.edu.undec.tp_eventos.repository.EventoRepository;
import ar.edu.undec.tp_eventos.exception.RecursoNoEncontradoException;
import ar.edu.undec.tp_eventos.exception.ReglaNegocioException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventoService {

    private final EventoRepository eventoRepository;
    private final EntradaRepository entradaRepository;

    public EventoService(EventoRepository eventoRepository, EntradaRepository entradaRepository) {
        this.eventoRepository = eventoRepository;
        this.entradaRepository = entradaRepository;
    }

    public List<Evento> buscarTodos() {
        return eventoRepository.findAll();
    }

    public Evento buscarPorId(Long id) {
        return eventoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Evento no encontrado"));
    }

    public Evento crear(Evento evento) {
        return eventoRepository.save(evento);
    }

    public Evento modificar(Long id, Evento datos) {
        Evento evento = buscarPorId(id);
        evento.setNombre(datos.getNombre());
        evento.setFecha(datos.getFecha());
        evento.setCapacidad(datos.getCapacidad());
        evento.setPrecio(datos.getPrecio());
        return eventoRepository.save(evento);
    }

    public void eliminar(Long id) {
        if (!eventoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Evento no encontrado");
        }
        if (entradaRepository.existsByEventoId(id)) {
            throw new ReglaNegocioException("No se puede borrar un evento con entradas asociadas");
        }
        eventoRepository.deleteById(id);
    }

    public List<Evento> buscarPorNombre(String nombre) {
        return eventoRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public OcupacionDTO calcularOcupacion(Long id) {
        Evento evento = buscarPorId(id);
        long vendidas = entradaRepository.countByEventoIdAndEstadoNot(id, EstadoEntrada.ANULADA);
        long disponibles = evento.getCapacidad() - vendidas;
        double porcentaje = (vendidas * 100.0) / evento.getCapacidad();
        return new OcupacionDTO(vendidas, disponibles, porcentaje);
    }
}