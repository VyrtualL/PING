package fr.epita.epita.assistants.features;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.ProjectService;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

public class MavenTest {
    /*
    @Test
    void testCompile() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (pr.getFeature(Mandatory.Features.Maven.COMPILE).get().execute(pr).isSuccess());
    }
    //@Test
    //void testClean() {
    //    MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
    //    ProjectService p =  MyIde.init(configuration);
    //    Project pr = p.load(configuration.indexFile());
    //    assert (pr.getFeature(Mandatory.Features.Maven.CLEAN).get().execute(pr).isSuccess());
    //}
    @Test
    void testExec() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (!pr.getFeature(Mandatory.Features.Maven.EXEC).get().execute(pr).isSuccess());
    }
   /* @Test
    void testInstall() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (pr.getFeature(Mandatory.Features.Maven.INSTALL).get().execute(pr).isSuccess());
    }
    @Test
    void testPackage() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (pr.getFeature(Mandatory.Features.Maven.PACKAGE).get().execute(pr).isSuccess());
    }
    @Test
    void testTest() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (pr.getFeature(Mandatory.Features.Maven.TEST).get().execute(pr).isSuccess());
    }
    @Test
    void testTree() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (pr.getFeature(Mandatory.Features.Maven.TREE).get().execute(pr).isSuccess());
    }
    */
}
