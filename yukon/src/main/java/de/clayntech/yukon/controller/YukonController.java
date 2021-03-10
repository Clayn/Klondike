package de.clayntech.yukon.controller;

import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.i18n.FXTranslator;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class YukonController implements Initializable {

    protected URL url;
    protected ResourceBundle resources;
    protected static final FXTranslator translator= Yukon.getTranslator();

    protected void onInit() {

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.url=url;
        this.resources=resourceBundle;
        onInit();
    }
}
