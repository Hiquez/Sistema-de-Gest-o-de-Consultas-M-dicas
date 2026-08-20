package Sistema.de.Gestao.de.Consultas.Medicas.Controller;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.ConsultaService;
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
import java.util.UUID;

@RestController
@RequestMapping("/consultas")
@Tag(name = "Consultas", description = "Operações de agendamento, consulta e atualização de status das consultas médicas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    @Operation(
            summary = "Agendar consulta",
            description = "Cria uma nova consulta médica com os dados do paciente, médico e agenda."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Consulta agendada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou campos obrigatórios ausentes"),
            @ApiResponse(responseCode = "409", description = "Conflito de agenda ou consulta duplicada")
    })
    public ResponseEntity<ConsultaResponseDTO> agendar(@RequestBody @Valid ConsultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.agendarConsulta(dto));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar consulta por ID",
            description = "Retorna os detalhes de uma consulta específica pelo seu identificador."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaResponseDTO> buscarPorId(
            @Parameter(description = "Identificador da consulta", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(consultaService.buscarConsultaPorId(id));
    }

    @GetMapping
    @Operation(
            summary = "Listar consultas",
            description = "Lista as consultas por paciente ou por médico. Informe ao menos um dos parâmetros."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de consultas retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Nenhum filtro informado")
    })
    public ResponseEntity<List<ConsultaResponseDTO>> listar(
            @Parameter(description = "Identificador do paciente para filtrar as consultas")
            @RequestParam(required = false) Long pacienteId,
            @Parameter(description = "Identificador do médico para filtrar as consultas")
            @RequestParam(required = false) Long medicoId) {

        if (pacienteId != null) {
            return ResponseEntity.ok(consultaService.listarConsultaPorPaciente(pacienteId));
        }
        if (medicoId != null) {
            return ResponseEntity.ok(consultaService.listarConsultaPorMedico(medicoId));
        }
        throw new IllegalArgumentException("Informe pacienteId ou medicoId para listar as consultas");
    }

    @PatchMapping("/{id}/confirmar")
    @Operation(
            summary = "Confirmar consulta",
            description = "Atualiza o status da consulta para confirmada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta confirmada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaResponseDTO> confirmar(
            @Parameter(description = "Identificador da consulta", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(consultaService.confirmarConsulta(id));
    }

    @PatchMapping("/{id}/cancelar")
    @Operation(
            summary = "Cancelar consulta",
            description = "Cancela uma consulta existente e registra a alteração de status."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta cancelada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaResponseDTO> cancelar(
            @Parameter(description = "Identificador da consulta", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(consultaService.cancelarConsulta(id));
    }

    @PatchMapping("/{id}/iniciar")
    @Operation(
            summary = "Iniciar atendimento",
            description = "Inicia o atendimento da consulta, alterando o status para em andamento."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Atendimento iniciado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaResponseDTO> iniciarAtendimento(
            @Parameter(description = "Identificador da consulta", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(consultaService.iniciarAtendimento(id));
    }

    @PatchMapping("/{id}/concluir")
    @Operation(
            summary = "Concluir consulta",
            description = "Finaliza a consulta e atualiza o status para concluída."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Consulta concluída com sucesso"),
            @ApiResponse(responseCode = "404", description = "Consulta não encontrada")
    })
    public ResponseEntity<ConsultaResponseDTO> concluir(
            @Parameter(description = "Identificador da consulta", required = true)
            @PathVariable UUID id) {
        return ResponseEntity.ok(consultaService.concluirConsulta(id));
    }
}