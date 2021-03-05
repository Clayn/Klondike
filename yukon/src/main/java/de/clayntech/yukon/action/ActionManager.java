package de.clayntech.yukon.action;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ActionManager {

    public void execute(NativeAction action) throws Exception {
        execute(action.getActionName());
    }

    public enum NativeAction {
        RELOAD_APPLICATIONS("yukon.applications.reload");

        private final String actionName;

        NativeAction(String actionName) {
            this.actionName = actionName;
        }

        public String getActionName() {
            return actionName;
        }
    }

    private static final ActionManager INSTANCE=new ActionManager();

    private final Map<String, Callable<Void>> actions=new HashMap<>();

    public void register(String name, Callable<Void> action) {
        actions.put(name,action);
    }

    public void execute(String name) throws Exception {
        if(actions.containsKey(name)) {
            actions.get(name).call();
        }
    }

    public static ActionManager getInstance() {
        return INSTANCE;
    }
}
