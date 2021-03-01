package de.clayntech.klondike.impl.exec;

import de.clayntech.klondike.impl.i18n.KlondikeTranslator;
import de.clayntech.klondike.sdk.exec.ExecutionContext;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.os.OSHandle;
import de.clayntech.klondike.sdk.param.ParameterDefinition;

import java.io.File;
import java.io.IOException;

@StepDefinition(name = "klondike.impl.launch",translator = KlondikeTranslator.class,parameter = {
        @ParameterDefinition(type= File.class,value="launch.workingdir",optional = false)
})
public class LaunchStep extends Step {
    @Override
    public void execute(ExecutionContext context) throws IOException {
        File executable=context.getExecutable();
        File workingDir=context.getParameter().get("launch.workingdir",executable.getParentFile());
        LOG.debug("Launching {} with working directory: {}",executable,workingDir);
        OSHandle.getHandle().execute(workingDir,executable,true);
    }
}
