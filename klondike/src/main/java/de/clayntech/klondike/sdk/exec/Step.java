package de.clayntech.klondike.sdk.exec;

import de.clayntech.klondike.sdk.param.StepParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class Step {

    protected final Logger LOG= LoggerFactory.getLogger(getClass());

    protected final List<StepParameter<?>> parameters=new ArrayList<>();

    public Step() {
    }

    public abstract void execute(ExecutionContext context) throws Exception;


    public List<StepParameter<?>> getParameter() {
        return parameters;
    }
}
