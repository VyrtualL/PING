package fr.epita.epita.assistants.nodes;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.*;
import fr.epita.assistants.myide.domain.service.NodeService;
import fr.epita.assistants.myide.domain.service.NodeService_model;

import fr.epita.assistants.myide.domain.service.ProjectService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class nodeTest {

    boolean has_type(Node n)
    {
        if (Files.isDirectory(n.getPath()))
        {
            return n.getType() == Node.Types.FOLDER;
        }
        return n.getType() == Node.Types.FILE;
    }

    boolean walk(Node n)
    {
        if (!has_type(n))
        {
            return false;
        }
        else{
            if (n.getType() != Node.Types.FILE)
            {
                for (Node c : n.getChildren())
                {
                    if (!walk(c))
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }
    @Test
    void testNodeType()
    {
        Node n = new Node_model(Paths.get(System.getProperty("user.dir")));
        assert (walk(n));
    }

    @Test
    void longTest()
    {
        Node n = new Node_model(Paths.get("~/"));
        assert (walk(n));
    }

    @Test
    void testUpdate()
    {
        // create a dir named /tmp/test
        try {
            Files.createDirectory(Paths.get("/tmp/test"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // create a file in /tmp and write it Hello World
        Node n = new Node_model(Paths.get("/tmp/test"));
        NodeService_model ns = new NodeService_model();
        Node f = ns.create(n, "test.txt", Node.Types.FILE);
        // checks that the file exists
        assert (Files.exists(f.getPath()));
        f = ns.update(f, 0, 0, "Hello World".getBytes());
        // checks that the file contains Hello World
        File check = new File(f.getPath().toAbsolutePath().toUri());
        assert (check.length() == 11);
        FileReader fr = null;
        try {
            fr = new FileReader(check);
            char[] a = new char[11];
            fr.read(a);
            fr.close();
            assert (new String(a).equals("Hello World"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        try {
            Files.delete(f.getPath());
            Files.delete(Paths.get("/tmp/test"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    void testUpdateUpdate()
    {
        // create a dir named /tmp/test
        try {
            Files.createDirectory(Paths.get("/tmp/test2"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // create a file in /tmp and write it Hello World
        Node n = new Node_model(Paths.get("/tmp/test2"));
        NodeService_model ns = new NodeService_model();
        Node f = ns.create(n, "test.txt", Node.Types.FILE);
        // checks that the file exists
        assert (Files.exists(f.getPath()));
        f = ns.update(f, 0, 0, "Hello World".getBytes());
        // checks that the file contains Hello World
        File check = new File(f.getPath().toAbsolutePath().toUri());
        assert (check.length() == 11);
        FileReader fr = null;
        try {
            fr = new FileReader(check);
            char[] a = new char[11];
            fr.read(a);
            fr.close();
            assert (new String(a).equals("Hello World"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        f = ns.update(f, 0, 5, "inserted".getBytes());
        // checks that the file contains inserted World
        check = new File(f.getPath().toAbsolutePath().toUri());
        assert (check.length() == "inserted World".length());
        try {
            fr = new FileReader(check);
            char[] a = new char[14];
            fr.read(a);
            fr.close();
            assert (new String(a).equals("inserted World"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        //delete the file
        try {
            Files.delete(f.getPath());
            Files.delete(Paths.get("/tmp/test2/"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Test
    void testDelete()
    {
        // create a dir named /tmp/test
        try {
            Files.createDirectory(Paths.get("/tmp/test3"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // create a file in /tmp and write it Hello World
        Node n = new Node_model(Paths.get("/tmp/test3"));
        NodeService_model ns = new NodeService_model();
        Node f = ns.create(n, "test.txt", Node.Types.FILE);
        // checks that the file exists
        assert (Files.exists(f.getPath()));
        f = ns.update(f, 0, 0, "Hello World".getBytes());

        ns.delete(f);
        assert (!Files.exists(Paths.get("/tmp/test3/test.txt")));
        ns.delete(n);
        assert (!Files.exists(Paths.get("/tmp/test3/")));
    }

    @Test
    void testDeleteDir()
    {
        // create a dir named /tmp/test
        try {
            Files.createDirectory(Paths.get("/tmp/test4"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // create a file in /tmp and write it Hello World
        Node n = new Node_model(Paths.get("/tmp/test4"));
        NodeService_model ns = new NodeService_model();
        Node f = ns.create(n, "test.txt", Node.Types.FILE);
        // checks that the file exists
        assert (Files.exists(f.getPath()));
        f = ns.update(f, 0, 0, "Hello World".getBytes());

        ns.delete(n);
        assert (!Files.exists(Paths.get("/tmp/test4/test.txt")));
        assert (!Files.exists(Paths.get("/tmp/test4")));
    }

    @Test
    void testCreateFile()
    {
        // create a dir named /tmp/test
        try {
            Files.createDirectory(Paths.get("/tmp/test5"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // create a file in /tmp and write it Hello World
        Node root = new Node_model(Paths.get("/tmp/test5"));
        NodeService n =  new NodeService_model();

        n.create(root, "tutu", Node.Types.FILE);
        assert (Files.exists(Paths.get("/tmp/test5/tutu")));

        n.delete(root);
        assert (!Files.exists(Paths.get("/tmp/test5/tutu")));
        assert (!Files.exists(Paths.get("/tmp/test5")));
    }
    @Test
    void testCreateDir()
    {
        // create a dir named /tmp/test
        try {
            Files.createDirectory(Paths.get("/tmp/test6"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // create a file in /tmp and write it Hello World
        Node root = new Node_model(Paths.get("/tmp/test6"));
        NodeService n =  new NodeService_model();

        n.create(root, "tutu", Node.Types.FILE);
        assert (Files.exists(Paths.get("/tmp/test6/tutu")));

        Node n2 = n.create(root, "toto", Node.Types.FOLDER);
        assert (Files.exists(Paths.get("/tmp/test6/toto")) && Files.isDirectory(Paths.get("/tmp/test6/toto")));

        n.create(n2, "tata", Node.Types.FILE);
        assert (Files.exists(Paths.get("/tmp/test6/toto/tata")));

        n.delete(root);
        assert (!Files.exists(Paths.get("/tmp/test5/toto/tata")));
        assert (!Files.exists(Paths.get("/tmp/test5/tutu")));
        assert (!Files.exists(Paths.get("/tmp/test5")));
    }


    @Test
    void testMoveFile()
    {
        try {
            Files.createDirectory(Paths.get("/tmp/test7"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node root = new Node_model(Paths.get("/tmp/test7"));
        NodeService n =  new NodeService_model();

        Node n1 = n.create(root, "tutu", Node.Types.FILE);
        assert (Files.exists(Paths.get("/tmp/test7/tutu")));

        Node n2 = n.create(root, "toto", Node.Types.FOLDER);
        assert (Files.exists(Paths.get("/tmp/test7/toto")) && Files.isDirectory(Paths.get("/tmp/test7/toto")));

        n.move(n1, n2);
        assert (Files.exists(Paths.get("/tmp/test7/toto/tutu")));

        n.delete(root);
        assert (!Files.exists(Paths.get("/tmp/test7/toto/tutu")));
        assert (!Files.exists(Paths.get("/tmp/test7/toto")));
        assert (!Files.exists(Paths.get("/tmp/test7")));
    }

    @Test
    void testMoveDirectory()
    {
        try {
            Files.createDirectory(Paths.get("/tmp/test8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node root = new Node_model(Paths.get("/tmp/test8"));
        NodeService n =  new NodeService_model();

        Node n1 = n.create(root, "tutu", Node.Types.FOLDER);
        assert (Files.exists(Paths.get("/tmp/test8/tutu")));

        Node n3 = n.create(n1, "titi", Node.Types.FILE);
        assert (Files.exists(Paths.get("/tmp/test8/tutu/titi")));

        Node n2 = n.create(root, "toto", Node.Types.FOLDER);
        assert (Files.exists(Paths.get("/tmp/test8/toto")) && Files.isDirectory(Paths.get("/tmp/test8/toto")) && Files.isDirectory(Paths.get("/tmp/test8/tutu")));

        n.move(n1, n2);
        assert (Files.exists(Paths.get("/tmp/test8/toto/tutu")));
        assert (Files.exists(Paths.get("/tmp/test8/toto/tutu/titi")));

        n.delete(root);
        assert (!Files.exists(Paths.get("/tmp/test8/toto/tutu/titi")));
        assert (!Files.exists(Paths.get("/tmp/test8/toto/tutu")));
        assert (!Files.exists(Paths.get("/tmp/test8/toto")));
        assert (!Files.exists(Paths.get("/tmp/test8")));
    }
    @Test
    void testZipDirectory()
    {
        try {
            Files.createDirectory(Paths.get("/tmp/test9"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Node root = new Node_model(Paths.get("/tmp/test9"));
        NodeService n =  new NodeService_model();

        Node n1 = n.create(root, "tutu", Node.Types.FOLDER);
        assert (Files.exists(Paths.get("/tmp/test9/tutu")));

        Node n3 = n.create(n1, "titi", Node.Types.FILE);
        assert (Files.exists(Paths.get("/tmp/test9/tutu/titi")));

        Node n2 = n.create(root, "toto", Node.Types.FOLDER);
        assert (Files.exists(Paths.get("/tmp/test9/toto")) && Files.isDirectory(Paths.get("/tmp/test9/toto")) && Files.isDirectory(Paths.get("/tmp/test9/tutu")));

        Node b = n.move(n1, n2);

        assert (Files.exists(Paths.get("/tmp/test9/toto/tutu")));
        assert (Files.exists(Paths.get("/tmp/test9/toto/tutu/titi")));

        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(Paths.get("/tmp/test9"));
        assert (pr.getFeature(Mandatory.Features.Any.DIST).get().execute(pr).isSuccess());
        n.delete(root);
    }

    @Test
    void testUpdate3()
    {
        // create a dir named /tmp/test
        try {
            Files.createDirectory(Paths.get("/tmp/test10"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // create a file in /tmp and write it Hello World
        Node n = new Node_model(Paths.get("/tmp/test10"));
        NodeService_model ns = new NodeService_model();
        Node f = ns.create(n, "test.txt", Node.Types.FILE);
        // checks that the file exists
        assert (Files.exists(f.getPath()));
        f = ns.update(f, 0, 0, "Hello World".getBytes());
        // checks that the file contains Hello World
        File check = new File(f.getPath().toAbsolutePath().toUri());
        assert (check.length() == 11);
        FileReader fr = null;
        try {
            fr = new FileReader(check);
            char[] a = new char[11];
            fr.read(a);
            fr.close();
            assert (new String(a).equals("Hello World"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        f = ns.update(f, 5, 11, " inserted".getBytes());
        // checks that the file contains inserted World
        check = new File(f.getPath().toAbsolutePath().toUri());
        try {
            fr = new FileReader(check);
            char[] a = new char[14];
            fr.read(a);
            fr.close();
            assert (new String(a).equals("Hello inserted"));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
        //delete the file
        try {
            Files.delete(f.getPath());
            Files.delete(Paths.get("/tmp/test10/"));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
