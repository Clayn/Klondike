package de.clayntech.yukon.bridge;

import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;
import de.clayntech.klondike.sdk.param.TypeConverter;
import de.clayntech.klondike.sdk.param.TypeConverterFactory;
import de.clayntech.klondike.sdk.util.ApplicationFormatter;
import de.clayntech.klondike.sdk.util.Formatter;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class YukonRepository implements ApplicationRepository {
    private final Logger LOG= KlondikeLoggerFactory.getLogger();
    private final Formatter<KlondikeApplication> formatter=new ApplicationFormatter();

    public YukonRepository() {

    }

    @Override
    public List<KlondikeApplication> getApplications() {
        String in=null;
        List<KlondikeApplication> applications=new ArrayList<>();
        List<String> candidates=new ArrayList<>();
        for(String candidate:KlondikeBridge.callKlondike("list")) {
            if(candidate.contains("\t-\t")) {
                candidates.add(candidate.substring(0,candidate.indexOf("\t-\t")));
            }
        }
        for(String candidate:candidates) {
            for(String app:KlondikeBridge.callKlondike("get",candidate)) {
                applications.add(formatter.fromString(app));
            }
        }
        return applications;
    }

    @Override
    public void register(KlondikeApplication app) {
        KlondikeBridge.callKlondike("add",app.getName(),app.getExecutable().getAbsolutePath());
    }

    @SuppressWarnings({"unchecked","rawtypes"})
    @Override
    public void update(KlondikeApplication app) {
        TypeConverterFactory factory=TypeConverterFactory.getFactory();
        for(int i=0;i<app.getScript().getSteps().size();++i) {
            Step step=app.getScript().getSteps().get(i);
            List<String> parameterValues=new ArrayList<>();
            List<String> parameterTypes=new ArrayList<>();
            List<String> stepArgs=new ArrayList<>();
            stepArgs.add("edit");
            stepArgs.add(app.getName());
            stepArgs.add("step");
            stepArgs.add(String.valueOf(i));
            stepArgs.add(step.getClass().getName());
            for(StepParameter parameter:step.getParameter()) {
                Object value=parameter.getValue();
                if(value==null) {
                    continue;
                }
                TypeConverter converter=factory.getConverter(parameter.getTypeClass());
                try {
                    String stringVal=converter.toString(value);
                    parameterValues.add(String.format("-SP%s=%s", parameter.getName(),stringVal));
                    parameterTypes.add(String.format("-C%s=%s",parameter.getName(),value.getClass().getName()));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if(!parameterValues.isEmpty()&&!parameterTypes.isEmpty()) {
                stepArgs.addAll(parameterValues);
                stepArgs.addAll(parameterTypes);
            }
            String[] args=stepArgs.toArray(new String[0]);
            KlondikeBridge.callKlondike(args);
        }
    }

    @Override
    public void update(KlondikeApplication app, String newName) {
        KlondikeBridge.callKlondike("edit",app.getName(),"name",newName);
        update(app);
    }
}
