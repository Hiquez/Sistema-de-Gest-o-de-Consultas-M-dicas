package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicoRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CRM é obrigatório")
        String crm,

        @NotNull(message = "Especialidade é obrigatória")
        Especialidade especialidade
) {
}
