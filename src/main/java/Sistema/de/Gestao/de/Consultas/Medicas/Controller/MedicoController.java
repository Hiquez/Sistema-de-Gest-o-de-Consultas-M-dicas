package Sistema.de.Gestao.de.Consultas.Medicas.Controller;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.MedicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
@Tag(name = "Médicos", description = "Operações de cadastro, listagem, atualização e ativação de médicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    @Operation(
            summary = "Cadastrar médico",
            description = "Cria um novo cadastro de médico no sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Médico cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "409", description = "Médico já cadastrado no sistema")
    })
    public ResponseEntity<MedicoResponseDTO> cadastrar(@RequestBody @Valid MedicoRequestDTO dto) {
        MedicoResponseDTO response = medicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar médico por ID",
            description = "Retorna os dados de um médico específico com base no identificador informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<MedicoResponseDTO> buscarPorId(
            @Parameter(description = "Identificador do médico", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping
    @Operation(
            summary = "Listar médicos",
            description = "Lista todos os médicos ou filtra por especialidade quando informada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de médicos retornada com sucesso")
    })
    public ResponseEntity<List<MedicoResponseDTO>> listar(
            @Parameter(description = "Especialidade para filtrar médicos")
            @RequestParam(required = false) Especialidade especialidade) {

        List<MedicoResponseDTO> medicos = especialidade != null
                ? medicoService.listarPorEspecialidade(especialidade)
                : medicoService.listarTodos();

        return ResponseEntity.ok(medicos);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Atualizar médico",
            description = "Atualiza os dados de um médico existente pelo identificador informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<MedicoResponseDTO> atualizar(
            @Parameter(description = "Identificador do médico", required = true)
            @PathVariable Long id,
            @RequestBody @Valid MedicoRequestDTO dto) {
        return ResponseEntity.ok(medicoService.atualizar(id, dto));
    }

    @PatchMapping("/ativar/{id}")
    @Operation(
            summary = "Ativar médico",
            description = "Ativa o cadastro do médico para permitir novas consultas e atendimentos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<MedicoResponseDTO> ativar(
            @Parameter(description = "Identificador do médico", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(medicoService.ativarMedico(id));
    }

    @PatchMapping("/desativar/{id}")
    @Operation(
            summary = "Desativar médico",
            description = "Desativa o cadastro do médico para impedir novas consultas enquanto estiver indisponível."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Médico desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Médico não encontrado")
    })
    public ResponseEntity<MedicoResponseDTO> desativar(
            @Parameter(description = "Identificador do médico", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(medicoService.inativarMedico(id));
    }
}

