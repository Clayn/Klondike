package de.clayntech.yukon.util;

import de.clayntech.yukon.ui.FXMLFile;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import jfxtras.styles.jmetro.Style;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UILoader {

    private final String fxml;
    private ResourceBundle resources;
    private final List<String> styles=new ArrayList<>();
    private Style style;

    public UILoader(String fxml) {
        this.fxml = fxml;
    }

    public static UILoader prepare(String fxml) {
        return new UILoader(fxml);
    }

    public static UILoader prepare(FXMLFile fxml) {
        return prepare(fxml.getFXML());
    }

    public UILoader with(ResourceBundle resources) {
        this.resources=resources;
        return this;
    }


    public UILoader with(String styleSheet, String... sheets) {
        styles.add(getClass().getResource(styleSheet).toExternalForm());
        if(sheets!=null&&sheets.length!=0) {
            styles.addAll(Arrays.stream(sheets)
            .map((str)->getClass().getResource(str).toExternalForm()).collect(Collectors.toList()));
        }
        return this;
    }

    public UILoader with(Style style) {
        this.style=style;
        return this;
    }

    public LoadedFXML load() throws IOException {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/"+fxml));
        loader.setResources(resources);
        Parent parent=loader.load();
        Object controller=loader.getController();
        LoadedFXML loaded=new LoadedFXML(parent,controller,style);
        if(!styles.isEmpty()) {
            loaded.getStyleSheets().addAll(styles);
        }
        return loaded;
    }
}
