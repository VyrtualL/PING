package fr.epita.assistants.myide.domain.entity.features.any;

import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;
import fr.epita.assistants.myide.domain.service.NodeService_model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cleanup extends Feature_model {

    public Cleanup() {super (Mandatory.Features.Any.CLEANUP);}

    @Override
    public ExecutionReport execute(Project project, Object... params) {
        BufferedReader reader;
        NodeService_model nodeserv = new NodeService_model();
        try {
            reader = new BufferedReader(new FileReader(Paths.get(project.getRootNode().getPath().toString(), ".myideignore").toString()));
            String line = reader.readLine();

            while (line != null) {
                // read next line
                Node n = project.getRootNode();
                Path p = Paths.get(n.getPath().toString(), line);
                if (Files.exists(p))
                {
                    Node p_root = new Node_model(p);
                    if (!nodeserv.delete(p_root))
                    {
                        return new ExecutionReport_model(false);
                    }
                }
                line = reader.readLine();
            }
            reader.close();
            return new ExecutionReport_model(true);
        } catch (Exception e) {
            return new ExecutionReport_model(false);
        }
    }
}
