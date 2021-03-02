package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.impl.KlondikeApplicationImpl;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.err.NameInUseException;

import java.io.File;
import java.util.Scanner;

public class AddCommand implements Command{
    @Override
    public String perform(Klondike klondike, Scanner input, String... args) throws Exception {
        if(args==null||args.length!=2) {
            System.out.println("Add needs two arguments. Usage: 'add <name> <executable>'");
            return null;
        }
        String name=args[0];
        String exe=args[1];
        KlondikeApplication app=new KlondikeApplicationImpl(name,new File(exe));
        ApplicationRepository repository= klondike.getRepository();
        //noinspection SpellCheckingInspection
        try {
            repository.register(app);
            System.out.println("Application: "+name+" added successfully");
        }catch (NameInUseException niue) {
            System.err.println("Can't add application with name '"+name+"' as there already exists such an application");
            klondike.setState(1001);
        }
        return null;
    }
}
