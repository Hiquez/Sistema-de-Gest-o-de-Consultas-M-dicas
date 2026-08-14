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
@RequestMapping("/consulta")
public class ConsultaController {

    private final ConsultaService consultaService;

    public ConsultaController(ConsultaService consultaService) {
        this.consultaService = consultaService;
    }

    @PostMapping("/agendar")
    public ResponseEntity<ConsultaResponseDTO> agendarConsulta(@RequestBody @Valid ConsultaRequestDTO consultaRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(consultaService.agendarConsulta(consultaRequestDTO));
    }

    @GetMapping("/buscar/consulta/{idConsulta}")
    public ResponseEntity<ConsultaResponseDTO> buscarConsultaId(@PathVariable Long idConsulta){
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.buscarConsultaPorId(idConsulta));
    }

    @GetMapping("/listar/consultas/paciente/{idPaciente}")
    public ResponseEntity<List<ConsultaResponseDTO>> listarConsultaPorPaciente(@PathVariable Long idPaciente){
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.listarConsultaPorPaciente(idPaciente));
    }

    @PatchMapping("/confirmar/{idConsulta}")
    public ResponseEntity<ConsultaResponseDTO> confirmarConsulta(@PathVariable Long idConsulta) {
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.confirmarConsulta(idConsulta));
    }

    @PatchMapping("/cancelar/{idConsulta}")
    public ResponseEntity<ConsultaResponseDTO> cancelarConsulta(@PathVariable Long idConsulta) {
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.cancelarConsulta(idConsulta));
    }

    @PatchMapping("/concluir/{idConsulta}")
    public ResponseEntity<ConsultaResponseDTO> concluirConsulta(@PathVariable Long idConsulta) {
        return ResponseEntity.status(HttpStatus.OK).body(consultaService.concluirConsulta(idConsulta));
    }
}
