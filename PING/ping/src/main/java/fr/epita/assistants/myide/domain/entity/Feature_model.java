package fr.epita.assistants.myide.domain.entity;

import java.io.IOException;

public class Feature_model implements Feature {

    private Type type;

    public Feature_model(Type type)
    {
        this.type = type;
    }

    /**
     * @param project {@link Project} on which the feature is executed.
     * @param params  Parameters given to the features.
     * @return {@link ExecutionReport}
     */
    @Override
    public ExecutionReport execute(Project project, Object... params) {
        return null;
    }

    /**
     * @return The type of the Feature.
     */
    @Override
    public Type type() {
        return type;
    }
}
