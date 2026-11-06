package ar.edu.undec.tp_eventos.dto;
import jakarta.validation.constraints.NotNull;

public record EntradaRequestDTO(
    @NotNull(message = "El eventoId es obligatorio") Long eventoId,
    @NotNull(message = "El asistenteId es obligatorio") Long asistenteId
) {}