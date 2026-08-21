package Sistema.de.Gestao.de.Consultas.Medicas.Eventos;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaAgendadaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaCanceladaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConcluidaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConfirmadaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Evento.EventoRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.reactive.function.client.WebClient;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class ConsultaEventListener {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoAgendarConsulta(ConsultaAgendadaEvent event) {

        try {

            EventoRequest eventoRequest = new EventoRequest(
                    "SISTEMA_CONSULTAS",
                    "CONSULTA_AGENDADA",
                    objectMapper.writeValueAsString(event),
                    UUID.randomUUID()
            );

            enviarEvento(eventoRequest);

            log.info(
                    "Evento de consulta agendada enviado com sucesso. Consulta: {}",
                    event.idConsulta()
            );

        } catch (Exception e) {

            log.error(
                    "Falha ao processar evento de consulta agendada: {}",
                    event.idConsulta(),
                    e
            );
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoConfirmarConsulta(ConsultaConfirmadaEvent event) {

        try {

            EventoRequest eventoRequest = new EventoRequest(
                    "SISTEMA_CONSULTAS",
                    "CONSULTA_CONFIRMADA",
                    objectMapper.writeValueAsString(event),
                    UUID.randomUUID()
            );

            enviarEvento(eventoRequest);

            log.info(
                    "Evento de consulta confirmada enviado com sucesso. Consulta: {}",
                    event.idConsulta()
            );

        } catch (Exception e) {

            log.error(
                    "Falha ao processar evento de consulta confirmada: {}",
                    event.idConsulta(),
                    e
            );
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoCancelarConsulta(ConsultaCanceladaEvent event) {

        try {

            EventoRequest eventoRequest = new EventoRequest(
                    "SISTEMA_CONSULTAS",
                    "CONSULTA_CANCELADA",
                    objectMapper.writeValueAsString(event),
                    UUID.randomUUID()
            );

            enviarEvento(eventoRequest);

            log.info(
                    "Evento de consulta cancelada enviado com sucesso. Consulta: {}",
                    event.idConsulta()
            );

        } catch (Exception e) {

            log.error(
                    "Falha ao processar evento de consulta cancelada: {}",
                    event.idConsulta(),
                    e
            );
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoConcluirConsulta(ConsultaConcluidaEvent event) {

        try {

            EventoRequest eventoRequest = new EventoRequest(
                    "SISTEMA_CONSULTAS",
                    "CONSULTA_CONCLUIDA",
                    objectMapper.writeValueAsString(event),
                    UUID.randomUUID()
            );

            enviarEvento(eventoRequest);

            log.info(
                    "Evento de consulta concluída enviado com sucesso. Consulta: {}",
                    event.idConsulta()
            );

        } catch (Exception e) {

            log.error(
                    "Falha ao processar evento de consulta concluída: {}",
                    event.idConsulta(),
                    e
            );
        }
    }

    private void enviarEvento(EventoRequest eventoRequest) {

        webClient
                .post()
                .uri("http://localhost:8081/evento/receber")
                .bodyValue(eventoRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}