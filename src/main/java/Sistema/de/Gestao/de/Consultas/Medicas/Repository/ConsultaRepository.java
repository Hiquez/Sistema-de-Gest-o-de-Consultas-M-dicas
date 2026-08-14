package Sistema.de.Gestao.de.Consultas.Medicas.Repository;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Consulta;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    boolean existsByMedicoAndDataHora(Medico medicoExiste, LocalDateTime localDateTime);

    List<ConsultaResponseDTO> listarConsultasPorPaciente(Long idPaciente);

    List<ConsultaResponseDTO> listarConsultasPorMedico(Long idMedico);
}
