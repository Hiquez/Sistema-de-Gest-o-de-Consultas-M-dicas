package Sistema.de.Gestao.de.Consultas.Medicas.Entidade;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedico;
    @NotBlank(message = "O nome do médico não pode ser vazio")
    @Size(max = 100, message = "O nome do médico não pode ter mais de 100 caracteres")
    @Min(value = 3, message = "O nome do médico deve ter pelo menos 3 caracteres")
    private String nome;
    @NotBlank
    @Column(nullable = false)
    private String crm;
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;
    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas;
}
