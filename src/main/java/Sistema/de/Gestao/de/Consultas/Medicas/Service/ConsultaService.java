package Sistema.de.Gestao.de.Consultas.Medicas.Service;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaAgendadaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaCanceladaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConcluidaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConfirmadaEvent;
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
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaMapper consultaMapper;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final MeterRegistry meterRegistry;

    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteRepository pacienteRepository,
                           MedicoRepository medicoRepository,
                           ConsultaMapper consultaMapper,
                           ApplicationEventPublisher applicationEventPublisher,
                           MeterRegistry meterRegistry) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaMapper = consultaMapper;
        this.applicationEventPublisher = applicationEventPublisher;
        this.meterRegistry = meterRegistry;
    }

    @Transactional
    public ConsultaResponseDTO agendarConsulta(ConsultaRequestDTO consultaRequestDTO) {
        Consulta consulta = validarAgendamentoConsulta(consultaRequestDTO);

        applicationEventPublisher.publishEvent(
                new ConsultaAgendadaEvent(
                        consulta.getIdConsulta(),
                        consulta.getPaciente().getNome(),
                        consulta.getPaciente().getEmail(),
                        consulta.getMedico().getNome(),
                        consulta.getDataHora()
                )
        );

        meterRegistry.counter("consultas.total.agendada", "status","AGENDADA").increment();

        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO confirmarConsulta(UUID idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new ConsultaException("Consulta não pode ser confirmada");
        }

        consulta.setStatus(StatusConsulta.CONFIRMADA);

        applicationEventPublisher.publishEvent(
                new ConsultaConfirmadaEvent(
                        consulta.getIdConsulta(),
                        consulta.getPaciente().getNome(),
                        consulta.getPaciente().getEmail(),
                        consulta.getMedico().getNome(),
                        consulta.getDataHora()
                )
        );

        meterRegistry.counter("consultas.total.confirmada", "status","CONFIRMADA").increment();

        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO cancelarConsulta(UUID idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        boolean podeCancelar = consulta.getStatus() == StatusConsulta.CONFIRMADA
                || consulta.getStatus() == StatusConsulta.AGENDADA;

        if (!podeCancelar) {
            throw new ConsultaException("Consulta não pode ser cancelada");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);

        applicationEventPublisher.publishEvent(
                new ConsultaCanceladaEvent(
                        consulta.getIdConsulta(),
                        consulta.getPaciente().getNome(),
                        consulta.getPaciente().getEmail(),
                        consulta.getMedico().getNome(),
                        consulta.getDataHora()
                )
        );

        meterRegistry.counter("consultas.total.cancelada", "status","CANCELADA").increment();

        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO iniciarAtendimento(UUID idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        if (consulta.getStatus() != StatusConsulta.CONFIRMADA) {
            throw new ConsultaException("Consulta não pode ser iniciada");
        }

        consulta.setStatus(StatusConsulta.EM_ATENDIMENTO);

        meterRegistry.counter("consultas.total.iniciadas", "status","EM_ATENDIMENTO").increment();

        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO concluirConsulta(UUID idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        if (consulta.getStatus() != StatusConsulta.EM_ATENDIMENTO) {
            throw new ConsultaException("Consulta não pode ser concluída");
        }

        consulta.setStatus(StatusConsulta.CONCLUIDA);

        applicationEventPublisher.publishEvent(
                new ConsultaConcluidaEvent(
                        consulta.getIdConsulta(),
                        consulta.getPaciente().getNome(),
                        consulta.getPaciente().getEmail(),
                        consulta.getMedico().getNome(),
                        consulta.getDataHora()
                )
        );

        meterRegistry.counter("consulta.total.concluida", "status","CONCLUIDA").increment();
        return consultaMapper.toResponse(consulta);
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscarConsultaPorId(UUID idConsulta) {
        return consultaMapper.toResponse(
                buscarEntidadePorId(idConsulta)
        );
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarConsultaPorPaciente(Long idPaciente) {
        return consultaRepository
                .findByPaciente_IdPaciente(idPaciente)
                .stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarConsultaPorMedico(Long idMedico) {
        return consultaRepository
                .findByMedico_IdMedico(idMedico)
                .stream()
                .map(consultaMapper::toResponse)
                .toList();
    }

    private Consulta validarAgendamentoConsulta(
            ConsultaRequestDTO consultaRequestDTO) {

        Paciente pacienteExiste = pacienteRepository
                .findById(consultaRequestDTO.idPaciente())
                .orElseThrow(() ->
                        new PacienteException("Paciente não encontrado"));

        Medico medicoExiste = medicoRepository
                .findById(consultaRequestDTO.idMedico())
                .orElseThrow(() ->
                        new MedicoException("Médico não encontrado"));

        if (consultaRepository.existsByMedicoAndDataHora(
                medicoExiste,
                consultaRequestDTO.dataHora())) {

            throw new HorarioException("Horário não disponível");
        }

        return salvarConsulta(
                pacienteExiste,
                medicoExiste,
                consultaRequestDTO
        );
    }

    private Consulta salvarConsulta(
            Paciente paciente,
            Medico medico,
            ConsultaRequestDTO consultaRequestDTO) {

        Consulta consulta = new Consulta();

        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(consultaRequestDTO.dataHora());
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setObservacao(consultaRequestDTO.observacao());

        return consultaRepository.save(consulta);
    }

    private Consulta buscarEntidadePorId(UUID idConsulta) {
        return consultaRepository.findById(idConsulta)
                .orElseThrow(() ->
                        new ConsultaException("Consulta não encontrada"));
    }
}