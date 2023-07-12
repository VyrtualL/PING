package fr.epita.epita.assistants.features;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.ProjectService;
import org.apache.lucene.store.Directory;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.junit.jupiter.api.Test;
import org.eclipse.jgit.api.Git;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class GitTest {

    @Test
    void testAddFalse() throws GitAPIException {
        try {
            Files.createDirectory(Paths.get("/tmp/git1/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File("/tmp/git1/");
        Git git = Git.init().setDirectory(file).call();
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(file.toPath());
        assert (!pr.getFeature(Mandatory.Features.Git.ADD).get().execute(pr).isSuccess());
        p.getNodeService().delete(pr.getRootNode());
        assert (!Files.exists(Paths.get("/tmp/git1")));
    }
    @Test
    void testAddTrue() throws GitAPIException {
        try {
            Files.createDirectory(Paths.get("/tmp/git2/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File("/tmp/git2/");
        try {
            Files.createFile(Paths.get("/tmp/git2/src"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Git git = Git.init().setDirectory(file).call();
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(file.toPath());
        assert (pr.getFeature(Mandatory.Features.Git.ADD).get().execute(pr, "src").isSuccess());
        p.getNodeService().delete(pr.getRootNode());
        assert (!Files.exists(Paths.get("/tmp/git2")));
    }

    @Test
    void testAddFalse2() throws GitAPIException {
        try {
            Files.createDirectory(Paths.get("/tmp/git3/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File("/tmp/git3/");
        try {
            Files.createDirectory(Paths.get("/tmp/git3/src/"));
            Files.createFile(Paths.get("/tmp/git3/src/aled"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Git git = Git.init().setDirectory(file).call();
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(file.toPath());
        assert (!pr.getFeature(Mandatory.Features.Git.ADD).get().execute(pr, "aled").isSuccess());
        p.getNodeService().delete(pr.getRootNode());
        assert (!Files.exists(Paths.get("/tmp/git3")));
    }

    @Test
    void testAddGOOD3() throws GitAPIException {
        try {
            Files.createDirectory(Paths.get("/tmp/git6/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File("/tmp/git6/");
        try {
            Files.createDirectory(Paths.get("/tmp/git6/src/"));
            Files.createFile(Paths.get("/tmp/git6/src/aled"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Git git = Git.init().setDirectory(file).call();
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(file.toPath());
        assert (pr.getFeature(Mandatory.Features.Git.ADD).get().execute(pr, ".").isSuccess());
        p.getNodeService().delete(pr.getRootNode());
        assert (!Files.exists(Paths.get("/tmp/git6")));
    }

    @Test
    void testpush() throws GitAPIException {
        try {
            Files.createDirectory(Paths.get("/tmp/git4/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File("/tmp/git4/");
        try {
            Files.createDirectory(Paths.get("/tmp/git4/src/"));
            Files.createFile(Paths.get("/tmp/git4/src/aled"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Git git = Git.init().setDirectory(file).call();
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(file.toPath());
        assert (pr.getFeature(Mandatory.Features.Git.ADD).get().execute(pr, "src").isSuccess());
        assert (pr.getFeature(Mandatory.Features.Git.COMMIT).get().execute(pr, "src").isSuccess());
        assert (!pr.getFeature(Mandatory.Features.Git.PUSH).get().execute(pr).isSuccess());
        p.getNodeService().delete(pr.getRootNode());
        assert (!Files.exists(Paths.get("/tmp/git4")));
    }

    @Test
    void testpushfails() throws GitAPIException {
        try {
            Files.createDirectory(Paths.get("/tmp/git5/"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        File file = new File("/tmp/git5/");
        try {
            Files.createDirectory(Paths.get("/tmp/git5/src/"));
            Files.createFile(Paths.get("/tmp/git5/src/aled"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Git git = Git.init().setDirectory(file).call();
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(file.toPath());
        assert (pr.getFeature(Mandatory.Features.Git.ADD).get().execute(pr, "src").isSuccess());
        assert (!pr.getFeature(Mandatory.Features.Git.PUSH).get().execute(pr).isSuccess());
        p.getNodeService().delete(pr.getRootNode());
        assert (!Files.exists(Paths.get("/tmp/git5s")));
    }
}
