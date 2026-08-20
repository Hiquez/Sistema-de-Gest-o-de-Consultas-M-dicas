package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public record ConsultaAgendadaEvent(
        UUID idConsulta,
        String nomePaciente,
        String emailPaciente,
        String nomeMedico,
        LocalDateTime dataHora,
        String dataHoraFormatada
) {

    public ConsultaAgendadaEvent(
            UUID idConsulta,
            String nomePaciente,
            String emailPaciente,
            String nomeMedico,
            LocalDateTime dataHora
    ) {
        this(
                idConsulta,
                nomePaciente,
                emailPaciente,
                nomeMedico,
                dataHora,
                dataHora.format(
                        DateTimeFormatter.ofPattern("dd/MM/yyyy 'às' HH:mm")
                )
        );
    }
}
