package Sistema.de.Gestao.de.Consultas.Medicas.Controller;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoRequestDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Medico.MedicoResponseDTO;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.Enum.Especialidade;
import Sistema.de.Gestao.de.Consultas.Medicas.Service.MedicoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    private final MedicoService medicoService;

    public MedicoController(MedicoService medicoService) {
        this.medicoService = medicoService;
    }

    @PostMapping
    public ResponseEntity<MedicoResponseDTO> cadastrar(@RequestBody @Valid MedicoRequestDTO dto) {
        MedicoResponseDTO response = medicoService.cadastrar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<MedicoResponseDTO>> listar(
            @RequestParam(required = false) Especialidade especialidade) {

        List<MedicoResponseDTO> medicos = especialidade != null
                ? medicoService.listarPorEspecialidade(especialidade)
                : medicoService.listarTodos();

        return ResponseEntity.ok(medicos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicoResponseDTO> atualizar(@PathVariable Long id,
                                                       @RequestBody @Valid MedicoRequestDTO dto) {
        return ResponseEntity.ok(medicoService.atualizar(id, dto));
    }

    @PatchMapping("/ativar/{id}")
    public ResponseEntity<MedicoResponseDTO> ativar(@PathVariable Long id) {
        return ResponseEntity.ok(medicoService.ativarMedico(id));
    }

    @PatchMapping("/desativar/{id}")
    public ResponseEntity<MedicoResponseDTO> desativar(@PathVariable Long id){
        return ResponseEntity.ok(medicoService.inativarMedico(id));
    }
}

