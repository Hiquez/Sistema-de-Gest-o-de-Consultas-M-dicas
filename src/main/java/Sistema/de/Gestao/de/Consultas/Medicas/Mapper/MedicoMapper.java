package Sistema.de.Gestao.de.Consultas.Medicas.Mapper;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MedicoMapper {


    MedicoResponseDTO toResponseDTO(Medico medico);

    Medico toEntity(MedicoRequestDTO dto);

    @Mapping(source = "medico.nome", target = "nome")
    @Mapping(source = "medico.crm", target = "crm")
    @Mapping(source = "medico.especialidade", target = "especialidade")
    Medico updateEntity(Medico medico, MedicoRequestDTO dto);

}
