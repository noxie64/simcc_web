package at.simcc.simcc_backend.trojan_build;

import at.simcc.simcc_backend.other.SimccSettings;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.BuildImageResultCallback;
import com.github.dockerjava.api.exception.NotFoundException;
import com.github.dockerjava.api.model.BuildResponseItem;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Set;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/6/26
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class TrojanBuildManager {
    private Git trojanGitRepo;
    private final SimccSettings simccSettings;
    private final DockerClient docker;

    @PostConstruct
    public void init() throws IOException, GitAPIException, InterruptedException {
        setupDirectories();
    }

    private void setupDirectories() throws IOException, GitAPIException, InterruptedException {
        if (!Files.exists(SimccSettings.DATA_DIR)) {
            Files.createDirectory(SimccSettings.DATA_DIR);
        }

        if (Files.exists(SimccSettings.TROJAN_DIR)) {
            trojanGitRepo = Git.open(SimccSettings.TROJAN_DIR.toFile());
            ObjectId oldHead = trojanGitRepo.getRepository().resolve("HEAD");
            try {
                trojanGitRepo.fetch().call();
                ObjectId fetchHead = trojanGitRepo.getRepository().resolve("FETCH_HEAD");

                if (!oldHead.equals(fetchHead)) {
                    log.info("Pulling changes from trojan upstream...");
                    trojanGitRepo.pull().call();
                    buildDockerImage();
                }
            } catch (Exception e) {
                log.error("Failed to look in trojan-repo for changes, using current state, not good!");
            }
        } else {
            trojanGitRepo = Git.cloneRepository()
                    .setURI("https://github.com/noxie64/simcc_trojan")
                    .setDirectory(SimccSettings.TROJAN_DIR.toFile())
                    .call();
            buildDockerImage();
        }

        if (!Files.exists(SimccSettings.BUILD_DIR)) {
            Files.createDirectories(SimccSettings.BUILD_DIR);
        }

        if (!doesImageExist()) {
            buildDockerImage();
        }
    }

    private boolean doesImageExist() {
        try {
            docker.inspectImageCmd(simccSettings.getBuilder().getImageTag()).exec();
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    private void buildDockerImage() throws InterruptedException {
        log.info("Building trojan docker-image...");
        docker.buildImageCmd()
                .withBaseDirectory(SimccSettings.TROJAN_DIR.toFile())
                .withDockerfile(SimccSettings.TROJAN_DIR.resolve("Dockerfile").toFile())
                .withTags(Set.of(simccSettings.getBuilder().getImageTag()))
                .exec(new BuildImageResultCallback() {
                    @Override
                    public void onNext(BuildResponseItem item) {
                        if (item.getStream() != null) {
                            log.info("[docker build] {}", item.getStream().trim());
                        }
                    }

                    @Override
                    public void onComplete() {
                        log.info("[docker build] {} build!", simccSettings.getBuilder().getImageTag());
                        super.onComplete();
                    }
                }).awaitCompletion();
    }
}
