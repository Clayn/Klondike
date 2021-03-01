package de.clayntech.klondike.impl;

import de.clayntech.klondike.impl.exec.ExecutionContextImpl;
import de.clayntech.klondike.impl.param.ParameterListImpl;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.ExecutionContext;
import de.clayntech.klondike.sdk.exec.ExecutionScript;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;

public class KlondikeRunner {

    private static final String APP_NAME="klondike.app.name";

    public void execute(KlondikeApplication app) throws Exception {
        ParameterListImpl listImpl=new ParameterListImpl();
        listImpl.set("klondike.app.executable",app.getExecutable());
        listImpl.set(APP_NAME,app.getName());
        ExecutionContext context=new ExecutionContextImpl(listImpl);
        prepareScript(app.getScript(),listImpl);
        for(Step step:app.getScript().getSteps()) {
            step.execute(context);
        }
    }

    private void prepareScript(ExecutionScript script,ParameterListImpl parameterList) {
        for(Step step:script.getSteps()) {
            for(StepParameter<?> parameter: step.getParameter()) {
                parameterList.set(parameter.getName(),parameter.getValue());
            }
        }
    }
}
