package fr.epita.epita.assistants.project;

import fr.epita.assistants.MyIde;
import fr.epita.assistants.myide.domain.entity.Mandatory;
import fr.epita.assistants.myide.domain.entity.Project;
import fr.epita.assistants.myide.domain.service.ProjectService;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Objects;

public class ProjectTest {
    @Test
    void testProjectAspects() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get(System.getProperty("user.dir")), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (pr.getFeature(Mandatory.Features.Maven.TEST).isPresent());
        assert (pr.getFeature(Mandatory.Features.Maven.COMPILE).isPresent());
        assert (pr.getFeature(Mandatory.Features.Maven.CLEAN).isPresent());
        assert (pr.getFeature(Mandatory.Features.Maven.EXEC).isPresent());
        assert (pr.getFeature(Mandatory.Features.Maven.INSTALL).isPresent());
        assert (pr.getFeature(Mandatory.Features.Maven.PACKAGE).isPresent());
        assert (pr.getFeature(Mandatory.Features.Maven.TREE).isPresent());
        assert (!pr.getFeature(Mandatory.Features.Git.ADD).isPresent());
        assert (!pr.getFeature(Mandatory.Features.Git.COMMIT).isPresent());
        assert (!pr.getFeature(Mandatory.Features.Git.PULL).isPresent());
        assert (!pr.getFeature(Mandatory.Features.Git.PUSH).isPresent());
        assert (pr.getFeature(Mandatory.Features.Any.CLEANUP).isPresent());
        assert (pr.getFeature(Mandatory.Features.Any.DIST).isPresent());
        assert (pr.getFeature(Mandatory.Features.Any.SEARCH).isPresent());
        assert (!Objects.equals(pr.getFeatures(), new ArrayList<>()));
    }
    @Test
    void testProjectEmpty() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get("~/.bashrc"), Paths.get("/tmp"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(configuration.indexFile());
        assert (pr.getRootNode() != null);
    }
    @Test
    void testProjectSearch() {
        MyIde.Configuration configuration = new MyIde.Configuration(Paths.get("toto"), Paths.get("toto"));
        ProjectService p =  MyIde.init(configuration);
        Project pr = p.load(Paths.get(System.getProperty("user.dir")));
        assert (pr.getFeature(Mandatory.Features.Any.SEARCH).get().execute(pr, "walk").isSuccess());
    }
}
