package Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record PacienteRequestDTO(

        @NotBlank(message = "Nome é obrigatório")
        String nome,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
        String cpf,

        @Past(message = "Data de nascimento deve estar no passado")
        LocalDate dataNascimento,

        String telefone,

        @Email(message = "E-mail inválido")
        String email
) {
}