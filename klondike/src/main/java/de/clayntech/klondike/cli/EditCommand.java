package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.Step;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EditCommand implements Command{

    public static final String EDIT_NAME="name";
    public static final String EDIT_STEP="step";

    @Override
    public String perform(Klondike klondike, Scanner input, String... args) throws Exception {
        String name=args[0];
        ApplicationRepository repository=klondike.getRepository();
        KlondikeApplication app=repository.getApplication(name);
        if(app==null) {
            return null;
        }
        String command=args[1];

        if(EDIT_NAME.equals(command)) {
            String newName=args[2];
            editName(repository,app,newName);
        } else if(EDIT_STEP.equals(command)) {
            int index=Integer.parseInt(args[2]);
            Step old=index<0||index>=app.getScript().getSteps().size()?null:app.getScript().getSteps().get(index);
            List<String> stepArgs = new ArrayList<>(Arrays.asList(args).subList(3, args.length));
            if(stepArgs.isEmpty()) {
                throw new IllegalArgumentException();
            }
            Step step=CliHelper.parseStep(stepArgs.toArray(new String[0]));
            if(old==null) {
                app.getScript().getSteps().add(step);
            }else {
                app.getScript().getSteps().set(index,step);
            }
            repository.update(app);
        }

        return null;
    }

    private void editName(ApplicationRepository repository, KlondikeApplication app, String newName) throws IOException {
        repository.update(app,newName);
    }
}
