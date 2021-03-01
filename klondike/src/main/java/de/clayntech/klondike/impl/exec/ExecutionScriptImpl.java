package de.clayntech.klondike.impl.exec;

import de.clayntech.klondike.sdk.exec.ExecutionScript;
import de.clayntech.klondike.sdk.exec.Step;

import java.util.ArrayList;
import java.util.List;

public class ExecutionScriptImpl implements ExecutionScript {
    private final List<Step> steps=new ArrayList<>();

    @Override
    public List<Step> getSteps() {
        return steps;
    }
}
