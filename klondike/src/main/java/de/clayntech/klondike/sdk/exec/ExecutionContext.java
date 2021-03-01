package de.clayntech.klondike.sdk.exec;

import de.clayntech.klondike.sdk.param.ParameterList;

import java.io.File;

public interface ExecutionContext {

    ParameterList getParameter();

    default File getExecutable() {
        return getParameter().get("klondike.app.executable",File.class);
    }
}
