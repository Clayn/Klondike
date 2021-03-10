package de.clayntech.yukon.i18n;

import de.clayntech.klondike.sdk.i18n.Translator;
import javafx.beans.property.StringProperty;

import java.util.Locale;

public interface FXTranslator extends Translator {

    void setLocale(Locale locale);

    Locale getLocale();

    StringProperty translate(String key);
}
