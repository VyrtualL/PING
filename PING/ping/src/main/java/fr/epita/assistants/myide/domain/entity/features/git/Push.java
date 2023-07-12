package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature_model;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.MergeCommand;
import org.eclipse.jgit.api.Status;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.RemoteRefUpdate;
import org.eclipse.jgit.treewalk.FileTreeIterator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

public class Push extends Feature_model {
    public Push() {
        super(Mandatory.Features.Git.PUSH);
    }

    @Override
    public ExecutionReport execute(Project project, Object... params) {
        FileRepositoryBuilder builder = new FileRepositoryBuilder();
        Repository repository;
        FileTreeIterator workingTreeIterator;
        try {
            repository = builder.setGitDir(new File(project.getRootNode().getPath().toString(), Paths.get("/.git/").toString())).build();
            workingTreeIterator = new FileTreeIterator(repository);
        } catch (IOException e) {
            return new ExecutionReport_model(false);
        }
        Git git = new Git(repository);
        if (git.push().isForce())
        {
            return new ExecutionReport_model(false);
        }
        AtomicBoolean atomicBoolean = new AtomicBoolean(true);
        try {
            Iterable<PushResult> pushResultIterable = git.push().setForce(false).call();
            // check the doc push result methode magique pour renvoyer les ref update puis reitterer et chopper get result
            pushResultIterable.forEach(pushResult -> {
                pushResult.getRemoteUpdates().forEach(remoteRefUpdate -> {
                    atomicBoolean.set(remoteRefUpdate.getStatus() != RemoteRefUpdate.Status.UP_TO_DATE);
                });
            });
        }
        catch (Exception e)
        {
            return new ExecutionReport_model(false);
        }
        return new ExecutionReport_model(atomicBoolean.get());
    }
}