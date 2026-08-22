package Sistema.de.Gestao.de.Consultas.Medicas.Eventos;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaAgendadaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaCanceladaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConcluidaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConfirmadaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Evento.EventoRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
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

    @Retry(name = "sistemaFluxos", fallbackMethod = "fallbackEnviarEvento")
    @CircuitBreaker(name = "sistemaFluxos", fallbackMethod = "fallbackEnviarEvento")
    public void enviarEvento(EventoRequest eventoRequest) {
        // LIMITAÇÃO CONHECIDA: não há chave de idempotência real aqui (o eventId
        // do sistema de fluxos é gerado aleatório a cada chamada, então retries
        // não são reconhecidos como reenvio do mesmo evento). Em caso de timeout
        // após a chamada já ter tido sucesso do lado do sistema de fluxos, o Retry
        // pode gerar um envio duplicado (ex: e-mail enviado 2x para o paciente).
        // Solução completa exigiria uma chave determinística (idConsulta + tipoEvento)
        // reconhecida pelo sistema de fluxos, ou uma tabela de controle local
        // (chave_idempotencia) — fica como próxima evolução, não implementada agora.
        webClient
                .post()
                .uri("http://host.docker.internal:8081/evento/receber")
                .bodyValue(eventoRequest)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    // Mesma assinatura de enviarEvento + Throwable no final.
    // Só é chamado se as tentativas de Retry se esgotarem OU o circuito estiver aberto.
    public void fallbackEnviarEvento(EventoRequest eventoRequest, Throwable t) {
        log.warn("Sistema de fluxos indisponível após retries. Evento: {}",
                eventoRequest.eventId(), t);
    }
}