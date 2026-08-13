package Sistema.de.Gestao.de.Consultas.Medicas.Repository;

import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicoRepository extends JpaRepository<Medico, Long> {
}
