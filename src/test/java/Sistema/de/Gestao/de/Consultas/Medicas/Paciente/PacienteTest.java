package Sistema.de.Gestao.de.Consultas.Medicas.Paciente;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;
import Sistema.de.Gestao.de.Consultas.Medicas.Mapper.PacienteMapper;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.PacienteRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.PacienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PacienteTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private PacienteMapper pacienteMapper;

    @InjectMocks
    private PacienteService pacienteService;

    @Test
    public void testarCadastroPaciente(){
        //Arrange -> Instaciar as classes.
        PacienteRequestDTO pacienteRequest = new PacienteRequestDTO(
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005,05,11),
                null,
                "pedroTeste@gmail.com");

        PacienteResponseDTO pacienteResponse = new PacienteResponseDTO(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com"
        );

        Paciente paciente = new Paciente();

        when(pacienteMapper.toEntity(pacienteRequest))
                .thenReturn(paciente);

        when(pacienteRepository.save(paciente))
                .thenReturn(paciente);

        when(pacienteMapper.toResponseDTO(paciente))
                .thenReturn(pacienteResponse);

        PacienteResponseDTO resultado = pacienteService.cadastrar(pacienteRequest);

        assertEquals(pacienteResponse,resultado);

        verify(pacienteRepository).save(paciente);
    }
}
