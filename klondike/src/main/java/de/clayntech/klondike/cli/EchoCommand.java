package de.clayntech.klondike.cli;

import java.util.Scanner;

public class EchoCommand implements Command{
    @Override
    public String perform(Scanner input, String... args) {
        StringBuilder builder=new StringBuilder();
        if(args!=null&&args.length>0) {
            for(int i=0;i<args.length-1;++i) {
                builder.append(args[i]).append(" ");
            }
            builder.append(args[args.length-1]);
        }
        System.out.println(builder.toString());
        return null;
    }
}
