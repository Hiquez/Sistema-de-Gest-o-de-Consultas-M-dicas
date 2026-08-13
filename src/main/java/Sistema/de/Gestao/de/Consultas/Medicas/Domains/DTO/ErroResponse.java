package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO;

import java.time.LocalDateTime;

public record ErroResponse(
        int status,
        String message,
        LocalDateTime timestamp
) {
}