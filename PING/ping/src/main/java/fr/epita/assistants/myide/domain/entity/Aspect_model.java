package fr.epita.assistants.myide.domain.entity;

import fr.epita.assistants.myide.domain.entity.features.any.Cleanup;
import fr.epita.assistants.myide.domain.entity.features.any.Dist;
import fr.epita.assistants.myide.domain.entity.features.any.Search;
import fr.epita.assistants.myide.domain.entity.features.git.Add;
import fr.epita.assistants.myide.domain.entity.features.git.Commit;
import fr.epita.assistants.myide.domain.entity.features.git.Pull;
import fr.epita.assistants.myide.domain.entity.features.git.Push;
import fr.epita.assistants.myide.domain.entity.features.maven.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Aspect_model implements Aspect {
    private Type type;
    private List<Feature> list;
    public Aspect_model(Type type)
    {
        this.list = new ArrayList<>();
        this.type = type;
        if (type == Mandatory.Aspects.GIT)
        {
            list.add(new Add());
            list.add(new Pull());
            list.add(new Push());
            list.add(new Commit());
        }
        if (type == Mandatory.Aspects.MAVEN)
        {
            list.add(new Clean());
            list.add(new Exec());
            list.add(new Test());
            list.add(new Tree());
            list.add(new Install());
            list.add(new PackageMaven());
            list.add(new Compile());
        }
        if (type == Mandatory.Aspects.ANY)
        {
            list.add(new Dist());
            list.add(new Search());
            list.add(new Cleanup());
        }
    }

    /**
     * @return The type of the Aspect.
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * @return The list of features associated with the Aspect.
     */
    @Override
    @NotNull
    public List<Feature> getFeatureList() {
        return list;
    }
}