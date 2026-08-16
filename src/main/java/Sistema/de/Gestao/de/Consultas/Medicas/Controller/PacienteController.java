package Sistema.de.Gestao.de.Consultas.Medicas.Controller;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.PacienteService;
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
@RequestMapping("/paciente")
@Tag(name = "Pacientes", description = "Operações de cadastro, busca, atualização e ativação de pacientes")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/cadastrar")
    @Operation(
            summary = "Cadastrar paciente",
            description = "Cria um novo paciente no sistema com os dados informados."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Paciente cadastrado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "409", description = "Paciente já cadastrado")
    })
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid PacienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastrar(dto));
    }

    @GetMapping("/buscar/{idPaciente}")
    @Operation(
            summary = "Buscar paciente por ID",
            description = "Retorna os dados de um paciente específico pelo identificador informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> buscarPorId(
            @Parameter(description = "Identificador do paciente", required = true)
            @PathVariable Long idPaciente) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.buscarPorId(idPaciente));
    }

    @GetMapping("/listar/todos")
    @Operation(
            summary = "Listar todos os pacientes",
            description = "Retorna a lista completa de pacientes cadastrados no sistema."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso")
    })
    public ResponseEntity<List<PacienteResponseDTO>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.listarTodos());
    }

    @PutMapping("/atualizar/{idPaciente}")
    @Operation(
            summary = "Atualizar paciente",
            description = "Atualiza os dados de um paciente existente pelo identificador informado."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<PacienteResponseDTO> atualizar(
            @Parameter(description = "Identificador do paciente", required = true)
            @PathVariable Long idPaciente,
            @RequestBody @Valid PacienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.atualizar(idPaciente, dto));
    }

    @DeleteMapping("/ativar/{idPaciente}")
    @Operation(
            summary = "Ativar paciente",
            description = "Ativa o cadastro do paciente para permitir novas operações e atendimentos."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> ativar(
            @Parameter(description = "Identificador do paciente", required = true)
            @PathVariable Long idPaciente) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.ativarPaciente(idPaciente));
    }

    @DeleteMapping("/inativar/{idPaciente}")
    @Operation(
            summary = "Inativar paciente",
            description = "Inativa o cadastro do paciente, bloqueando operações futuras relacionadas ao mesmo."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Paciente inativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })
    public ResponseEntity<PacienteResponseDTO> inativar(
            @Parameter(description = "Identificador do paciente", required = true)
            @PathVariable Long idPaciente) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.inativarPaciente(idPaciente));
    }
}

