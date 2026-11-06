package ar.edu.undec.tp_eventos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Entrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "evento_id")
    private Evento evento;

    @ManyToOne(optional = false)
    @JoinColumn(name = "asistente_id")
    private Asistente asistente;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEntrada estado = EstadoEntrada.RESERVADA;

    @Column(nullable = false, unique = true)
    private UUID codigo = UUID.randomUUID();

    private Double importePagado;

    @Column(nullable = false)
    private LocalDateTime fechaReserva = LocalDateTime.now();

    public Entrada() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Evento getEvento() { return evento; }
    public void setEvento(Evento evento) { this.evento = evento; }
    public Asistente getAsistente() { return asistente; }
    public void setAsistente(Asistente asistente) { this.asistente = asistente; }
    public EstadoEntrada getEstado() { return estado; }
    public void setEstado(EstadoEntrada estado) { this.estado = estado; }
    public UUID getCodigo() { return codigo; }
    public void setCodigo(UUID codigo) { this.codigo = codigo; }
    public Double getImportePagado() { return importePagado; }
    public void setImportePagado(Double importePagado) { this.importePagado = importePagado; }
    public LocalDateTime getFechaReserva() { return fechaReserva; }
    public void setFechaReserva(LocalDateTime fechaReserva) { this.fechaReserva = fechaReserva; }
}