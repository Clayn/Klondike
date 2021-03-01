package de.clayntech.klondike.sdk.exec;

import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.param.StepParameter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class Step {

    protected final Logger LOG= KlondikeLoggerFactory.getLogger();

    protected final List<StepParameter<?>> parameters=new ArrayList<>();

    public Step() {
    }

    public abstract void execute(ExecutionContext context) throws Exception;


    public List<StepParameter<?>> getParameter() {
        return parameters;
    }
}
