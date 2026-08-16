package Sistema.de.Gestao.de.Consultas.Medicas.Service;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Paciente;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.CpfDuplicadoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.PacienteNaoLocalizadoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Mapper.PacienteMapper;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.PacienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    public PacienteService(PacienteRepository pacienteRepository, PacienteMapper pacienteMapper) {
        this.pacienteRepository = pacienteRepository;
        this.pacienteMapper = pacienteMapper;
    }

    @Transactional
    public PacienteResponseDTO cadastrar(PacienteRequestDTO dto) {
        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new CpfDuplicadoException("Já existe paciente cadastrado com este CPF");
        }

        Paciente paciente = pacienteMapper.toEntity(dto);
        Paciente salvo = pacienteRepository.save(paciente);

        return pacienteMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        Paciente paciente = buscarEntidadePorId(id);
        return pacienteMapper.toResponseDTO(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(pacienteMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        Paciente paciente = buscarEntidadePorId(id);

        if (!paciente.getCpf().equals(dto.cpf()) && pacienteRepository.existsByCpf(dto.cpf())) {
            throw new CpfDuplicadoException("Já existe paciente cadastrado com este CPF");
        }

        pacienteMapper.updateEntityFromDto(dto, paciente);
        return pacienteMapper.toResponseDTO(paciente);
    }

    @Transactional
    public PacienteResponseDTO ativarPaciente(Long idPaciente){
        Paciente paciente = buscarEntidadePorId(idPaciente);
        paciente.setStatus(true);
        return pacienteMapper.toResponseDTO(paciente);
    }

    @Transactional
    public PacienteResponseDTO inativarPaciente(Long idPaciente){
        Paciente paciente = buscarEntidadePorId(idPaciente);
        paciente.setStatus(false);
        return pacienteMapper.toResponseDTO(paciente);
    }

    private Paciente buscarEntidadePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoLocalizadoException("Paciente não encontrado com id: " + id));
    }
}
