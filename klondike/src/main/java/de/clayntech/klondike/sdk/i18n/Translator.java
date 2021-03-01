package de.clayntech.klondike.sdk.i18n;

import java.util.Locale;

public interface Translator {
    String translate(String key, Locale language);
}
