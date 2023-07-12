package fr.epita.assistants.myide.domain.entity.features;

import fr.epita.assistants.myide.domain.entity.Feature;

public class ExecutionReport_model implements Feature.ExecutionReport {
    public boolean res;

    public ExecutionReport_model(boolean res){
        this.res = res;
    }
    @Override
    public boolean isSuccess() {
        return res;
    }
}
