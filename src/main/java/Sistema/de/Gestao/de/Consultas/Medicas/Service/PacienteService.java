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

    public PacienteService(PacienteRepository pacienteRepository) {
        this.pacienteRepository = pacienteRepository;
    }

    @Transactional
    public PacienteResponseDTO cadastrar(PacienteRequestDTO dto) {
        if (pacienteRepository.existsByCpf(dto.cpf())) {
            throw new CpfDuplicadoException("Já existe paciente cadastrado com este CPF");
        }

        Paciente paciente = PacienteMapper.toEntity(dto);
        Paciente salvo = pacienteRepository.save(paciente);

        return PacienteMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public PacienteResponseDTO buscarPorId(Long id) {
        Paciente paciente = buscarEntidadePorId(id);
        return PacienteMapper.toResponseDTO(paciente);
    }

    @Transactional(readOnly = true)
    public List<PacienteResponseDTO> listarTodos() {
        return pacienteRepository.findAll()
                .stream()
                .map(PacienteMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public PacienteResponseDTO atualizar(Long id, PacienteRequestDTO dto) {
        Paciente paciente = buscarEntidadePorId(id);

        if (!paciente.getCpf().equals(dto.cpf()) && pacienteRepository.existsByCpf(dto.cpf())) {
            throw new CpfDuplicadoException("Já existe paciente cadastrado com este CPF");
        }

        PacienteMapper.updateEntityFromDto(dto, paciente);
        return PacienteMapper.toResponseDTO(paciente);
    }

    @Transactional
    public void excluir(Long id) {
        Paciente paciente = buscarEntidadePorId(id);
        pacienteRepository.delete(paciente);
    }

    private Paciente buscarEntidadePorId(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNaoLocalizadoException("Paciente não encontrado com id: " + id));
    }
}
