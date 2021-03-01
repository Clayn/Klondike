package de.clayntech.klondike.impl.exec;

import de.clayntech.klondike.sdk.exec.ExecutionContext;
import de.clayntech.klondike.sdk.param.ParameterList;

public class ExecutionContextImpl implements ExecutionContext {
    private final ParameterList list;

    public ExecutionContextImpl(ParameterList list) {
        this.list = list;
    }

    @Override
    public ParameterList getParameter() {
        return list;
    }
}
