package de.clayntech.yukon.util;

import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.ArrayList;
import java.util.List;

public class LoadedFXML {
    private final Parent parent;
    private final Object controller;
    private final Style style;
    private final List<String> styleSheets=new ArrayList<>();

    public LoadedFXML(Parent parent, Object controller, Style style) {
        this.parent = parent;
        this.controller = controller;
        this.style = style;
    }

    public Parent getParent() {
        return parent;
    }

    public <T> T getController() {
        return (T)controller;
    }

    List<String> getStyleSheets() {
        return styleSheets;
    }

    public Scene getScene() {
        Scene sc=new Scene(parent);
        if(!styleSheets.isEmpty()) {
            sc.getStylesheets().addAll(styleSheets);
        }
        if(style!=null) {
            JMetro metro=new JMetro(sc,style);
            metro.setAutomaticallyColorPanes(true);
        }
        return sc;
    }
}
