package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.impl.KlondikeRunner;
import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.util.KlondikeVersion;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class KlondikeCli {
    private static final String DEFAULT_PROMPT="klondike> ";
    private static final String EXIT="exit";
    private String prompt=DEFAULT_PROMPT;

    private final Map<String,Command> commands=new HashMap<>();
    private final KlondikeRunner runner=new KlondikeRunner();
    private Scanner sc;
    private final Klondike klondike;

    public KlondikeCli(Klondike klondike) {
        this.klondike = klondike;
    }

    {
        commands.put("echo",new EchoCommand());
        commands.put("add",new AddCommand());
        commands.put("list",new ListCommand());
        commands.put("exec",new ExecCommand());
    }

    private void print(String line,boolean newLine) {
        System.out.print(line);
        if(newLine) {
            System.out.println();
        }
    }

    private String getInput() {
        if(sc==null) {
            sc=new Scanner(System.in);
        }
        print(prompt,false);
        return sc.nextLine();
    }

    private Command getCommand(String input) {
        int space=input.indexOf(" ");
        String com=space<=0?input:input.substring(0,space);
        Command cmd=commands.get(com);
        if(cmd==null) {
            print("No command found for '"+com+"'",true);
        }
        return cmd;
    }

    public void execute(String command, String... args) {
        klondike.setState(0);
        Command com=getCommand(command);
        if(com!=null) {
            String newPrompt= null;
            try {
                newPrompt = com.perform(klondike,sc, args);
            } catch (Exception e) {
                KlondikeLoggerFactory.getLogger().error("",e);
                klondike.setState(-1);
            }
            prompt=newPrompt==null?DEFAULT_PROMPT:newPrompt;
        }
    }

    public void start() {
        boolean run;
        String input;
        KlondikeVersion.printHello(System.out);
        do{
            input=getInput();
            run=!input.startsWith(EXIT);
            if(run) {
                String command=input.substring(0,input.contains(" ")?input.indexOf(" "):input.length());
                String[] arguments=CliHelper.analyzeInput(input);
                execute(command,arguments);
            }
        }while(run);
    }
}
