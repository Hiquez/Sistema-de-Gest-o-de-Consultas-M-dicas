package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos;

import java.time.LocalDateTime;

public record ConsultaConfirmadaEvent(
        Long idConsulta,
        String nomePaciente,
        String emailPaciente,
        String nomeMedico,
        LocalDateTime dataHora
) {
}
