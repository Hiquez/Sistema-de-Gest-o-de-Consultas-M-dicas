package Sistema.de.Gestao.de.Consultas.Medicas.Mapper;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;

public class PacienteMapper {

    private PacienteMapper() {}

    public static Paciente toEntity(PacienteRequestDTO dto) {
        return Paciente.builder()
                .nome(dto.nome())
                .cpf(dto.cpf())
                .dataNascimento(dto.dataNascimento())
                .telefone(dto.telefone())
                .email(dto.email())
                .build();
    }

    public static void updateEntityFromDto(PacienteRequestDTO dto, Paciente paciente) {
        paciente.setNome(dto.nome());
        paciente.setCpf(dto.cpf());
        paciente.setDataNascimento(dto.dataNascimento());
        paciente.setTelefone(dto.telefone());
        paciente.setEmail(dto.email());
    }

    public static PacienteResponseDTO toResponseDTO(Paciente paciente) {
        return new PacienteResponseDTO(
                paciente.getId(),
                paciente.getNome(),
                paciente.getCpf(),
                paciente.getDataNascimento(),
                paciente.getTelefone(),
                paciente.getEmail()
        );
    }
}