package de.clayntech.klondike.cli;

import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.param.ParameterDefinition;
import de.clayntech.klondike.sdk.param.StepParameter;
import de.clayntech.klondike.sdk.param.TypeConverter;
import de.clayntech.klondike.sdk.param.TypeConverterFactory;

import java.util.*;

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

    public static Step parseStep(String[] args) throws Exception {
        if(args.length<1) {
            throw new IllegalArgumentException();
        }
        Class<? extends Step> stepClass= (Class<? extends Step>) Class.forName(args[0]);
        Map<String,String> parameter=separateParameter("-SP",args);
        Map<String,String> types=separateParameter("-C",args);
        Step step=stepClass.getConstructor().newInstance();
        Set<ParameterDefinition> definitions=new HashSet<>(Arrays.asList(step.getClass().getAnnotation(StepDefinition.class).parameter()));
        TypeConverterFactory factory=TypeConverterFactory.getFactory();
        Set<String> handledParameter=new HashSet<>();
        for(ParameterDefinition def:definitions) {
            StepParameter stepParameter=null;
            if(!def.optional()&&!parameter.containsKey(def.name())) {
                throw new RuntimeException();
            }
            TypeConverter<?> converter=factory.getConverter(def.type());
            if(!def.optional()&&converter==null) {
                throw new RuntimeException();
            }
            if(converter!=null) {
                stepParameter=new StepParameter<>(def.name(),def.type());
                stepParameter.setOptional(def.optional());
                Object val=parameter.containsKey(def.name())?converter.fromString(parameter.get(def.name())):null;
                stepParameter.setValue(val);
            }
            if(stepParameter!=null) {
                step.getParameter().add(stepParameter);
                handledParameter.add(def.name());
            }
        }
        for(Map.Entry<String,String> entry:parameter.entrySet()) {

            String name=entry.getKey();
            if(handledParameter.contains(name)) {
                continue;
            }
            String value=entry.getValue();
            Class<?> parType=types.containsKey(name)?Class.forName(types.get(name)):String.class;
            StepParameter stepParameter;
            TypeConverter<?> converter=factory.getConverter(parType);
            if(converter==null) {
                continue;
            }
            stepParameter=new StepParameter<>(name,parType);
            stepParameter.setOptional(true);
            Object val=parameter.containsKey(name)?converter.fromString(value):null;
            stepParameter.setValue(val);
            step.getParameter().add(stepParameter);
            handledParameter.add(name);
        }
        return step;
    }

    private static Map<String,String> separateParameter(String start,String... args) {
        Map<String,String> map=new HashMap<>();
        for(String arg:args) {
            if(arg.startsWith(start)) {
                String use=arg.substring(start.length());
                int index=use.indexOf("=");
                String key=use.substring(0,index);
                String value=use.substring(index+1);
                map.put(key,value);
            }
        }
        return map;
    }
}
