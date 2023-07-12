package fr.epita.assistants.myide.domain.entity.features.maven;

import fr.epita.assistants.myide.domain.entity.Feature_model;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class Exec extends Feature_model {
    public Exec() {
        super(Mandatory.Features.Maven.EXEC);
    }

    @Override
    public ExecutionReport execute(Project project, Object... params){
        ProcessBuilder processBuilder = new ProcessBuilder();
        String mvn = "mvn";
        String command = "exec:java";
        ArrayList<String> cmd = new ArrayList<>();
        cmd.add(mvn);
        cmd.add(command);
        Arrays.stream(params).forEach(param ->  {
            cmd.add((String) param);
        });
        if (project.getRootNode().getType() != Node.Types.FOLDER)
        {
            return new ExecutionReport_model(false);
        }
        processBuilder.directory(new File(project.getRootNode().getPath().toUri()));
        processBuilder.command(cmd);
        try {
            Process process = processBuilder.start();
            int exitCode = process.waitFor();
            return new ExecutionReport_model(exitCode == 0);
        } catch (IOException e) {
            return new ExecutionReport_model(false);
        } catch (InterruptedException e) {
            return new ExecutionReport_model(false);
        }
    }
}
