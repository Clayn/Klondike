package de.clayntech.klondike.cli;

import de.clayntech.klondike.impl.KlondikeRunner;

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



    public void start() throws Exception {
        boolean run;
        String input;
        Command com;
        do{
            input=getInput();
            run=!input.startsWith(EXIT);
            if(run) {
                com=getCommand(input);
                if(com!=null) {
                    String newPrompt=com.perform(sc, CliHelper.analyzeInput(input));
                    prompt=newPrompt==null?DEFAULT_PROMPT:newPrompt;
                }
            }
        }while(run);
    }
}
