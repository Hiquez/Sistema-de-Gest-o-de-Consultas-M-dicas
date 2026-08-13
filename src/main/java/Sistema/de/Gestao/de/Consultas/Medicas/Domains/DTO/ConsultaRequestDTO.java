package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.StatusConsulta;
import java.time.LocalDateTime;

public record ConsultaRequestDTO(
        Long idConsulta,
        Long idPaciente,
        Long idMedico,
        LocalDateTime dataHora,
        StatusConsulta status,
        String observacao
) {
}
