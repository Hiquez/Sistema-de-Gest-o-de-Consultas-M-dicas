package Sistema.de.Gestao.de.Consultas.Medicas.Medico;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import Sistema.de.Gestao.de.Consultas.Medicas.Entidade.Medico;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.CRMDuplicadoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.MedicoNaoEncontradoException;
import Sistema.de.Gestao.de.Consultas.Medicas.Mapper.MedicoMapper;
import Sistema.de.Gestao.de.Consultas.Medicas.Repository.MedicoRepository;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.MedicoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MedicoTest {

    @Mock
    private MedicoRepository medicoRepository;

    @Mock
    private MedicoMapper medicoMapper;

    @InjectMocks
    private MedicoService medicoService;


    @Test
    public void testarCadastroMedico() {

        MedicoRequestDTO medicoRequest = new MedicoRequestDTO(
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        Medico medico = new Medico();

        when(medicoMapper.toEntity(medicoRequest))
                .thenReturn(medico);

        when(medicoRepository.save(medico))
                .thenReturn(medico);

        when(medicoMapper.toResponseDTO(medico))
                .thenReturn(medicoResponse);

        MedicoResponseDTO resultado =
                medicoService.cadastrar(medicoRequest);

        assertEquals(medicoResponse, resultado);

        verify(medicoRepository).save(medico);
        verify(medicoMapper).toEntity(medicoRequest);
        verify(medicoMapper).toResponseDTO(medico);
    }


    @Test
    public void testarBuscaPorId() {

        Medico medico = new Medico(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA,
                null,
                true
        );

        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(medicoMapper.toResponseDTO(medico))
                .thenReturn(medicoResponse);

        MedicoResponseDTO resultado =
                medicoService.buscarPorId(1L);

        assertEquals(medicoResponse, resultado);

        verify(medicoRepository).findById(1L);
        verify(medicoMapper).toResponseDTO(medico);
    }


    @Test
    public void testarAtualizarMedico() {

        Medico medico = new Medico(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA,
                null,
                true
        );

        MedicoRequestDTO medicoRequest = new MedicoRequestDTO(
                "Dr. Pedro Henrique de Borba",
                "123456",
                Especialidade.ORTOPEDIA
        );

        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                1L,
                "Dr. Pedro Henrique de Borba",
                "123456",
                Especialidade.ORTOPEDIA
        );

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(medicoMapper.toResponseDTO(medico))
                .thenReturn(medicoResponse);

        MedicoResponseDTO resultado =
                medicoService.atualizar(1L, medicoRequest);

        assertEquals(medicoResponse, resultado);

        verify(medicoRepository).findById(1L);
        verify(medicoMapper).updateEntity(medico, medicoRequest);
        verify(medicoMapper).toResponseDTO(medico);

        verify(medicoRepository, never()).existsByCrm(any());
    }


    @Test
    public void testarAtivarMedico() {

        Medico medico = new Medico(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA,
                null,
                false
        );

        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(medicoMapper.toResponseDTO(medico))
                .thenReturn(medicoResponse);

        MedicoResponseDTO resultado =
                medicoService.ativarMedico(1L);

        assertTrue(medico.getStatus());
        assertEquals(medicoResponse, resultado);

        verify(medicoRepository).findById(1L);
        verify(medicoMapper).toResponseDTO(medico);
    }


    @Test
    public void testarInativarMedico() {

        Medico medico = new Medico(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA,
                null,
                true
        );

        MedicoResponseDTO medicoResponse = new MedicoResponseDTO(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(medicoMapper.toResponseDTO(medico))
                .thenReturn(medicoResponse);

        MedicoResponseDTO resultado =
                medicoService.inativarMedico(1L);

        assertFalse(medico.getStatus());
        assertEquals(medicoResponse, resultado);

        verify(medicoRepository).findById(1L);
        verify(medicoMapper).toResponseDTO(medico);
    }


    @Test
    public void testarCRMDuplicado() {

        MedicoRequestDTO medicoRequest = new MedicoRequestDTO(
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        when(medicoRepository.existsByCrm(medicoRequest.crm()))
                .thenReturn(true);

        assertThrows(
                CRMDuplicadoException.class,
                () -> medicoService.cadastrar(medicoRequest)
        );

        verify(medicoRepository).existsByCrm(medicoRequest.crm());
        verify(medicoRepository, never()).save(any());
        verify(medicoMapper, never()).toEntity(any());
    }


    @Test
    public void testarIdNaoLocalizado() {

        when(medicoRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                MedicoNaoEncontradoException.class,
                () -> medicoService.buscarPorId(2L)
        );

        verify(medicoRepository).findById(2L);
    }


    @Test
    public void testarListaVazia() {

        when(medicoRepository.findAll())
                .thenReturn(Collections.emptyList());

        List<MedicoResponseDTO> resultado =
                medicoService.listarTodos();

        assertTrue(resultado.isEmpty());

        verify(medicoRepository).findAll();
    }


    @Test
    public void testarListaComMedicos() {

        Medico medico1 = new Medico(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA,
                null,
                true
        );

        Medico medico2 = new Medico(
                2L,
                "Dra. Maria Silva",
                "654321",
                Especialidade.ORTOPEDIA,
                null,
                true
        );

        MedicoResponseDTO medicoResponse1 = new MedicoResponseDTO(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        MedicoResponseDTO medicoResponse2 = new MedicoResponseDTO(
                2L,
                "Dra. Maria Silva",
                "654321",
                Especialidade.ORTOPEDIA
        );

        when(medicoRepository.findAll())
                .thenReturn(List.of(medico1, medico2));

        when(medicoMapper.toResponseDTO(medico1))
                .thenReturn(medicoResponse1);

        when(medicoMapper.toResponseDTO(medico2))
                .thenReturn(medicoResponse2);

        List<MedicoResponseDTO> resultado =
                medicoService.listarTodos();

        assertEquals(2, resultado.size());
        assertEquals(medicoResponse1, resultado.get(0));
        assertEquals(medicoResponse2, resultado.get(1));

        verify(medicoRepository).findAll();
        verify(medicoMapper).toResponseDTO(medico1);
        verify(medicoMapper).toResponseDTO(medico2);
    }


    @Test
    public void testarAtualizacaoComCRMDuplicado() {

        Medico medico = new Medico(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.ORTOPEDIA,
                null,
                true
        );

        MedicoRequestDTO medicoRequest = new MedicoRequestDTO(
                "Dr. Pedro Henrique",
                "999999",
                Especialidade.CARDIOLOGIA
        );

        when(medicoRepository.findById(1L))
                .thenReturn(Optional.of(medico));

        when(medicoRepository.existsByCrm("999999"))
                .thenReturn(true);

        assertThrows(
                CRMDuplicadoException.class,
                () -> medicoService.atualizar(1L, medicoRequest)
        );

        verify(medicoRepository).findById(1L);
        verify(medicoRepository).existsByCrm("999999");

        verify(medicoMapper, never())
                .updateEntity(any(), any());
    }


    @Test
    public void testarMedicoNaoLocalizadoAtivar() {

        when(medicoRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                MedicoNaoEncontradoException.class,
                () -> medicoService.ativarMedico(2L)
        );

        verify(medicoRepository).findById(2L);
    }


    @Test
    public void testarMedicoNaoLocalizadoInativar() {

        when(medicoRepository.findById(2L))
                .thenReturn(Optional.empty());

        assertThrows(
                MedicoNaoEncontradoException.class,
                () -> medicoService.inativarMedico(2L)
        );

        verify(medicoRepository).findById(2L);
    }


    @Test
    public void testarListaPorEspecialidade() {

        Medico medico1 = new Medico(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA,
                null,
                true
        );

        Medico medico2 = new Medico(
                2L,
                "Dra. Maria Silva",
                "654321",
                Especialidade.CARDIOLOGIA,
                null,
                true
        );

        MedicoResponseDTO medicoResponse1 = new MedicoResponseDTO(
                1L,
                "Dr. Pedro Henrique",
                "123456",
                Especialidade.CARDIOLOGIA
        );

        MedicoResponseDTO medicoResponse2 = new MedicoResponseDTO(
                2L,
                "Dra. Maria Silva",
                "654321",
                Especialidade.CARDIOLOGIA
        );

        when(medicoRepository.findByEspecialidade(Especialidade.CARDIOLOGIA))
                .thenReturn(List.of(medico1, medico2));

        when(medicoMapper.toResponseDTO(medico1))
                .thenReturn(medicoResponse1);

        when(medicoMapper.toResponseDTO(medico2))
                .thenReturn(medicoResponse2);

        List<MedicoResponseDTO> resultado =
                medicoService.listarPorEspecialidade(Especialidade.CARDIOLOGIA);

        assertEquals(2, resultado.size());
        assertEquals(medicoResponse1, resultado.get(0));
        assertEquals(medicoResponse2, resultado.get(1));

        verify(medicoRepository)
                .findByEspecialidade(Especialidade.CARDIOLOGIA);

        verify(medicoMapper).toResponseDTO(medico1);
        verify(medicoMapper).toResponseDTO(medico2);
    }


    @Test
    public void testarListaPorEspecialidadeVazia() {

        when(medicoRepository.findByEspecialidade(Especialidade.CARDIOLOGIA))
                .thenReturn(Collections.emptyList());

        List<MedicoResponseDTO> resultado =
                medicoService.listarPorEspecialidade(Especialidade.CARDIOLOGIA);

        assertTrue(resultado.isEmpty());

        verify(medicoRepository)
                .findByEspecialidade(Especialidade.CARDIOLOGIA);
    }
}