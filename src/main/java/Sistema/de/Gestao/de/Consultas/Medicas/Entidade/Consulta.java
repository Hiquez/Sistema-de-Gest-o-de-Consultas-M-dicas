package Sistema.de.Gestao.de.Consultas.Medicas.Entidade;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idConsulta;
    @ManyToMany
    @JoinColumn
    private Paciente paciente;
    @ManyToMany
    @JoinColumn
    private Medico medico;
    @Future(message = "A data e hora da consulta deve ser uma data futura")
    private LocalDateTime dataHora;
    @Enumerated(EnumType.STRING)
    private StatusConsulta status;
    private String observacao;
    @CreationTimestamp
    private LocalDateTime criadoEm = LocalDateTime.now();
}
