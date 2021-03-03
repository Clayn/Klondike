package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.util.ApplicationFormatter;
import de.clayntech.klondike.sdk.util.Formatter;

import java.util.Scanner;

public class GetSingleApplication implements Command {
    @SuppressWarnings("unchecked")
    @Override
    public String perform(Klondike klondike, Scanner input, String... args) throws Exception {
        if(args==null||args.length<1) {
            throw new IllegalArgumentException();
        }
        String appName=args.length>1?args[1]:args[0];
        KlondikeApplication app=klondike.getRepository().getApplication(appName);
        if(app==null) {
            throw new IllegalArgumentException();
        }
        Formatter<KlondikeApplication> formatter=args.length==1?new ApplicationFormatter(): (Formatter<KlondikeApplication>) Class.forName(args[0]).getConstructor().newInstance();
        System.out.print(formatter.toString(app));
        System.out.println();
        return null;
    }
}
