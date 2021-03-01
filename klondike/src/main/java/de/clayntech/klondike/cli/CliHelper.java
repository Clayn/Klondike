package de.clayntech.klondike.cli;

import java.util.ArrayList;
import java.util.List;

public class CliHelper {

    @SuppressWarnings("ConstantConditions")
    public static String[] analyzeInput(String input) {
        int space=input.indexOf(" ");
        if(space<=0) {
            return new String[0];
        }else {
            String analyze=input.substring(space+1);
            List<String> arguments=new ArrayList<>();
            StringBuilder buffer=new StringBuilder();
            boolean backSlash=false;
            boolean escape=false;
            char ch;
            for(int i=0;i<analyze.length();++i) {
                ch=analyze.charAt(i);
                if(ch=='"'&&escape&&!backSlash) {
                    if(!buffer.toString().isBlank()) {
                        arguments.add(buffer.toString());
                    }
                    buffer=new StringBuilder();
                    escape=false;
                }
                else if(ch=='"'&&!escape&&!backSlash) {
                    char ahead;
                    for(int j=i+1;j<analyze.length();j++) {
                        ahead=analyze.charAt(j);
                        if(ahead=='"'&&ahead-1!='\\') {
                            escape=true;
                            break;
                        }
                    }
                }
                else if(ch=='\\'&&escape) {
                    if(i<analyze.length()-1) {
                        char next=analyze.charAt(i+1);
                        if(next=='"') {
                            i+=1;
                            buffer.append('"');
                        }else {
                            buffer.append(ch);
                        }
                    }else {
                        buffer.append(ch);
                    }
                }
                else if(ch=='\\'&&!backSlash) {
                    backSlash=true;
                }else if(backSlash) {
                    backSlash=false;
                    buffer.append(ch);
                }else if(ch==' '&&escape){
                    buffer.append(ch);
                }else if(ch==' '&&!escape) {
                    if(!buffer.toString().isBlank()) {
                        arguments.add(buffer.toString());
                    }
                    buffer=new StringBuilder();
                }else {
                    buffer.append(ch);
                }
            }
            if(buffer.length()>0&&!buffer.toString().isBlank()) {
                arguments.add(buffer.toString());
            }
            return arguments.toArray(new String[0]);
        }
    }
}
