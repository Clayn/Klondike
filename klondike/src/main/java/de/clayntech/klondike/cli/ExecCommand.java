package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.impl.KlondikeRunner;
import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;

import java.util.Scanner;

public class ExecCommand implements Command{
    @Override
    public String perform(Klondike klondike, Scanner input, String... args) {
        if(args==null||args.length!=1) {
            throw new InvalidParameterException("exec",1,"<Application Name>");
        }
        String appName=args[0];
        ApplicationRepository repository= klondike.getRepository();
        KlondikeApplication app=repository.getApplication(appName);
        if(app==null) {
            System.out.println("No app found with name: "+appName);
            return null;
        }
        KlondikeRunner runner=klondike.getRunner();
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
