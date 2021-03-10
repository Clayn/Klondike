package de.clayntech.klondike.impl.i18n;

import de.clayntech.klondike.sdk.i18n.Translator;

import java.util.*;

public class KlondikeTranslator implements Translator {
    private final Map<Locale, ResourceBundle> loadedBundles=new HashMap<>();

    protected ResourceBundle loadBundle(Locale l) {
        if(loadedBundles.containsKey(l)) {
            return loadedBundles.get(l);
        }
        ResourceBundle bundle;
        Locale toUse=l;
        try{
            bundle=getResourceBundle(toUse);
            if(!bundle.getLocale().getLanguage().equals(l.getLanguage())) {
                throw new MissingResourceException("","","");
            }
        } catch (MissingResourceException e) {
            toUse= Locale.ROOT;
            bundle=getResourceBundle(toUse);
        }
        if(!loadedBundles.containsKey(toUse)) {
            loadedBundles.put(toUse,bundle);
        }
        return bundle;
    }

    protected ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("klondike",locale);
    }

    @Override
    public String translate(String key, Locale language) {
        return loadBundle(language).getString(key);
    }
}
