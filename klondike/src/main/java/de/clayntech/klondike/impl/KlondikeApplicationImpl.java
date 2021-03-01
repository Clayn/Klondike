package de.clayntech.klondike.impl;

import de.clayntech.klondike.impl.exec.ExecutionScriptImpl;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.ExecutionScript;

import java.io.File;

public class KlondikeApplicationImpl implements KlondikeApplication {
    private String name;
    private File executable;
    private ExecutionScriptImpl script=new ExecutionScriptImpl();

    public KlondikeApplicationImpl() {
    }

    public KlondikeApplicationImpl(String name, File executable) {
        this.name = name;
        this.executable = executable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExecutable(File executable) {
        this.executable = executable;
    }

    public void setScript(ExecutionScriptImpl script) {
        this.script = script;
    }

    @Override
    public ExecutionScript getScript() {
        return script;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public File getExecutable() {
        return executable;
    }

    @Override
    public String toString() {
        return "KlondikeApplicationImpl{" +
                "name='" + name + '\'' +
                ", executable=" + executable +
                ", script=" + script +
                '}';
    }
}
