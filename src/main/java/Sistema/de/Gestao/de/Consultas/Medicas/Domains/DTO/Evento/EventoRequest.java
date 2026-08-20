package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Evento;

import java.util.UUID;

public record EventoRequest(
        String sistemaOrigem,
        String tipoEvento,
        String payload,
        UUID eventId
) {
}
