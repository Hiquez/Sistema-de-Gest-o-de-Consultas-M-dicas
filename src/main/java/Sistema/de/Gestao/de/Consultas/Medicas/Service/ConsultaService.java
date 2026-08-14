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
import java.util.List;
import java.util.Optional;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;
    private final ConsultaMapper  consultaMapper;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, MedicoRepository medicoRepository, ConsultaMapper consultaMapper) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
        this.consultaMapper = consultaMapper;
    }

    public ConsultaResponseDTO agendarConsulta(ConsultaRequestDTO consultaRequestDTO) {
        Consulta consulta = validarAgendamentoConsulta(consultaRequestDTO);
        return consultaMapper.toResponse(consulta);
    }

    public ConsultaResponseDTO confirmarConsulta(Long IdConsulta) {
        Consulta consulta = consultaRepository.findById(IdConsulta).get();
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new ConsultaException("Consulta não pode ser confirmada");
        }
        consulta.setStatus(StatusConsulta.CONFIRMADA);
        consultaRepository.save(consulta);
        return consultaMapper.toResponse(consulta);
    }

    public ConsultaResponseDTO cancelarConsulta(Long IdConsulta) {
        Optional<Consulta> consulta = Optional.of(consultaRepository.findById(IdConsulta)
                .orElseThrow(() -> new ConsultaException("Consulta não encontrada")));

        boolean podeCancelar = consulta.get().getStatus() == StatusConsulta.CONFIRMADA || consulta.get().getStatus() == StatusConsulta.AGENDADA;
        if (!podeCancelar) {
            throw new ConsultaException("Consulta não pode ser cancelada");
        }

        consulta.get().setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta.get());
        return consultaMapper.toResponse(consulta.get());
    }

    public ConsultaResponseDTO iniciarAtendimento(Long idConsulta) {
        Optional<Consulta> consulta = consultaRepository.findById(idConsulta);
        if (consulta.get().getStatus() == StatusConsulta.CONFIRMADA) {
            throw new ConsultaException("Consulta não pode ser iniciada");
        }

        consulta.get().setStatus(StatusConsulta.EM_ATENDIMENTO);
        consultaRepository.save(consulta.get());
        return consultaMapper.toResponse(consulta.get());
    }

    public ConsultaResponseDTO concluirConsulta(Long idConsulta) {
        Optional<Consulta> consulta = consultaRepository.findById(idConsulta);
        if (consulta.get().getStatus() == StatusConsulta.EM_ATENDIMENTO) {
            throw new ConsultaException("Consulta não pode ser concluída");
        }

        consulta.get().setStatus(StatusConsulta.CONCLUIDA);
        consultaRepository.save(consulta.get());
        return consultaMapper.toResponse(consulta.get());
    }

    public ConsultaResponseDTO buscarConsultaPorId(Long idConsulta) {
        Optional<Consulta> consulta = consultaRepository.findById(idConsulta);
        if (consulta.isPresent()) {
            return consultaMapper.toResponse(consulta.get());
        } else {
            throw new ConsultaException("Consulta não encontrada");
        }
    }

    public List<ConsultaResponseDTO> listarConsultaPorPaciente(Long idPaciente) {
        return consultaRepository.listarConsultasPorPaciente(idPaciente);
    }

    public List<ConsultaResponseDTO> listarConsultaPorMedico(Long idMedico) {
        return consultaRepository.listarConsultasPorMedico(idMedico);
    }


    private Consulta validarAgendamentoConsulta(ConsultaRequestDTO  consultaRequestDTO) {
        Paciente pacienteExiste = pacienteRepository.findById(consultaRequestDTO.idPaciente())
                .orElseThrow(() -> new PacienteException("Paciente não encontrado"));

        Medico medicoExiste = medicoRepository.findById(consultaRequestDTO.idMedico())
                .orElseThrow(() -> new MedicoException("Médico não encontrado"));

        if(consultaRepository.existsByMedicoAndDataHora(medicoExiste, consultaRequestDTO.dataHora())) {
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
        consultaRepository.save(consulta);
        return consulta;
    }
}
