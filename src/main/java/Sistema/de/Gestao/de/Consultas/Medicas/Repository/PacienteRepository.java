package Sistema.de.Gestao.de.Consultas.Medicas.Repository;

import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
