package Sistema.de.Gestao.de.Consultas.Medicas.Paciente;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.CpfDuplicadoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.PacienteNaoLocalizadoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Mapper.PacienteMapper;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.PacienteRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.PacienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void testarBuscaPorId(){
        Paciente paciente = new Paciente(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                true
        );

        PacienteResponseDTO pacienteResponse = new PacienteResponseDTO(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com"
        );

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(pacienteMapper.toResponseDTO(paciente))
                .thenReturn(pacienteResponse);

        PacienteResponseDTO resultado = pacienteService.buscarPorId(1L);

        assertEquals(pacienteResponse,resultado);

        verify(pacienteRepository).findById(1L);
        verify(pacienteMapper).toResponseDTO(paciente);
    }

    @Test
    public void testarAtualizarPaciente(){
        Paciente paciente = new Paciente(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                true
        );

        PacienteRequestDTO pacienteRequest = new PacienteRequestDTO(
                "Pedro Henrique de Borba",
                "13681599960",
                LocalDate.of(2010,05,11),
                null,
                "pedroTeste@gmail.com");

        PacienteResponseDTO pacienteResponse = new PacienteResponseDTO(
                1L,
                "Pedro Henrique de Borba",
                "13681599960",
                LocalDate.of(2010,05,11),
                null,
                "pedroTeste@gmail.com");

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(pacienteMapper.toResponseDTO(paciente))
                .thenReturn(pacienteResponse);

        PacienteResponseDTO resultado = pacienteService.atualizar(1L, pacienteRequest);

        assertEquals(pacienteResponse, resultado);

        verify(pacienteRepository).findById(1L);
        verify(pacienteMapper).toResponseDTO(paciente);
        verify(pacienteMapper).updateEntityFromDto(pacienteRequest,paciente);
    }

    @Test
    public void testeAtivarPaciente(){
        Paciente paciente = new Paciente(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                false
        );

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        pacienteService.ativarPaciente(1L);

        assertEquals(paciente.getStatus(), true);

        verify(pacienteRepository).findById(1L);
    }

    @Test
    public void testeInativarPaciente(){
        Paciente paciente = new Paciente(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                true
        );

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        pacienteService.inativarPaciente(1L);

        assertEquals(paciente.getStatus(), false);

        verify(pacienteRepository).findById(1L);
    }

    @Test
    public void testarCpfDuplicado(){
        PacienteRequestDTO pacienteRequest = new PacienteRequestDTO(
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005,05,11),
                null,
                "pedroTeste@gmail.com");

        when(pacienteRepository.existsByCpf(pacienteRequest.cpf()))
                .thenReturn(true);

        assertThrows(
                CpfDuplicadoException.class,
                () -> pacienteService.cadastrar(pacienteRequest)
        );

        verify(pacienteRepository).existsByCpf(pacienteRequest.cpf());
        verify(pacienteRepository,never()).save(any());
    }

    @Test
    public void testarIdNaoLocalizado(){

        when(pacienteRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                PacienteNaoLocalizadoException.class,
                () -> pacienteService.buscarPorId(2L)
        );

        verify(pacienteRepository).findById(2L);
    }

    @Test
    public void testarListaVazia(){
        when(pacienteRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<PacienteResponseDTO> resultado = pacienteService.listarTodos();

        assertTrue(resultado.isEmpty());

        verify(pacienteRepository).findAll();
    }

    @Test
    public void testarListaComPaciente(){
        Paciente paciente1 = new Paciente(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                true
        );

        Paciente paciente2 = new Paciente(
                2L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                true
        );

        PacienteResponseDTO pacienteResponse1 = new PacienteResponseDTO(
                1L,
                "Pedro Henrique de Borba",
                "13681599960",
                LocalDate.of(2010,05,11),
                null,
                "pedroTeste@gmail.com");

        PacienteResponseDTO pacienteResponse2 = new PacienteResponseDTO(
                2L,
                "Pedro Henrique de Borba",
                "13681599960",
                LocalDate.of(2010,05,11),
                null,
                "pedroTeste@gmail.com");

        when(pacienteRepository.findAll())
                .thenReturn(List.of(paciente1,paciente2));

        when(pacienteMapper.toResponseDTO(paciente1))
                .thenReturn(pacienteResponse1);

        when(pacienteMapper.toResponseDTO(paciente2))
                .thenReturn(pacienteResponse2);

        List<PacienteResponseDTO> resultado = pacienteService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(pacienteResponse1, resultado.get(0));
        assertEquals(pacienteResponse2, resultado.get(1));

        verify(pacienteRepository).findAll();
        verify(pacienteMapper).toResponseDTO(paciente1);
        verify(pacienteMapper).toResponseDTO(paciente2);
    }

    @Test
    public void testarCampoNaoAlterado(){
        Paciente paciente = new Paciente(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                true
        );

        PacienteRequestDTO pacienteRequest = new PacienteRequestDTO(
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2010,05,11),
                null,
                "pedroTeste@gmail.com");

        PacienteResponseDTO pacienteResponse = new PacienteResponseDTO(
                1L,
                "Pedro Henrique de Borba",
                "13681599960",
                LocalDate.of(2010,05,11),
                null,
                "pedroTeste@gmail.com");

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(pacienteMapper.toResponseDTO(paciente))
                .thenReturn(pacienteResponse);

        PacienteResponseDTO resultado = pacienteService.atualizar(1L, pacienteRequest);

        assertEquals(pacienteResponse.cpf(), resultado.cpf());

        verify(pacienteRepository).findById(1L);
        verify(pacienteMapper).toResponseDTO(paciente);
        verify(pacienteMapper).updateEntityFromDto(pacienteRequest,paciente);
    }

    @Test
    public void testarPacienteNaoLocalizadoAtivar(){
        when(pacienteRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                PacienteNaoLocalizadoException.class,
                () -> pacienteService.ativarPaciente(2L)
        );

        verify(pacienteRepository).findById(2L);
    }

    @Test
    public void testarPacienteNaoLocalizadoInativar(){
        when(pacienteRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                PacienteNaoLocalizadoException.class,
                () -> pacienteService.inativarPaciente(2L)
        );

        verify(pacienteRepository).findById(2L);
    }

    @Test
    public void testarAtualizacaoComCpfDuplicado() {

        Paciente paciente = new Paciente(
                1L,
                "Pedro Henrique",
                "13681599960",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com",
                null,
                true
        );

        PacienteRequestDTO pacienteRequest = new PacienteRequestDTO(
                "Pedro Henrique",
                "99999999999",
                LocalDate.of(2005, 5, 11),
                null,
                "pedroTeste@gmail.com"
        );

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(pacienteRepository.existsByCpf("99999999999"))
                .thenReturn(true);

        assertThrows(
                CpfDuplicadoException.class,
                () -> pacienteService.atualizar(1L, pacienteRequest)
        );

        verify(pacienteRepository).findById(1L);
        verify(pacienteRepository).existsByCpf("99999999999");

        verify(pacienteMapper, never())
                .updateEntityFromDto(any(), any());
    }
}
