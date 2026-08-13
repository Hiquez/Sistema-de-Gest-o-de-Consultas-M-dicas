package Sistema.de.Gestao.de.Consultas.Medicas.Service;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.ConsultaRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.StatusConsulta;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Consulta;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.ConsultaRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.MedicoRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.PacienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConsultaService {

    private final ConsultaRepository consultaRepository;
    private final PacienteRepository pacienteRepository;
    private final MedicoRepository medicoRepository;

    public ConsultaService(ConsultaRepository consultaRepository, PacienteRepository pacienteRepository, MedicoRepository medicoRepository) {
        this.consultaRepository = consultaRepository;
        this.pacienteRepository = pacienteRepository;
        this.medicoRepository = medicoRepository;
    }

    public ConsultaResponseDTO agendarConsulta(ConsultaRequestDTO consultaRequestDTO) {
        Consulta consulta = validarAgendamentoConsulta(consultaRequestDTO);
        return new ConsultaResponseDTO(consulta.getIdConsulta(), consulta.getPaciente().getNome(), consulta.getMedico().getNome(), consulta.getDataHora(), consulta.getStatus(), consulta.getObservacao());
    }

    public ConsultaResponseDTO confirmarConsulta(Long IdConsulta) {
        Consulta consulta = consultaRepository.findById(IdConsulta).get();
        if (consulta.getStatus() != StatusConsulta.AGENDADA) {
            throw new RuntimeException("Consulta não pode ser confirmada");
        }
        consulta.setStatus(StatusConsulta.CONFIRMADA);
        consultaRepository.save(consulta);
        return new ConsultaResponseDTO(consulta.getIdConsulta(), consulta.getPaciente().getNome(), consulta.getMedico().getNome(), consulta.getDataHora(), consulta.getStatus(), consulta.getObservacao());
    }

    public ConsultaResponseDTO cancelarConsulta(Long IdConsulta) {
        Optional<Consulta> consulta = Optional.of(consultaRepository.findById(IdConsulta)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada")));

        boolean podeCancelar = consulta.get().getStatus() == StatusConsulta.CONFIRMADA || consulta.get().getStatus() == StatusConsulta.AGENDADA;
        if (!podeCancelar) {
            throw new RuntimeException("Consulta não pode ser cancelada");
        }

        consulta.get().setStatus(StatusConsulta.CANCELADA);
        consultaRepository.save(consulta.get());
        return new ConsultaResponseDTO(consulta.get().getIdConsulta(), consulta.get().getPaciente().getNome(), consulta.get().getMedico().getNome(), consulta.get().getDataHora(), consulta.get().getStatus(), consulta.get().getObservacao());
    }

    public ConsultaResponseDTO iniciarAtendimento(Long idConsulta) {
        Optional<Consulta> consulta = consultaRepository.findById(idConsulta);
        if (consulta.get().getStatus() == StatusConsulta.CONFIRMADA) {
            throw new RuntimeException("Consulta não pode ser iniciada");
        }

        consulta.get().setStatus(StatusConsulta.EM_ATENDIMENTO);
        consultaRepository.save(consulta.get());
        return new ConsultaResponseDTO(consulta.get().getIdConsulta(), consulta.get().getPaciente().getNome(), consulta.get().getMedico().getNome(), consulta.get().getDataHora(), consulta.get().getStatus(), consulta.get().getObservacao());
    }

    public ConsultaResponseDTO concluirConsulta(Long idConsulta) {
        Optional<Consulta> consulta = consultaRepository.findById(idConsulta);
        if (consulta.get().getStatus() == StatusConsulta.EM_ATENDIMENTO) {
            throw new RuntimeException("Consulta não pode ser concluída");
        }

        consulta.get().setStatus(StatusConsulta.CONCLUIDA);
        consultaRepository.save(consulta.get());
        return new ConsultaResponseDTO(consulta.get().getIdConsulta(), consulta.get().getPaciente().getNome(), consulta.get().getMedico().getNome(), consulta.get().getDataHora(), consulta.get().getStatus(), consulta.get().getObservacao());
    }

    public ConsultaResponseDTO buscarConsultaPorId(Long idConsulta) {
        Optional<Consulta> consulta = consultaRepository.findById(idConsulta);
        if (consulta.isPresent()) {
            return new ConsultaResponseDTO(consulta.get().getIdConsulta(), consulta.get().getPaciente().getNome(), consulta.get().getMedico().getNome(), consulta.get().getDataHora(), consulta.get().getStatus(), consulta.get().getObservacao());
        } else {
            throw new RuntimeException("Consulta não encontrada");
        }
    }

    public List<ConsultaResponseDTO> listarConsultaPorPaciente(Long idPaciente){
        return consultaRepository.listarConsultasPorPaciente(idPaciente).stream()
                .map(consulta -> new ConsultaResponseDTO(consulta.idConsulta(), consulta.nomePaciente(), consulta.nomeMedico(), consulta.dataHora(), consulta.status(), consulta.observacao()))
                .collect(Collectors.toList());
    }

    public List<ConsultaResponseDTO> listarConsultaPorMedico(Long idMedico){
        return consultaRepository.listarConsultasPorMedico(idMedico).stream()
                .map(consulta -> new ConsultaResponseDTO(consulta.idConsulta(), consulta.nomePaciente(), consulta.nomeMedico(), consulta.dataHora(), consulta.status(), consulta.observacao()))
                .collect(Collectors.toList());
    }


    private Consulta validarAgendamentoConsulta(ConsultaRequestDTO  consultaRequestDTO) {
        Paciente pacienteExiste = pacienteRepository.findById(consultaRequestDTO.idPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado"));

        Medico medicoExiste = medicoRepository.findById(consultaRequestDTO.idMedico())
                .orElseThrow(() -> new RuntimeException("Médico não encontrado"));

        if(consultaRepository.existsByMedicoAndDataHora(medicoExiste, consultaRequestDTO.dataHora())) {
            throw new RuntimeException("Horário não disponível");
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
