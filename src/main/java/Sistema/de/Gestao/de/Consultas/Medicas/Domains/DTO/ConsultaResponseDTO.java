package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.StatusConsulta;
import java.time.LocalDateTime;

public record ConsultaResponseDTO(
        Long idConsulta,
        String nomePaciente,
        String nomeMedico,
        LocalDateTime dataHora,
        StatusConsulta status,
        String observacao
) {
}
