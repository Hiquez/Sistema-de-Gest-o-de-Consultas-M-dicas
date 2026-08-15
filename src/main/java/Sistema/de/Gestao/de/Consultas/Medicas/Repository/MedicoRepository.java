package Sistema.de.Gestao.de.Consultas.Medicas.Repository;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MedicoRepository extends JpaRepository<Medico, Long> {

    Optional<Medico> findByCrm(String crm);

    boolean existsByCrm(String crm);

    List<Medico> findByEspecialidade(Especialidade especialidade);
}
