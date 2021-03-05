package de.clayntech.klondike.impl.exec;

import de.clayntech.klondike.impl.KlondikeRunner;
import de.clayntech.klondike.impl.i18n.KlondikeTranslator;
import de.clayntech.klondike.sdk.evt.Events;
import de.clayntech.klondike.sdk.exec.ExecutionContext;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.os.OSHandle;
import de.clayntech.klondike.sdk.param.ParameterDefinition;
import de.clayntech.klondike.sdk.param.types.Directory;

import java.io.File;
import java.io.IOException;

@StepDefinition(name = "klondike.impl.launch",translator = KlondikeTranslator.class,parameter = {
        @ParameterDefinition(type= Directory.class, name ="launch.workingdir",optional = false)
})
public class LaunchStep extends Step {
    @Override
    public void execute(ExecutionContext context) throws IOException {
        File executable=context.getExecutable();
        File workingDir=context.getParameter().get("launch.workingdir",executable.getParentFile());
        context.getSharedParameter().set(LogStep.MESSAGE_PARAMETER,String.format("Launching application %s with %s",context.getParameter().get(KlondikeRunner.APP_NAME,String.class),executable.getAbsolutePath()));
        context.trigger(Events.PRE_EXECUTION);
        OSHandle.getHandle().execute(workingDir, executable, true, integer -> {
            context.getSharedParameter().set(LogStep.MESSAGE_PARAMETER, String.format("Application finished with: %d", integer));
            context.trigger(Events.POST_EXECUTION);
        });

    }
}
