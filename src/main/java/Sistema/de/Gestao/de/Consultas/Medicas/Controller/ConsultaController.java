package Sistema.de.Gestao.de.Consultas.Medicas.Controller;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.ConsultaResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.ConsultaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping
    public ResponseEntity<ConsultaResponseDTO> agendar(@RequestBody @Valid ConsultaRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.agendarConsulta(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ConsultaResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.buscarConsultaPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<ConsultaResponseDTO>> listar(
            @RequestParam(required = false) Long pacienteId,
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
    public ResponseEntity<ConsultaResponseDTO> confirmar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.confirmarConsulta(id));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<ConsultaResponseDTO> cancelar(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.cancelarConsulta(id));
    }

    @PatchMapping("/{id}/iniciar")
    public ResponseEntity<ConsultaResponseDTO> iniciarAtendimento(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.iniciarAtendimento(id));
    }

    @PatchMapping("/{id}/concluir")
    public ResponseEntity<ConsultaResponseDTO> concluir(@PathVariable Long id) {
        return ResponseEntity.ok(consultaService.concluirConsulta(id));
    }
}