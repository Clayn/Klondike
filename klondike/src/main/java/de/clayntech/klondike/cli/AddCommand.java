package de.clayntech.klondike.cli;

import de.clayntech.klondike.impl.KlondikeApplicationImpl;
import de.clayntech.klondike.impl.KlondikeApplicationRepository;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;

import java.io.File;
import java.nio.file.Path;
import java.util.Scanner;

public class AddCommand implements Command{
    @Override
    public String perform(Scanner input, String... args) throws Exception {
        if(args==null||args.length!=2) {
            System.out.println("Add needs two arguments. Usage: 'add <name> <executable>'");
            return null;
        }
        String name=args[0];
        String exe=args[1];
        KlondikeApplication app=new KlondikeApplicationImpl(name,new File(exe));
        ApplicationRepository repository=new KlondikeApplicationRepository(Path.of(System.getProperty("user.dir")));
        repository.register(app);
        System.out.println("Application: "+name+" added successfully");
        return null;
    }
}
