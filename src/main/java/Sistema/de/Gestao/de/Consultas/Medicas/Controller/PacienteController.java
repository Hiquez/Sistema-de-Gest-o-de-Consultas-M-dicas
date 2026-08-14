package Sistema.de.Gestao.de.Consultas.Medicas.Controller;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Paciente.PacienteResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.PacienteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/paciente")
public class PacienteController {

    private final PacienteService pacienteService;

    public PacienteController(PacienteService pacienteService) {
        this.pacienteService = pacienteService;
    }

    @PostMapping("/cadastrar")
    public ResponseEntity<PacienteResponseDTO> cadastrar(@RequestBody @Valid PacienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteService.cadastrar(dto));
    }

    @GetMapping("/buscar/{idPaciente}")
    public ResponseEntity<PacienteResponseDTO> buscarPorId(@PathVariable Long idPaciente) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.buscarPorId(idPaciente));
    }

    @GetMapping("/listar/todos")
    public ResponseEntity<List<PacienteResponseDTO>> listarTodos() {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.listarTodos());
    }

    @PutMapping("/atualizar/{idPaciente}")
    public ResponseEntity<PacienteResponseDTO> atualizar(@PathVariable Long idPaciente, @RequestBody @Valid PacienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.atualizar(idPaciente, dto));
    }

    @DeleteMapping("/ativar/{idPaciente}")
    public ResponseEntity<PacienteResponseDTO> ativar(@PathVariable Long idPaciente) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.ativarPaciente(idPaciente));
    }

    @DeleteMapping("/inativar/{idPaciente}")
    public ResponseEntity<PacienteResponseDTO> inativar(@PathVariable Long idPaciente) {
        return ResponseEntity.status(HttpStatus.OK).body(pacienteService.inativarPaciente(idPaciente));
    }
}

