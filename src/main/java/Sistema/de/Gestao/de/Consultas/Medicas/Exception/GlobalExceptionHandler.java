package Sistema.de.Gestao.de.Consultas.Medicas.Exception;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.ErroResponse;
import Sistema.de.Gestao.de.Consultas.Medicas.Exception.Exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntimeException(
            RuntimeException ex) {

        ErroResponse error = new ErroResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErroResponse> handleEntityNotFound(
            EntityNotFoundException ex) {

        ErroResponse error = new ErroResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Erro de validação");

        ErroResponse error = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                message,
                LocalDateTime.now()
        );

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(ConsultaException.class)
    public ResponseEntity<ErroResponse> handleConsultaException(
            ConsultaException ex) {

        ErroResponse error = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(PacienteException.class)
    public ResponseEntity<ErroResponse> handlePacienteException(
            PacienteException ex) {
        ErroResponse error = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MedicoException.class)
    public ResponseEntity<ErroResponse> handleMedicoException(
            MedicoException ex){
                ErroResponse error = new ErroResponse(
                        HttpStatus.BAD_REQUEST.value(),
                        ex.getMessage(),
                        LocalDateTime.now()
                );

                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(error);
    }

    @ExceptionHandler(HorarioException.class)
    public ResponseEntity<ErroResponse> handleHorarioException(
            HorarioException ex){
        ErroResponse error = new ErroResponse(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(PacienteNaoLocalizadoException.class)
    public ResponseEntity<ErroResponse> handlePacienteNaoLocalizadoException(
            PacienteNaoLocalizadoException ex){
        ErroResponse error = new ErroResponse(
                (HttpStatus.BAD_REQUEST.value()),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(CpfDuplicadoException.class)
    public ResponseEntity<ErroResponse> handleCpfDuplicadoException(
            CpfDuplicadoException ex){
        ErroResponse error = new ErroResponse(
                (HttpStatus.BAD_REQUEST.value()),
                ex.getMessage(),
                LocalDateTime.now()
        );
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }
}
