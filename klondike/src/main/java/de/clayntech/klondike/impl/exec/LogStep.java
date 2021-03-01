package de.clayntech.klondike.impl.exec;

import de.clayntech.klondike.impl.i18n.KlondikeTranslator;
import de.clayntech.klondike.sdk.exec.ExecutionContext;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.param.ParameterDefinition;


@StepDefinition(name = "klondike.impl.log",translator = KlondikeTranslator.class,parameter = {
        @ParameterDefinition(type= String.class,value=LogStep.MESSAGE_PARAMETER,optional = false)
})
public class LogStep extends Step {
    static final String MESSAGE_PARAMETER="log.message";

    @Override
    public void execute(ExecutionContext context) {
        String message=context.getParameter().get(MESSAGE_PARAMETER,String.class);
        if(message==null) {
            message=context.getSharedParameter().get(MESSAGE_PARAMETER,String.class);
        }
        LOG.debug(message);
    }
}
