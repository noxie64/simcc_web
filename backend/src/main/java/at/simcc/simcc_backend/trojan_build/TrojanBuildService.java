package at.simcc.simcc_backend.trojan_build;

import at.simcc.simcc_backend.api.controller.TrojanController;
import at.simcc.simcc_backend.api.sse.BuildCompleteEvent;
import at.simcc.simcc_backend.api.sse.BuildFailedEvent;
import at.simcc.simcc_backend.api.sse.BuildSSEComponent;
import at.simcc.simcc_backend.entities.Trojan;
import at.simcc.simcc_backend.entities.TrojanBuild;
import at.simcc.simcc_backend.other.SimccSettings;
import at.simcc.simcc_backend.repo.TrojanRepository;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/7/26
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TrojanBuildService {
    private final DockerClient docker;
    private final TrojanRepository trojanRepo;
    private final SimccSettings simccSettings;
    private final BuildSSEComponent buildSSEComponent;

    public void buildTrojan(UUID ccid) {
        Trojan trojan = trojanRepo.findTrojanByCcid(ccid).orElseThrow();
        trojan.setBuilding(true);

        Thread.ofVirtual().start(() -> {
            log.info("Building trojan {}!", ccid);
            List<String> envs = trojan.getTrojanSettings().stream()
                    .map(ts -> "%s=%s".formatted(ts.getKey(), ts.getValue()))
                    .collect(Collectors.toList());
            envs.add("CCID=%s".formatted(ccid));

            TrojanBuild trojanBuild = TrojanBuild.builder()
                    .buildId(UUID.randomUUID())
                    .trojan(trojan)
                    .build();

            String containerId = docker.createContainerCmd(simccSettings.getBuilder().getImageTag())
                    .withEnv(envs)
                    .withCmd("sh", "-c",
                            "cargo make win-build && cp target/x86_64-pc-windows-gnu/release/simcc_trojan.exe /out/%s.exe"
                                    .formatted(trojanBuild.getBuildId())
                    )
                    .withHostConfig(HostConfig.newHostConfig()
                            .withBinds(

                                    new Bind("simcc_simcc-build-data", new Volume("/out"))
                            )
                    ).exec()
                    .getId();

            docker.startContainerCmd(containerId).exec();

            try {
                docker.logContainerCmd(containerId)
                        .withStdOut(true)
                        .withStdErr(true)
                        .withFollowStream(true)
                        .exec(new ResultCallback.Adapter<Frame>() {
                            @Override
                            public void onNext(Frame frame) {
                                String line = new String(frame.getPayload()).trim();
                                if (!line.isEmpty()) {
                                    log.info("[cargo] {}", line);
                                }
                            }
                }).awaitCompletion();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            int exit = docker.waitContainerCmd(containerId)
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode();

            docker.removeContainerCmd(containerId).withForce(true).exec();


            if (exit != 0) {
                buildSSEComponent.publish(
                        new BuildFailedEvent(ccid, "Failed to build trojan: %d".formatted(exit))
                );
                throw new RuntimeException("Build failed for %s".formatted(ccid));
            }

            trojanBuild.setBuildAt(LocalDateTime.now());
            log.info("Successfully build %s!".formatted(trojanBuild.getBuildId()));
            buildSSEComponent.publish(
                    new BuildCompleteEvent(ccid, "Trojan build!")
            );
        });
    }
}
