package de.clayntech.yukon.i18n;

import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.i18n.Translator;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class YukonTranslationHelper {

    private static final Map<Class<? extends Step>,Translator> translators=new HashMap<>();

    public static Translator getTranslator(Step step) {
        if(translators.containsKey(step.getClass())) {
            return translators.get(step.getClass());
        }
        Translator trans;
        StepDefinition def=step.getClass().getAnnotation(StepDefinition.class);
        if(def==null) {
            throw new RuntimeException();
        }
        Class<? extends Translator> translatorClass=def.translator();
        if(StepDefinition.NoTranslator.class.equals(translatorClass)) {
            return new StepDefinition.NoTranslator();
        }
        try {
            trans=translatorClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        translators.put(step.getClass(),trans);
        return trans;
    }
}
