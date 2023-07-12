package fr.epita.assistants.myide.domain.service;

import fr.epita.assistants.myide.domain.entity.Node;
import fr.epita.assistants.myide.domain.entity.Node_model;
import fr.epita.assistants.myide.domain.service.NodeService;
import org.apache.lucene.store.Directory;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.*;

public class NodeService_model implements NodeService {

    public NodeService_model()
    {
    }
    /**
     * Update the content in the range [from, to[.
     * The content must be inserted in any case.
     * i.e. : "hello world" -> update(0, 0, "inserted ") -> "inserted hello world"
     * : "hello world" -> update(0, 5, "inserted ") -> "inserted world"
     *
     * @param node            Node to update (must be a file).
     * @param from            Beginning index of the text to update.
     * @param to              Last index of the text to update (Not included).
     * @param insertedContent Content to insert.
     * @return The node that has been updated.
     * @throws Exception upon update failure.
     */
    @Override
    public Node update(Node node, int from, int to, byte[] insertedContent) {
        try {
            if (!node.isFile() || !Files.exists(node.getPath()))
            {
                throw new IOException();
            }
            File f = new File(node.getPath().toAbsolutePath().toUri());

            RandomAccessFile raf = new RandomAccessFile(f, "rw");
            byte[] begin = new byte[from];
            byte[] end = new byte[(int) (raf.length() - to)];
            raf.read(begin);
            raf.seek(to);
            raf.read(end,0, (int) raf.length() - to);
            raf.seek(0);
            Files.delete(node.getPath());
            Files.createFile(node.getPath());
            f = new File(node.getPath().toAbsolutePath().toUri());

            raf = new RandomAccessFile(f, "rw");
            raf.write(begin);
            raf.write(insertedContent);
            raf.write(end);
            raf.close();
            return new Node_model(node.getPath());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Delete the node given as parameter.
     *
     * @param node Node to remove.
     * @return True if the node has been deleted, false otherwise.
     */
    @Override
    public boolean delete(Node node) {
        if (node.isFile())
        {
            try {
                Files.delete(node.getPath());
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        else
        {
            for (Node n : node.getChildren())
            {
                if (!delete(n)) {
                    return false;
                }
            }
            try {
                Files.delete(node.getPath());
            } catch (IOException e) {
                return false;
            }
            return true;
        }
    }

    /**
     * Create a new node and the associated file/directory.
     *
     * @param folder Parent node of the new node.
     * @param name   Name of the new node.
     * @param type   Type of the new node.
     * @return Node that has been created.
     * @throws Exception upon creation failure.
     */
    @Override
    public Node create(Node folder, String name, Node.Type type) {
        try{
            if (!folder.isFolder() || Files.exists(Paths.get(folder.getPath().toAbsolutePath().toString(), name))){
                throw new IOException();
            }
            Path p = Paths.get(folder.getPath().toAbsolutePath().toString(), name);
            if (type == Node.Types.FILE){
                File nfile = new File(p.toString());
                try {
                    if (!nfile.createNewFile())
                    {
                        throw new IOException();
                    }
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
            else
            {
                try {
                    Files.createDirectories(p);
                }
                catch (Exception e)
                {
                    throw new RuntimeException(e);
                }
            }
            Node n = new Node_model(p);
            return n;
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    /**
     * Move node from source to destination.
     *
     * @param nodeToMove        Node to move.
     * @param destinationFolder Destination of the node.
     * @return The node that has been moved.
     * @throws Exception upon move failure.
     */
    @Override
    public Node move(Node nodeToMove, Node destinationFolder) {
        if (destinationFolder.getType() != Node.Types.FOLDER)
        {
            throw new RuntimeException("Destination is not a folder");
        }

        Path dest = destinationFolder.getPath().resolve(nodeToMove.getPath().getFileName());
        Path p = null;
        try {
            p = Files.move(nodeToMove.getPath(), dest, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        return new Node_model(p);
    }
}
