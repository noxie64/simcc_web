package at.simcc.simcc_backend.config;

import at.simcc.simcc_backend.other.SimccSettings;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/7/26
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DockerConfig {
    private final SimccSettings simccSettings;

    @Bean(destroyMethod = "close")
    public DockerClient docker() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                .withDockerHost(
                        (System.getProperty("os.name").toLowerCase().contains("win")
                                ? "tcp://host.docker.internal:2375"
                                : "unix:///var/run/docker.sock"
                        )
                )
                .withDockerTlsVerify(simccSettings.getDocker().isTls())
                .build();

        DockerHttpClient httpClient = new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        DockerClient dockerClient = DockerClientImpl.getInstance(config, httpClient);
        log.info("Docker connected! Version {}", dockerClient.versionCmd().exec().getVersion());
        return dockerClient;
    }
}
