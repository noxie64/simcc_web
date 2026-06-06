package at.simcc.simcc_backend.trojan_build;

import at.simcc.simcc_backend.other.SimccConstants;
import jakarta.annotation.PostConstruct;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;

/**
 * Project: backend
 * Created by: Georg Kollegger
 * Date: 6/6/26
 */
@Component
public class TrojanBuildManager {
    private Git trojanGitRepo;

    @PostConstruct
    public void init() throws IOException, GitAPIException {
        if (!Files.exists(SimccConstants.DATA_DIR)) {
            Files.createDirectory(SimccConstants.DATA_DIR);
        }

        if (Files.exists(SimccConstants.TROJAN_DIR)) {
            trojanGitRepo = Git.open(SimccConstants.TROJAN_DIR.toFile());
            trojanGitRepo.pull().call();
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
}
