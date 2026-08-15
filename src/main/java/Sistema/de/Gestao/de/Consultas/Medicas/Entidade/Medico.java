package Sistema.de.Gestao.de.Consultas.Medicas.Entidade;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "medico", uniqueConstraints = {
        @UniqueConstraint(name = "uk_medico_crm", columnNames = "crm")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedico;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(nullable = false, length = 20)
    @NotBlank(message = "CRM é obrigatório")
    private String crm;

    @Column(nullable = false, length = 30)
    @NotNull(message = "Especialidade é obrigatória")
    @Enumerated(EnumType.STRING)
    private Especialidade especialidade;

    @OneToMany(mappedBy = "medico", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Consulta> consultas = new ArrayList<>();

    private Boolean status = true;
}
