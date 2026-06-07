package at.simcc.simcc_backend.trojan_build;

import at.simcc.simcc_backend.other.SimccConstants;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Set;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/6/26
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class TrojanBuildService {
    private Git trojanGitRepo;
    private final SimccConstants simccConstants;
    private final DockerClient docker;

    @PostConstruct
    public void init() throws IOException, GitAPIException {
        setupDirectories();
    }

    private void setupDirectories() throws IOException, GitAPIException {
        if (!Files.exists(SimccConstants.DATA_DIR)) {
            Files.createDirectory(SimccConstants.DATA_DIR);
        }

        if (Files.exists(SimccConstants.TROJAN_DIR)) {
            trojanGitRepo = Git.open(SimccConstants.TROJAN_DIR.toFile());
            ObjectId oldHead = trojanGitRepo.getRepository().resolve("HEAD");
            trojanGitRepo.fetch().call();
            ObjectId fetchHead = trojanGitRepo.getRepository().resolve("FETCH_HEAD");

            if (!oldHead.equals(fetchHead)) {
                log.info("Pulling changes from trojan upstream...");
                trojanGitRepo.pull().call();
                buildDockerImage();
            }
        } else {
            trojanGitRepo = Git.cloneRepository()
                    .setURI("https://github.com/noxie64/simcc_trojan")
                    .setDirectory(SimccConstants.TROJAN_DIR.toFile())
                    .call();
        }

        if (!Files.exists(SimccConstants.BUILD_DIR)) {
            Files.createDirectories(SimccConstants.BUILD_DIR);
        }
    }

    private void buildDockerImage() {
        log.info("Building trojan docker-image...");
        docker.buildImageCmd()
                .withBaseDirectory(SimccConstants.TROJAN_DIR.toFile())
                .withDockerfile(SimccConstants.TROJAN_DIR.resolve("Dockerfile").toFile())
                .withTags(Set.of(simccConstants.getBuilder().getImageTag()));

    }
}
