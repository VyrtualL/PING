package fr.epita.assistants.myide.domain.entity;

import java.nio.file.Path;
import java.util.*;

public class Project_model implements Project {
    private Node root_node;
    private Set<Aspect> aspect_set;
    private List<Feature> feature_list;
    public Path tempDir;
    public Path indexPath;

    public Project_model(Node root, Set<Aspect> aspect_set, Path tempDir, Path indexPath)
    {
        this.root_node = root;
        this.aspect_set = aspect_set;
        this.feature_list = getAspects().stream().map(Aspect::getFeatureList).flatMap(Collection::stream).toList();
        this.tempDir = tempDir;
        this.indexPath = indexPath;
    }


    /**
     * @return The root node of the project.
     */
    @Override
    public Node getRootNode() {
        return root_node;
    }

    /**
     * @return The aspects of the project.
     */
    @Override
    public Set<Aspect> getAspects() {
        return aspect_set;
    }

    /**
     * Get an optional feature of the project depending
     * of its type. Returns an empty optional if the
     * project does not have the features queried.
     *
     * @param featureType Type of the feature to retrieve.
     * @return An optional feature of the project.
     */
    @Override
    public Optional<Feature> getFeature(Feature.Type featureType)
    {
         List<Aspect> list = aspect_set.stream().toList();
         for (Aspect a : list)
         {
             for (Feature f : a.getFeatureList()){
                 if (f.type() == featureType)
                 {
                     return Optional.of(f);
                 }
             }
         }
         return Optional.empty();
    }
}
