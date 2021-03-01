package de.clayntech.klondike.sdk.exec;

import de.clayntech.klondike.sdk.param.ParameterList;
import de.clayntech.klondike.sdk.param.SharedParameterList;

import java.io.File;

public interface ExecutionContext {

    ParameterList getParameter();

    SharedParameterList getSharedParameter();

    void trigger(String event);

    default File getExecutable() {
        return getParameter().get("klondike.app.executable",File.class);
    }
}
