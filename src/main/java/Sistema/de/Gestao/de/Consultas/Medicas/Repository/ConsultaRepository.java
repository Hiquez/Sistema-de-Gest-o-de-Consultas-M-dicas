package Sistema.de.Gestao.de.Consultas.Medicas.Repository;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Consulta;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ConsultaRepository extends JpaRepository<Consulta, UUID> {
    boolean existsByMedicoAndDataHora(Medico medicoExiste, LocalDateTime localDateTime);

    List<Consulta> findByPaciente_IdPaciente(Long idPaciente);

    List<Consulta> findByMedico_IdMedico(Long idMedico);
}
