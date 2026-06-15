package at.simcc.simcc_backend.api.sse;

import lombok.Getter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/15/26
 */
@Component
@Getter
public class InfectedSSEComponent {
    private final Sinks.Many<InfectedStatusChangeEvent> sseSink = Sinks.many().multicast().onBackpressureBuffer();

    public void publish(InfectedStatusChangeEvent event) {
        sseSink.tryEmitNext(event);
    }
}
