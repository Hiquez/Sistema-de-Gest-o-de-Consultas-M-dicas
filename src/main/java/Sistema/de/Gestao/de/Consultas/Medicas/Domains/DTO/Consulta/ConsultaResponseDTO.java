package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.StatusConsulta;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaResponseDTO(
        UUID idConsulta,
        String nomePaciente,
        String nomeMedico,
        LocalDateTime dataHora,
        StatusConsulta status,
        String observacao
) {
}
