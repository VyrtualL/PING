package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature_model;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Pull extends Feature_model {
    public Pull() {
        super(Mandatory.Features.Git.PULL);
    }

    @Override
    public ExecutionReport execute(Project project, Object... params) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository;
        try {
            repository = builder.setGitDir(new File(project.getRootNode().getPath().toString(), Paths.get("/.git/").toString())).build();
        } catch (IOException e) {
            return new ExecutionReport_model(false);
        }
        Git git = new Git(repository);
        try {
            git.pull().setFastForward(MergeCommand.FastForwardMode.FF).call();
        } catch (GitAPIException e) {
            return new ExecutionReport_model(false);
        }
        return new ExecutionReport_model(true);
    }
}