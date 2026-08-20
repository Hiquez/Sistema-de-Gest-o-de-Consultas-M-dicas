package Sistema.de.Gestao.de.Consultas.Medicas.Consulta;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.StatusConsulta;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Consulta;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.ConsultaException;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.HorarioException;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.MedicoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.PacienteException;
import Sistema.de.Gestao.de.Consultas.Medicas.Mapper.ConsultaMapper;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.ConsultaRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.MedicoRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.PacienteRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.ConsultaService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConsultaTest {

    @Mock
    private ConsultaRepository consultaRepository;

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private ConsultaMapper consultaMapper;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private ConsultaService consultaService;

    private final UUID idConsulta = UUID.randomUUID();
    private final UUID idConsultaNaoEncontrada = UUID.randomUUID();

    private Paciente criarPacienteValido() {
        Paciente paciente = new Paciente();
        paciente.setNome("Maria Silva");
        paciente.setEmail("maria@email.com");
        return paciente;
    }

    private Medico criarMedicoValido() {
        Medico medico = new Medico();
        medico.setNome("Dr. João Souza");
        return medico;
    }

    @Test
    public void testarAgendamentoConsulta() {

        ConsultaRequestDTO consultaRequest =
                mock(ConsultaRequestDTO.class);

        Paciente paciente = criarPacienteValido();
        Medico medico = criarMedicoValido();

        LocalDateTime dataHora =
                LocalDateTime.of(2026, 8, 20, 14, 0);

        Consulta consultaSalva = new Consulta();
        consultaSalva.setPaciente(paciente);
        consultaSalva.setMedico(medico);
        consultaSalva.setDataHora(dataHora);

        ConsultaResponseDTO consultaResponse =
                mock(ConsultaResponseDTO.class);

        when(consultaRequest.idPaciente())
                .thenReturn(1L);

        when(consultaRequest.idMedico())
                .thenReturn(1L);

        when(consultaRequest.dataHora())
                .thenReturn(dataHora);

        when(consultaRequest.observacao())
                .thenReturn("Consulta de rotina");

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(consultaRepository.existsByMedicoAndDataHora(
                medico,
                dataHora
        )).thenReturn(false);

        when(consultaRepository.save(any(Consulta.class)))
                .thenReturn(consultaSalva);

        when(consultaMapper.toResponse(consultaSalva))
                .thenReturn(consultaResponse);

        ConsultaResponseDTO resultado =
                consultaService.agendarConsulta(consultaRequest);

        assertEquals(consultaResponse, resultado);

        verify(pacienteRepository)
                .findById(1L);

        verify(medicoRepository)
                .findById(1L);

        verify(consultaRepository)
                .existsByMedicoAndDataHora(
                        medico,
                        dataHora
                );

        verify(consultaRepository)
                .save(any(Consulta.class));

        verify(consultaMapper)
                .toResponse(consultaSalva);
    }

    @Test
    public void testarAgendamentoPacienteNaoEncontrado() {

        ConsultaRequestDTO consultaRequest =
                mock(ConsultaRequestDTO.class);

        when(consultaRequest.idPaciente())
                .thenReturn(1L);

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                PacienteException.class,
                () -> consultaService.agendarConsulta(consultaRequest)
        );

        verify(pacienteRepository)
                .findById(1L);

        verify(medicoRepository, never())
                .findById(any());

        verify(consultaRepository, never())
                .save(any());
    }

    @Test
    public void testarAgendamentoMedicoNaoEncontrado() {

        ConsultaRequestDTO consultaRequest =
                mock(ConsultaRequestDTO.class);

        when(consultaRequest.idPaciente())
                .thenReturn(1L);

        when(consultaRequest.idMedico())
                .thenReturn(1L);

        Paciente paciente = new Paciente();

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.empty());

        assertThrows(
                MedicoException.class,
                () -> consultaService.agendarConsulta(consultaRequest)
        );

        verify(pacienteRepository)
                .findById(1L);

        verify(medicoRepository)
                .findById(1L);

        verify(consultaRepository, never())
                .save(any());
    }

    @Test
    public void testarHorarioIndisponivel() {

        ConsultaRequestDTO consultaRequest =
                mock(ConsultaRequestDTO.class);

        Paciente paciente = new Paciente();
        Medico medico = new Medico();

        LocalDateTime dataHora =
                LocalDateTime.of(2026, 8, 20, 14, 0);

        when(consultaRequest.idPaciente())
                .thenReturn(1L);

        when(consultaRequest.idMedico())
                .thenReturn(1L);

        when(consultaRequest.dataHora())
                .thenReturn(dataHora);

        when(pacienteRepository.findById(1L))
                .thenReturn(Optional.of(paciente));

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(consultaRepository.existsByMedicoAndDataHora(
                medico,
                dataHora
        )).thenReturn(true);

        assertThrows(
                HorarioException.class,
                () -> consultaService.agendarConsulta(consultaRequest)
        );

        verify(consultaRepository)
                .existsByMedicoAndDataHora(
                        medico,
                        dataHora
                );

        verify(consultaRepository, never())
                .save(any());
    }

    @Test
    public void testarConfirmarConsulta() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setPaciente(criarPacienteValido());
        consulta.setMedico(criarMedicoValido());

        ConsultaResponseDTO consultaResponse =
                mock(ConsultaResponseDTO.class);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        when(consultaMapper.toResponse(consulta))
                .thenReturn(consultaResponse);

        ConsultaResponseDTO resultado =
                consultaService.confirmarConsulta(idConsulta);

        assertEquals(
                StatusConsulta.CONFIRMADA,
                consulta.getStatus()
        );

        assertEquals(
                consultaResponse,
                resultado
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper)
                .toResponse(consulta);
    }

    @Test
    public void testarConfirmarConsultaStatusInvalido() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.CANCELADA);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        assertThrows(
                ConsultaException.class,
                () -> consultaService.confirmarConsulta(idConsulta)
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper, never())
                .toResponse(any());
    }

    @Test
    public void testarCancelarConsultaAgendada() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setPaciente(criarPacienteValido());
        consulta.setMedico(criarMedicoValido());

        ConsultaResponseDTO consultaResponse =
                mock(ConsultaResponseDTO.class);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        when(consultaMapper.toResponse(consulta))
                .thenReturn(consultaResponse);

        ConsultaResponseDTO resultado =
                consultaService.cancelarConsulta(idConsulta);

        assertEquals(
                StatusConsulta.CANCELADA,
                consulta.getStatus()
        );

        assertEquals(
                consultaResponse,
                resultado
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper)
                .toResponse(consulta);
    }

    @Test
    public void testarCancelarConsultaConfirmada() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.CONFIRMADA);
        consulta.setPaciente(criarPacienteValido());
        consulta.setMedico(criarMedicoValido());

        ConsultaResponseDTO consultaResponse =
                mock(ConsultaResponseDTO.class);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        when(consultaMapper.toResponse(consulta))
                .thenReturn(consultaResponse);

        ConsultaResponseDTO resultado =
                consultaService.cancelarConsulta(idConsulta);

        assertEquals(
                StatusConsulta.CANCELADA,
                consulta.getStatus()
        );

        assertEquals(
                consultaResponse,
                resultado
        );

        verify(consultaMapper)
                .toResponse(consulta);
    }

    @Test
    public void testarCancelarConsultaStatusInvalido() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.CONCLUIDA);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        assertThrows(
                ConsultaException.class,
                () -> consultaService.cancelarConsulta(idConsulta)
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper, never())
                .toResponse(any());
    }

    @Test
    public void testarIniciarAtendimento() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.CONFIRMADA);

        ConsultaResponseDTO consultaResponse =
                mock(ConsultaResponseDTO.class);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        when(consultaMapper.toResponse(consulta))
                .thenReturn(consultaResponse);

        ConsultaResponseDTO resultado =
                consultaService.iniciarAtendimento(idConsulta);

        assertEquals(
                StatusConsulta.EM_ATENDIMENTO,
                consulta.getStatus()
        );

        assertEquals(
                consultaResponse,
                resultado
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper)
                .toResponse(consulta);
    }

    @Test
    public void testarIniciarAtendimentoStatusInvalido() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.AGENDADA);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        assertThrows(
                ConsultaException.class,
                () -> consultaService.iniciarAtendimento(idConsulta)
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper, never())
                .toResponse(any());
    }

    @Test
    public void testarConcluirConsulta() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.EM_ATENDIMENTO);
        consulta.setPaciente(criarPacienteValido());
        consulta.setMedico(criarMedicoValido());

        ConsultaResponseDTO consultaResponse =
                mock(ConsultaResponseDTO.class);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        when(consultaMapper.toResponse(consulta))
                .thenReturn(consultaResponse);

        ConsultaResponseDTO resultado =
                consultaService.concluirConsulta(idConsulta);

        assertEquals(
                StatusConsulta.CONCLUIDA,
                consulta.getStatus()
        );

        assertEquals(
                consultaResponse,
                resultado
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper)
                .toResponse(consulta);
    }

    @Test
    public void testarConcluirConsultaStatusInvalido() {

        Consulta consulta = new Consulta();

        consulta.setStatus(StatusConsulta.CONFIRMADA);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        assertThrows(
                ConsultaException.class,
                () -> consultaService.concluirConsulta(idConsulta)
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper, never())
                .toResponse(any());
    }

    @Test
    public void testarBuscarConsultaPorId() {

        Consulta consulta = new Consulta();

        ConsultaResponseDTO consultaResponse =
                mock(ConsultaResponseDTO.class);

        when(consultaRepository.findById(idConsulta))
                .thenReturn(Optional.of(consulta));

        when(consultaMapper.toResponse(consulta))
                .thenReturn(consultaResponse);

        ConsultaResponseDTO resultado =
                consultaService.buscarConsultaPorId(idConsulta);

        assertEquals(
                consultaResponse,
                resultado
        );

        verify(consultaRepository)
                .findById(idConsulta);

        verify(consultaMapper)
                .toResponse(consulta);
    }

    @Test
    public void testarConsultaNaoEncontrada() {

        when(consultaRepository.findById(idConsultaNaoEncontrada))
                .thenReturn(Optional.empty());

        assertThrows(
                ConsultaException.class,
                () -> consultaService.buscarConsultaPorId(
                        idConsultaNaoEncontrada
                )
        );

        verify(consultaRepository)
                .findById(idConsultaNaoEncontrada);

        verify(consultaMapper, never())
                .toResponse(any());
    }

    @Test
    public void testarListarConsultaPorPaciente() {

        Consulta consulta1 = mock(Consulta.class);
        Consulta consulta2 = mock(Consulta.class);

        ConsultaResponseDTO response1 = mock(ConsultaResponseDTO.class);
        ConsultaResponseDTO response2 = mock(ConsultaResponseDTO.class);

        List<Consulta> consultas = List.of(consulta1, consulta2);

        when(consultaRepository.findByPaciente_IdPaciente(1L))
                .thenReturn(consultas);

        when(consultaMapper.toResponse(consulta1))
                .thenReturn(response1);

        when(consultaMapper.toResponse(consulta2))
                .thenReturn(response2);

        List<ConsultaResponseDTO> resultado =
                consultaService.listarConsultaPorPaciente(1L);

        assertEquals(
                2,
                resultado.size()
        );

        assertEquals(
                List.of(response1, response2),
                resultado
        );

        verify(consultaRepository)
                .findByPaciente_IdPaciente(1L);

        verify(consultaMapper)
                .toResponse(consulta1);

        verify(consultaMapper)
                .toResponse(consulta2);
    }

    @Test
    public void testarListarConsultaPorPacienteVazio() {

        when(consultaRepository.findByPaciente_IdPaciente(1L))
                .thenReturn(Collections.emptyList());

        List<ConsultaResponseDTO> resultado =
                consultaService.listarConsultaPorPaciente(1L);

        assertTrue(resultado.isEmpty());

        verify(consultaRepository)
                .findByPaciente_IdPaciente(1L);
    }

    @Test
    public void testarListarConsultaPorMedico() {

        Consulta consulta1 = mock(Consulta.class);
        Consulta consulta2 = mock(Consulta.class);

        ConsultaResponseDTO response1 =
                mock(ConsultaResponseDTO.class);

        ConsultaResponseDTO response2 =
                mock(ConsultaResponseDTO.class);

        List<Consulta> consultas =
                List.of(consulta1, consulta2);

        when(consultaRepository.findByMedico_IdMedico(1L))
                .thenReturn(consultas);

        when(consultaMapper.toResponse(consulta1))
                .thenReturn(response1);

        when(consultaMapper.toResponse(consulta2))
                .thenReturn(response2);

        List<ConsultaResponseDTO> resultado =
                consultaService.listarConsultaPorMedico(1L);

        assertEquals(
                2,
                resultado.size()
        );

        assertEquals(
                List.of(response1, response2),
                resultado
        );

        verify(consultaRepository)
                .findByMedico_IdMedico(1L);

        verify(consultaMapper)
                .toResponse(consulta1);

        verify(consultaMapper)
                .toResponse(consulta2);
    }

    @Test
    public void testarListarConsultaPorMedicoVazio() {

        when(consultaRepository.findByMedico_IdMedico(1L))
                .thenReturn(Collections.emptyList());

        List<ConsultaResponseDTO> resultado =
                consultaService.listarConsultaPorMedico(1L);

        assertTrue(resultado.isEmpty());

        verify(consultaRepository)
                .findByMedico_IdMedico(1L);
    }

    @Test
    public void testarConfirmarConsultaNaoEncontrada() {

        when(consultaRepository.findById(idConsultaNaoEncontrada))
                .thenReturn(Optional.empty());

        assertThrows(
                ConsultaException.class,
                () -> consultaService.confirmarConsulta(
                        idConsultaNaoEncontrada
                )
        );

        verify(consultaRepository)
                .findById(idConsultaNaoEncontrada);
    }

    @Test
    public void testarCancelarConsultaNaoEncontrada() {

        when(consultaRepository.findById(idConsultaNaoEncontrada))
                .thenReturn(Optional.empty());

        assertThrows(
                ConsultaException.class,
                () -> consultaService.cancelarConsulta(
                        idConsultaNaoEncontrada
                )
        );

        verify(consultaRepository)
                .findById(idConsultaNaoEncontrada);
    }

    @Test
    public void testarIniciarAtendimentoNaoEncontrado() {

        when(consultaRepository.findById(idConsultaNaoEncontrada))
                .thenReturn(Optional.empty());

        assertThrows(
                ConsultaException.class,
                () -> consultaService.iniciarAtendimento(
                        idConsultaNaoEncontrada
                )
        );

        verify(consultaRepository)
                .findById(idConsultaNaoEncontrada);
    }

    @Test
    public void testarConcluirConsultaNaoEncontrada() {

        when(consultaRepository.findById(idConsultaNaoEncontrada))
                .thenReturn(Optional.empty());

        assertThrows(
                ConsultaException.class,
                () -> consultaService.concluirConsulta(
                        idConsultaNaoEncontrada
                )
        );

        verify(consultaRepository)
                .findById(idConsultaNaoEncontrada);
    }
}