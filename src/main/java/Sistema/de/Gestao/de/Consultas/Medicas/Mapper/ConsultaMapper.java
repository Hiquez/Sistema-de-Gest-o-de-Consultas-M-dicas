package Sistema.de.Gestao.de.Consultas.Medicas.Mapper;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.ConsultaRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Consulta;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConsultaMapper {

    @Mapping(target = "nomePaciente", source = "paciente.nome")
    @Mapping(target = "nomeMedico", source = "medico.nome")
    ConsultaResponseDTO toResponse(Consulta consulta);

    List<ConsultaResponseDTO> toListResponse(List<Consulta> consultas);
}
