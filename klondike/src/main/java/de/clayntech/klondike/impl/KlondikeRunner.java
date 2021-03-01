package de.clayntech.klondike.impl;

import de.clayntech.klondike.impl.exec.ExecutionContextImpl;
import de.clayntech.klondike.impl.param.ParameterListImpl;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.ExecutionContext;
import de.clayntech.klondike.sdk.exec.ExecutionScript;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;
import de.clayntech.klondike.sdk.util.Pair;

import java.util.*;
import java.util.function.Consumer;

public class KlondikeRunner {

    public static final String APP_NAME="klondike.app.name";

    private final Map<Pair<String,String>, List<Step>> eventSteps=new HashMap<>();
    private final Map<String,ExecutionContext> currentExecutions=new HashMap<>();

    public void execute(KlondikeApplication app) throws Exception {
        ParameterListImpl listImpl=new ParameterListImpl();
        ParameterListImpl sharedList=new ParameterListImpl();
        listImpl.set("klondike.app.executable",app.getExecutable());
        listImpl.set(APP_NAME,app.getName());
        String id= UUID.randomUUID().toString();
        ExecutionContext context=new ExecutionContextImpl(listImpl,sharedList,createConsumer(id));
        currentExecutions.put(id,context);
        List<Step> executionSteps=prepareScript(app.getScript(),listImpl,id);
        try{
            for(Step step:executionSteps) {
                step.execute(context);
            }
        }finally {
            currentExecutions.remove(id);
        }
    }

    private Consumer<String> createConsumer(String uuid) {
        return s -> {
            Pair<String,String> key=new Pair<>(s,uuid);
            if(eventSteps.containsKey(key)) {
                ExecutionContext context=currentExecutions.get(uuid);
                if(context==null) {
                    return;
                }
                for(Step step:eventSteps.get(key)) {
                    try {
                        step.execute(context);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }

    private List<Step> prepareScript(ExecutionScript script,ParameterListImpl parameterList,String id) {
        List<Step> steps=new ArrayList<>();
        for(Step step:script.getSteps()) {
            if(step.getTrigger().isEmpty()) {
                steps.add(step);
            }else {
                for(String trigger:step.getTrigger()) {
                    Pair<String,String> key=new Pair<>(trigger,id);
                    if(!eventSteps.containsKey(key)) {
                        eventSteps.put(key,new ArrayList<>());
                    }
                    eventSteps.get(key).add(step);
                }
            }
            for(StepParameter<?> parameter: step.getParameter()) {
                parameterList.set(parameter.getName(),parameter.getValue());
            }
        }
        return steps;
    }
}
