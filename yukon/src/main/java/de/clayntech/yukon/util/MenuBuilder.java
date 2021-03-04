package de.clayntech.yukon.util;

import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.ui.FXMLFile;
import de.clayntech.yukon.ui.YukonImage;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;

public class MenuBuilder {

    public static MenuBar createMainMenu() {
        MenuBar bar=new MenuBar();
        Menu file=new Menu("File");
        MenuItem newApp=new MenuItem("New");
        newApp.setOnAction(actionEvent -> {
            Stage st = new Stage();
            try {
                st.setScene(UILoader.prepare(FXMLFile.NEW_DIALOG).with(Style.DARK).with("/style/yukon.css")
                        .load().getScene());
                st.initOwner(Yukon.getYukonWindow());
                st.initModality(Modality.APPLICATION_MODAL);
                ImageHelper.applyImage(st, YukonImage.LOGO);
                st.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        MenuItem exit=new MenuItem("Exit");
        exit.setOnAction((evt)-> Platform.exit());
        file.getItems().addAll(newApp,exit);
        bar.getMenus().addAll(file);
        return bar;
    }
}
