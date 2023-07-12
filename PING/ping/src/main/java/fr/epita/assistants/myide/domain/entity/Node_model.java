package fr.epita.assistants.myide.domain.entity;



import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Node_model implements Node {

    private Path node_path;
    private Type node_type;
    private List<Node> children;

    /**
     * This function will create a node base on his
     * type (folder or file) if it is a folder, will
     * append all the child recursively.
     * @param node_path
     */
    public Node_model(Path node_path){
        this.node_path = node_path;
        this.children = new ArrayList<>();
        // simlinks
        File f = new File(node_path.toUri());
        if (f.isDirectory())
        {
            this.node_type = Types.FOLDER;
            // recusivly create nodes
            try {
                Stream<Path> to_visit = Files.list(node_path);
                to_visit.forEach(nodes -> {
                    children.add(new Node_model(nodes));
                });
                to_visit.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
            this.node_type = Types.FILE;
        }
    }

    /**
     * @return The Node path.
     */
    @Override
    public Path getPath() {
        return node_path;
    }

    /**
     * @return The Node type.
     */
    @Override
    public Type getType() {
        return node_type;
    }

    /**
     * If the Node is a Folder, returns a list of its children,
     * else returns an empty list.
     *
     * @return List of node
     */
    @Override
    public List<@NotNull Node> getChildren() {
        List<Node> nlist = new ArrayList<>();
        if (this.node_type == Types.FOLDER)
        {
            Stream<Path> to_visit = null;
            try {
                to_visit = Files.list(node_path);
                to_visit.forEach(nodes -> {
                    nlist.add(new Node_model(nodes));
                });
                to_visit.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        this.children = List.copyOf(nlist);
        return children;
    }
}
