package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.entity.features.ExecutionReport_model;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProjectService_model implements ProjectService {
    List<Project> project_list;
    NodeService_model nodeService_model;
    Path tempDir;
    Path indexDir;

    public ProjectService_model(Path indexDir, Path tempDir)
    {
        this.project_list = new ArrayList<>();
        this.nodeService_model = new NodeService_model();
        this.tempDir = tempDir;
        this.indexDir = indexDir;
    }

    /**
     * Load a {@link Project} from a path.
     *
     * @param root Path of the root of the project to load.
     * @return New project.
     */
    @Override
    public Project load(Path root) {
        Node root_node = new Node_model(root);
        Set<Aspect> aspect_set = new HashSet<>();
        for (Node n_ : root_node.getChildren()){
            if (n_ != null && n_.getPath().toString().contains(".git"))
            {
                aspect_set.add(new Aspect_model(Mandatory.Aspects.GIT));
            }
            if (n_ != null && n_.getPath().toString().contains("pom.xml"))
            {
                aspect_set.add(new Aspect_model(Mandatory.Aspects.MAVEN));
            }
        }
        aspect_set.add(new Aspect_model(Mandatory.Aspects.ANY));
        Project p = new Project_model(root_node, aspect_set, tempDir, indexDir);
        project_list.add(p);
        return p;
    }

    /**
     * Execute the given feature on the given project.
     *
     * @param project     Project for which the features is executed.
     * @param featureType Type of the feature to execute.
     * @param params      Parameters given to the features.
     * @return Execution report of the feature.
     */
    @Override
    public Feature.ExecutionReport execute(Project project, Feature.Type featureType, Object... params) {
        if (project.getFeature(featureType).isPresent())
        {
            project.getFeature(featureType).get().execute(project, params);
            return new ExecutionReport_model(true);
        }
        return new ExecutionReport_model(false);
    }

    /**
     * @return The {@link NodeService} associated with your {@link ProjectService}
     */
    @Override
    public NodeService getNodeService() {
        return nodeService_model;
    }
}
