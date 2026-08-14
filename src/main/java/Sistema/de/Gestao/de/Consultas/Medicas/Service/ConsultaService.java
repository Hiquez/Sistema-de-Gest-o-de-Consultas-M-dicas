package Sistema.de.Gestao.de.Consultas.Medicas.Service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaMapper consultaMapper;

    public ConsultaService(ConsultaRepository consultaRepository,
                           PacienteRepository pacienteRepository,
                           MedicoRepository medicoRepository,
                           ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaMapper = consultaMapper;
    }

    @Transactional
    public ConsultaResponseDTO agendarConsulta(ConsultaRequestDTO consultaRequestDTO) {
        Consulta consulta = validarAgendamentoConsulta(consultaRequestDTO);
        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO confirmarConsulta(Long idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new ConsultaException("Consulta não pode ser confirmada");
        }

        consulta.setStatus(StatusConsulta.CONFIRMADA);
        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO cancelarConsulta(Long idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        boolean podeCancelar = consulta.getStatus() == StatusConsulta.CONFIRMADA
                || consulta.getStatus() == StatusConsulta.AGENDADA;

        if (!podeCancelar) {
            throw new ConsultaException("Consulta não pode ser cancelada");
        }

        consulta.setStatus(StatusConsulta.CANCELADA);
        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO iniciarAtendimento(Long idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        if (consulta.getStatus() != StatusConsulta.CONFIRMADA) {
            throw new ConsultaException("Consulta não pode ser iniciada");
        }

        consulta.setStatus(StatusConsulta.EM_ATENDIMENTO);
        return consultaMapper.toResponse(consulta);
    }

    @Transactional
    public ConsultaResponseDTO concluirConsulta(Long idConsulta) {
        Consulta consulta = buscarEntidadePorId(idConsulta);

        // Só pode concluir se a consulta estiver em atendimento
        if (consulta.getStatus() != StatusConsulta.EM_ATENDIMENTO) {
            throw new ConsultaException("Consulta não pode ser concluída");
        }

        consulta.setStatus(StatusConsulta.CONCLUIDA);
        return consultaMapper.toResponse(consulta);
    }

    @Transactional(readOnly = true)
    public ConsultaResponseDTO buscarConsultaPorId(Long idConsulta) {
        return consultaMapper.toResponse(buscarEntidadePorId(idConsulta));
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarConsultaPorPaciente(Long idPaciente) {
        return consultaRepository.listarConsultasPorPaciente(idPaciente);
    }

    @Transactional(readOnly = true)
    public List<ConsultaResponseDTO> listarConsultaPorMedico(Long idMedico) {
        return consultaRepository.listarConsultasPorMedico(idMedico);
    }

    private Consulta validarAgendamentoConsulta(ConsultaRequestDTO consultaRequestDTO) {
        Paciente pacienteExiste = pacienteRepository.findById(consultaRequestDTO.idPaciente())
                .orElseThrow(() -> new PacienteException("Paciente não encontrado"));

        Medico medicoExiste = medicoRepository.findById(consultaRequestDTO.idMedico())
                .orElseThrow(() -> new MedicoException("Médico não encontrado"));

        if (consultaRepository.existsByMedicoAndDataHora(medicoExiste, consultaRequestDTO.dataHora())) {
            throw new HorarioException("Horário não disponível");
        }

        return salvarConsulta(pacienteExiste, medicoExiste, consultaRequestDTO);
    }

    private Consulta salvarConsulta(Paciente paciente, Medico medico, ConsultaRequestDTO consultaRequestDTO) {
        Consulta consulta = new Consulta();
        consulta.setPaciente(paciente);
        consulta.setMedico(medico);
        consulta.setDataHora(consultaRequestDTO.dataHora());
        consulta.setStatus(StatusConsulta.AGENDADA);
        consulta.setObservacao(consultaRequestDTO.observacao());

        return consultaRepository.save(consulta);
    }

    private Consulta buscarEntidadePorId(Long idConsulta) {
        return consultaRepository.findById(idConsulta)
                .orElseThrow(() -> new ConsultaException("Consulta não encontrada"));
    }
}