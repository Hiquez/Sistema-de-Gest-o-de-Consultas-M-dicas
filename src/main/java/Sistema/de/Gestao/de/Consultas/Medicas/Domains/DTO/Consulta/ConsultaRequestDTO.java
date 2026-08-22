package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.StatusConsulta;
import jakarta.validation.constraints.Future;

import java.time.LocalDateTime;

public record ConsultaRequestDTO(
        Long idConsulta,
        Long idPaciente,
        Long idMedico,
        @Future(message = "A data e hora da consulta deve ser uma data futura")
        LocalDateTime dataHora,
        StatusConsulta status,
        String observacao
) {
}
