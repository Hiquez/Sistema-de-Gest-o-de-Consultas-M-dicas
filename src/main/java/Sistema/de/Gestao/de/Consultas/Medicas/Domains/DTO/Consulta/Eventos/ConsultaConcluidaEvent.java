package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos;

import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaConcluidaEvent(
        UUID idConsulta,
        String nomePaciente,
        String emailPaciente,
        String nomeMedico,
        LocalDateTime dataHora
) {
}
