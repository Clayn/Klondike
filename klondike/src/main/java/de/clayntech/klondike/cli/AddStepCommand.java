package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.Step;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * Adds a new step to an existing application. The parameters for the step can only be added if they can be converted from a string.
 * All mandatory parameters for the step must be available upon adding.
 * <br>
 * If parameters should be added that are not defined by the step, the type for that parameter must be given as full qualified class name.
 *
 * <br><br><b>Usage: </b> {@code add-step <application> <step-provider> [-Pparameter=value...] [-Cparameter=class...] [-Ttrigger...]}
 */
public class AddStepCommand implements Command{
    @Override
    public String perform(Klondike klondike, Scanner input, String... args) throws Exception {
        if(args==null||args.length<2) {
            System.err.println("Usage: 'add-step <application> <provider> [-SPparameter=value...] [-Cparameter=class]'");
            return null;
        }
        ApplicationRepository repository=klondike.getRepository();
        KlondikeApplication app=repository.getApplication(args[0]);
        if(app==null) {
            return null;
        }
        List<String> stepArgs=new ArrayList<>(args.length-1);
        stepArgs.addAll(Arrays.asList(args).subList(1, args.length));
        Step step=CliHelper.parseStep(stepArgs.toArray(new String[0]));
        app.getScript().getSteps().add(step);
        repository.update(app);
        return null;
    }



}
