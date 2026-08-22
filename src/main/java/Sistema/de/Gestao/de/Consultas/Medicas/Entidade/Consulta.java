package Sistema.de.Gestao.de.Consultas.Medicas.Entidade;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.StatusConsulta;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_consulta", nullable = false, updatable = false)
    private UUID idConsulta;
    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;
    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;
    private LocalDateTime dataHora;
    @Enumerated(EnumType.STRING)
    private StatusConsulta status;
    private String observacao;
    @CreationTimestamp
    private LocalDateTime criadoEm = LocalDateTime.now();
}
