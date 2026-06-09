package at.simcc.simcc_backend.api.sse;

import lombok.Getter;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Sinks;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/9/26
 */
@Component
@Getter
public class BuildSSEComponent {
    private final Sinks.Many<BuildEvent> sseSink = Sinks.many().multicast().onBackpressureBuffer();

    public void publish(BuildEvent buildEvent) {
        sseSink.tryEmitNext(buildEvent);
    }
}
