package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente;

import java.time.LocalDate;

public record PacienteResponseDTO(
        Long id,
        String nome,
        String cpf,
        LocalDate dataNascimento,
        String telefone,
        String email
) {
}
