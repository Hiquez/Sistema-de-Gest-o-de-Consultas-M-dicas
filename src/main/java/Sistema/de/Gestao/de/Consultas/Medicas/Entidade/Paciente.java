package Sistema.de.Gestao.de.Consultas.Medicas.Entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "paciente", uniqueConstraints = {
        @UniqueConstraint(name = "uk_paciente_cpf", columnNames = "cpf")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @Column(nullable = false, length = 150)
    @NotBlank(message = "Nome é obrigatório")
    private String nome;

    @Column(nullable = false, length = 11)
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
    private String cpf;

    @Column(name = "data_nascimento", nullable = false)
    @Past(message = "Data de nascimento deve estar no passado")
    private LocalDate dataNascimento;

    @Column(length = 20)
    private String telefone;

    @Column(length = 150)
    @Email(message = "E-mail inválido")
    private String email;

    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<Consulta> consultas = new ArrayList<>();

    private Boolean status = true;
}
