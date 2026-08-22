package Sistema.de.Gestao.de.Consultas.Medicas.Config.HealthIndicator;

import org.springframework.boot.health.contributor.Health;
import org.springframework.boot.health.contributor.HealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;

@Component
public class SistemaFluxosHealthIndicator implements HealthIndicator {

    private final WebClient webClient;

    public SistemaFluxosHealthIndicator(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Health health() {
        try {
            webClient.get()
                    .uri("http://host.docker.internal:8081/")
                    .retrieve()
                    .toBodilessEntity()
                    .block(Duration.ofSeconds(2));

            return Health.up().build();

        } catch (WebClientResponseException e) {
            return Health.up().withDetail("info", "Servidor respondeu, sem endpoint de health dedicado").build();

        } catch (Exception e) {
            return Health.down().withDetail("erro", e.getMessage()).build();

        }
    }
}