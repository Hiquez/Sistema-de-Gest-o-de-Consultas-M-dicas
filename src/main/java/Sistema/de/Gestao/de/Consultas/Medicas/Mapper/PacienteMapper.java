package Sistema.de.Gestao.de.Consultas.Medicas.Mapper;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PacienteMapper {

    Paciente toEntity(PacienteRequestDTO dto);

    PacienteResponseDTO toResponseDTO(Paciente paciente);

    void updateEntityFromDto(
            PacienteRequestDTO dto,
            @MappingTarget Paciente paciente
    );
}