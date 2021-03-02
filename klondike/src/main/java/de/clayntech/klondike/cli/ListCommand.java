package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;

import java.nio.file.Path;
import java.util.Scanner;

public class ListCommand implements Command{
    @Override
    public String perform(Klondike klondike,Scanner input, String... args) {
        ApplicationRepository repository= klondike.getRepository();
        System.out.println("Listing applications in: "+Path.of(System.getProperty("user.dir")).toAbsolutePath().toString());
        for(KlondikeApplication app: repository.getApplications()) {
            System.out.printf("%s\t-\t%s%n",app.getName(),app.getExecutable().getAbsolutePath());
        }
        return null;
    }
}
