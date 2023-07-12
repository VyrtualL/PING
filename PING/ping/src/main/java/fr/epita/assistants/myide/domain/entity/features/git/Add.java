package fr.epita.assistants.myide.domain.entity.features.git;

import fr.epita.assistants.myide.domain.entity.Feature_model;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;
import org.apache.lucene.store.Directory;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Add extends Feature_model {

    public Add() {
        super(Mandatory.Features.Git.ADD);
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
        if (params.length == 0)
            return new ExecutionReport_model(false);
        AddCommand a = git.add();
        for (Object s : params)
        {
            String path = (String) s;
            if (!Files.exists(Paths.get(project.getRootNode().getPath().toString() , path)))
            {
                return new ExecutionReport_model(false);
            }
            a.addFilepattern(path);
        }
        try {
            a.call();
        }
        catch (Exception e)
        {
            return new ExecutionReport_model(false);
        }
        return new ExecutionReport_model(true);
    }
}
