package de.clayntech.klondike.impl.exec;

import de.clayntech.klondike.sdk.exec.ExecutionContext;
import de.clayntech.klondike.sdk.param.ParameterList;
import de.clayntech.klondike.sdk.param.SharedParameterList;

import java.util.function.Consumer;

public class ExecutionContextImpl implements ExecutionContext {
    private final ParameterList list;
    private final SharedParameterList sharedList;
    private final Consumer<String> eventConsumer;

    public ExecutionContextImpl(ParameterList list,SharedParameterList sharedList,Consumer<String> eventConsumer) {
        this.list = list;
        this.sharedList=sharedList;
        this.eventConsumer=eventConsumer;
    }

    @Override
    public ParameterList getParameter() {
        return list;
    }

    @Override
    public SharedParameterList getSharedParameter() {
        return sharedList;
    }

    @Override
    public void trigger(String event) {
        eventConsumer.accept(event);
    }
}
