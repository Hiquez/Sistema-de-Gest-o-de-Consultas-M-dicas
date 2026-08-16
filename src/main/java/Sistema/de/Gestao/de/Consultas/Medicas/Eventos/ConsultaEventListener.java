package Sistema.de.Gestao.de.Consultas.Medicas.Eventos;

import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaAgendadaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaCanceladaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConcluidaEvent;
import Sistema.de.Gestao.de.Consultas.Medicas.Domains.DTO.Consulta.Eventos.ConsultaConfirmadaEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
public class ConsultaEventListener {

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoAgendarConsulta(ConsultaAgendadaEvent event) {
        try {
            // Integração com o sistema de Fluxos: envio de e-mail + geração de PDF
        } catch (Exception e) {
            log.error("Falha ao processar evento de consulta agendada: {}", event.idConsulta(), e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoConfirmarConsulta(ConsultaConfirmadaEvent event) {
        try {
            // Integração com o sistema de Fluxos: envio de e-mail + geração de PDF
        } catch (Exception e) {
            log.error("Falha ao processar evento de consulta confirmada: {}", event.idConsulta(), e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoCancelarConsulta(ConsultaCanceladaEvent event) {
        try {
            // Integração com o sistema de Fluxos: envio de e-mail + geração de PDF
        } catch (Exception e) {
            log.error("Falha ao processar evento de consulta cancelada: {}", event.idConsulta(), e);
        }
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void aoConcluirConsulta(ConsultaConcluidaEvent event) {
        try {
            // Integração com o sistema de Fluxos: envio de e-mail + geração de PDF
        } catch (Exception e) {
            log.error("Falha ao processar evento de consulta concluída: {}", event.idConsulta(), e);
        }
    }
}