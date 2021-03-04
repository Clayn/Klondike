package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;

import java.io.IOException;
import java.util.Scanner;

public class EditCommand implements Command{

    public static String EDIT_NAME="name";

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
        }

        return null;
    }

    private void editName(ApplicationRepository repository, KlondikeApplication app, String newName) throws IOException {
        KlondikeLoggerFactory.getLogger().debug("Changing name from {} to {}",app.getName(),newName);
        repository.update(app,newName);
    }
}
