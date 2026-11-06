package ar.edu.undec.tp_eventos.dto;
import jakarta.validation.constraints.NotBlank;

public record EstadoRequestDTO(
    @NotBlank(message = "El estado es obligatorio") String estado
) {}