package de.clayntech.klondike.impl.i18n;

import de.clayntech.klondike.sdk.i18n.Translator;

import java.util.*;

public class KlondikeTranslator implements Translator {
    private final Map<Locale, ResourceBundle> loadedBundles=new HashMap<>();

    private ResourceBundle loadBundle(Locale l) {
        if(loadedBundles.containsKey(l)) {
            return loadedBundles.get(l);
        }
        ResourceBundle bundle;
        Locale toUse=l;
        try{
            bundle=ResourceBundle.getBundle("klondike",toUse);
        } catch (MissingResourceException e) {
            toUse= Locale.ROOT;
            bundle=ResourceBundle.getBundle("klondike",toUse);
        }
        if(!loadedBundles.containsKey(toUse)) {
            loadedBundles.put(toUse,bundle);
        }
        return bundle;
    }
    @Override
    public String translate(String key, Locale language) {
        return loadBundle(language).getString(key);
    }
}
