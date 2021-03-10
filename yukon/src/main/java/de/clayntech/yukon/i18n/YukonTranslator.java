package de.clayntech.yukon.i18n;

import de.clayntech.klondike.impl.i18n.KlondikeTranslator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Labeled;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class YukonTranslator extends KlondikeTranslator implements FXTranslator {

    private static final FXTranslator INSTANCE=new YukonTranslator();
    private final ObjectProperty<Locale> language=new SimpleObjectProperty<>(Locale.getDefault());
    private ResourceBundle bundle;

    private final Map<String,StringProperty> properties=new HashMap<>();

    private YukonTranslator() {

        language.addListener((observableValue, locale, t1) -> {
            bundle = loadBundle(t1);
            updateStrings();
        });
        language.get();
        bundle=loadBundle();
    }

    private void updateStrings() {
        if(bundle==null) {
            return;
        }
        for(Map.Entry<String,StringProperty> entry:properties.entrySet()) {
            entry.getValue().setValue(bundle.getString(entry.getKey()));
        }
    }

    private ResourceBundle loadBundle() {
        return loadBundle(language.get());
    }

    @Override
    public Locale getLocale() {
        return language.get();
    }

    @Override
    public void setLocale(Locale locale) {
        this.language.set(locale);
    }

    @Override
    protected ResourceBundle getResourceBundle(Locale locale) {
        return ResourceBundle.getBundle("i18n.yukon",locale);
    }

    @Override
    public StringProperty translate(String key) {
        if(properties.containsKey(key)) {
            return properties.get(key);
        }
        language.get();
        StringProperty prop=new SimpleStringProperty();
        prop.setValue(translate(key,language.get()));
        properties.put(key,prop);
        return prop;
    }

    public void applyTranslations(Scene scene) {
        applyTranslation(scene.getRoot());
    }

    public void applyTranslation(Node n) {
        if(n instanceof Labeled) {
            applyTranslation((Labeled) n);
        }
        else if(n instanceof ButtonBar) {
            applyTranslation((ButtonBar) n);
        }
        else if(n instanceof Parent) {
            applyTranslation((Parent) n);
        }
    }


    private void applyTranslation(Parent parent) {
        if(parent==null) {
            return;
        }
        for(Node n:parent.getChildrenUnmodifiable()) {
            applyTranslation(n);
        }
    }

    private void applyTranslation(ButtonBar bar) {
        bar.getButtons().forEach(this::applyTranslation);
    }

    private void applyTranslation(Labeled labeled) {
        String val=labeled.getText();
        if(val.startsWith("§")) {
            String key=val.substring(1);
            if(bundle.containsKey(key)) {
                labeled.textProperty().bind(translate(key));
            }
        }
    }

    public static FXTranslator getTranslator() {
        return INSTANCE;
    }
}
