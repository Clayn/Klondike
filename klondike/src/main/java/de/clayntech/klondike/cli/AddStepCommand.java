package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.param.ParameterDefinition;
import de.clayntech.klondike.sdk.param.StepParameter;
import de.clayntech.klondike.sdk.param.TypeConverter;
import de.clayntech.klondike.sdk.param.TypeConverterFactory;

import java.util.*;

/**
 * Adds a new step to an existing application. The parameters for the step can only be added if they can be converted from a string.
 * All mandatory parameters for the step must be available upon adding.
 * <br>
 * If parameters should be added that are not defined by the step, the type for that parameter must be given as full qualified class name.
 *
 * <br><br><b>Usage: </b> {@code add-step <application> <step-provider> [-Pparameter=value...] [-Cparameter=class...] [-Ttrigger...]}
 */
public class AddStepCommand implements Command{
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public String perform(Klondike klondike, Scanner input, String... args) throws Exception {
        if(args==null||args.length<2) {
            System.err.println("Usage: 'add-step <application> <provider> [-SPparameter=value...] [-Cparameter=class]'");
            return null;
        }
        ApplicationRepository repository=klondike.getRepository();
        KlondikeApplication app=repository.getApplication(args[0]);
        if(app==null) {

            return null;
        }
        Class<? extends Step> stepClass= (Class<? extends Step>) Class.forName(args[1]);
        Map<String,String> parameter=separateParameter("-SP",args);
        Map<String,String> types=separateParameter("-C",args);
        Step step=stepClass.getConstructor().newInstance();
        Set<ParameterDefinition> definitions=new HashSet<>(Arrays.asList(step.getClass().getAnnotation(StepDefinition.class).parameter()));
        TypeConverterFactory factory=TypeConverterFactory.getFactory();
        Set<String> handledParameter=new HashSet<>();
        for(ParameterDefinition def:definitions) {
            StepParameter stepParameter=null;
            if(!def.optional()&&parameter.containsKey(def.name())) {
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
        app.getScript().getSteps().add(step);
        repository.update(app);
        return null;
    }

    private Map<String,String> separateParameter(String start,String... args) {
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
