package Sistema.de.Gestao.de.Consultas.Medicas.Entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paciente")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;
    @NotBlank(message = "O nome do paciente não pode ser vazio")
    @Size(max = 100, message = "O nome do paciente não pode ter mais de 100 caracteres")
    @Min(value = 3, message = "O nome do paciente deve ter pelo menos 3 caracteres")
    private String nome;
    @NotBlank(message = "O CPF do paciente não pode ser vazio")
    @Size(min = 11, max = 11, message = "O CPF do paciente deve ter exatamente 11 caracteres")
    @Column(unique = true)
    private String cpf;
    @Past(message = "A data de nascimento deve ser uma data passada")
    private LocalDate dataNascimento;
    private String telefone;
    @Email(message = "O email do paciente deve ser um email válido")
    private String email;
    @OneToMany(mappedBy = "paciente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Consulta> consultas;
}
