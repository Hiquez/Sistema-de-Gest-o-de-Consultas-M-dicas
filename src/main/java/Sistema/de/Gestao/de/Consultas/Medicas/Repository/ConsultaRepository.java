package Sistema.de.Gestao.de.Consultas.Medicas.Repository;

import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
}
