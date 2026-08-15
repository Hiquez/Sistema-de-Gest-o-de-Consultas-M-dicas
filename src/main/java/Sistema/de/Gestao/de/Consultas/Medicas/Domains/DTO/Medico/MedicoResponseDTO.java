package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;

public record   MedicoResponseDTO(
        Long idMedico,
        String nome,
        String crm,
        Especialidade especialidade
) {
}