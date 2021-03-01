package de.clayntech.klondike.sdk.exec;

import de.clayntech.klondike.sdk.i18n.Translator;
import de.clayntech.klondike.sdk.param.ParameterDefinition;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface StepDefinition {
    String name();

    Class<? extends Translator> translator() default NoTranslator.class;

    ParameterDefinition[] parameter() default {};

    class NoTranslator implements Translator {

        @Override
        public String translate(String key, Locale language) {
            return key;
        }
    }
}
