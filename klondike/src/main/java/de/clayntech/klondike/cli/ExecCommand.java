package de.clayntech.klondike.cli;

import de.clayntech.klondike.impl.KlondikeApplicationRepository;
import de.clayntech.klondike.impl.KlondikeRunner;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;

import java.nio.file.Path;
import java.util.Scanner;

public class ExecCommand implements Command{
    @Override
    public String perform(Scanner input, String... args) throws Exception {
        if(args==null||args.length!=1) {
            throw new IllegalArgumentException();
        }
        String appName=args[0];
        ApplicationRepository repository=new KlondikeApplicationRepository(Path.of(System.getProperty("user.dir")));
        KlondikeApplication app=repository.getApplications().stream().filter((a)->a.getName().equals(appName)).findFirst().orElse(null);
        if(app==null) {
            System.out.println("No app found with name: "+appName);
            return null;
        }
        KlondikeRunner runner=new KlondikeRunner();
        Thread t=new Thread(()-> {
            try {
                runner.execute(app);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
        t.setDaemon(true);
        t.start();
        return null;
    }
}
