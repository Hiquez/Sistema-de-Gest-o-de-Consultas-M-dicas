package Sistema.de.Gestao.de.Consultas.Medicas.Service;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.CRMDuplicadoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.MedicoNaoEncontradoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Mapper.MedicoMapper;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.MedicoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class MedicoService {

    private final MedicoRepository medicoRepository;
    private final MedicoMapper medicoMapper;

    public MedicoService(MedicoRepository medicoRepository, MedicoMapper medicoMapper) {
        this.medicoRepository = medicoRepository;
        this.medicoMapper = medicoMapper;
    }

    @Transactional
    public MedicoResponseDTO cadastrar(MedicoRequestDTO dto) {
        if (medicoRepository.existsByCrm(dto.crm())) {
            throw new CRMDuplicadoException("Já existe médico cadastrado com este CRM");
        }

        Medico medico = medicoMapper.toEntity(dto);
        Medico salvo = medicoRepository.save(medico);

        return medicoMapper.toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public MedicoResponseDTO buscarPorId(Long id) {
        return medicoMapper.toResponseDTO(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public List<MedicoResponseDTO> listarTodos() {
        return medicoRepository.findAll()
                .stream()
                .map(medicoMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MedicoResponseDTO> listarPorEspecialidade(Especialidade especialidade) {
        return medicoRepository.findByEspecialidade(especialidade)
                .stream()
                .map(medicoMapper::toResponseDTO)
                .toList();
    }

    @Transactional
    public MedicoResponseDTO atualizar(Long id, MedicoRequestDTO dto) {
        Medico medico = buscarEntidadePorId(id);

        if (!medico.getCrm().equals(dto.crm()) && medicoRepository.existsByCrm(dto.crm())) {
            throw new CRMDuplicadoException("Já existe médico cadastrado com este CRM");
        }

        medicoMapper.updateEntity(medico, dto);
        return medicoMapper.toResponseDTO(medico);
    }

    @Transactional
    public MedicoResponseDTO ativarMedico(Long id) {
        Medico medico = buscarEntidadePorId(id);
        medico.setStatus(true);
        return medicoMapper.toResponseDTO(medico);
    }

    @Transactional
    public MedicoResponseDTO inativarMedico(Long id) {
        Medico medico = buscarEntidadePorId(id);
        medico.setStatus(false);
        return medicoMapper.toResponseDTO(medico);
    }

    private Medico buscarEntidadePorId(Long id) {
        return medicoRepository.findById(id)
                .orElseThrow(() -> new MedicoNaoEncontradoException("Médico não encontrado com id: " + id));
    }
}
