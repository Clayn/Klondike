package de.clayntech.yukon.util;

import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.i18n.FXTranslator;
import de.clayntech.yukon.ui.FXMLFile;
import de.clayntech.yukon.ui.YukonImage;
import de.clayntech.yukon.ui.dialog.DialogBuilder;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.Style;

import java.io.IOException;
import java.util.Locale;
import java.util.function.Supplier;

public class MenuBuilder {

    private final static FXTranslator translator=Yukon.getTranslator();
    private static Menu createMenu(String textKey) {
        Menu m=new Menu();
        StringProperty binding=translator.translate(textKey);
        m.textProperty().bind(binding);
        return m;
    }

    private static <T extends MenuItem> T createMenuItem(String textKey, Supplier<T> construct) {
        T item=construct.get();
        item.textProperty().bind(translator.translate(textKey));
        return item;
    }

    private static MenuItem createMenuItem(String textKey) {
        return createMenuItem(textKey,MenuItem::new);
    }

    public static MenuBar createMainMenu() {
        MenuBar bar=new MenuBar();
        Menu file=createMenu("menu.file");

        MenuItem newApp=createMenuItem("menu.new");
        newApp.setOnAction(actionEvent -> {
            Stage st = new Stage();
            try {
                st.setScene(UILoader.prepare(FXMLFile.NEW_DIALOG).with(Style.DARK).with("/style/yukon.css")
                        .load().getScene());
                Yukon.getTranslator().applyTranslations(st.getScene());
                st.initOwner(Yukon.getYukonWindow());
                st.initModality(Modality.APPLICATION_MODAL);
                ImageHelper.applyImage(st, YukonImage.LOGO);
                st.show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        MenuItem exit=createMenuItem("menu.exit");
        exit.setOnAction((evt)-> Platform.exit());

        Menu setting=createMenu("menu.setting");
        Menu language=createMenu("menu.language");
        RadioMenuItem german=createMenuItem("menu.german", RadioMenuItem::new);
        german.setOnAction(actionEvent -> Yukon.getTranslator().setLocale(Locale.GERMAN));
        german.setUserData(Locale.GERMAN);
        RadioMenuItem english=createMenuItem("menu.english", RadioMenuItem::new);
        english.setUserData(Locale.ENGLISH);
        english.setOnAction(actionEvent -> Yukon.getTranslator().setLocale(Locale.ENGLISH));
        ToggleGroup group=new ToggleGroup();
        group.getToggles().addAll(german,english);
        for(Toggle toggle:group.getToggles()) {
            Locale locale= (Locale) toggle.getUserData();
            if(locale.getLanguage().equals(Yukon.getTranslator().getLocale().getLanguage())) {
                toggle.setSelected(true);
                break;
            }
        }
        language.getItems().addAll(german,english);
        setting.getItems().addAll(language);
        Menu help=createMenu("menu.help");
        MenuItem about=createMenuItem("menu.about");
        about.setOnAction(actionEvent -> DialogBuilder.getAboutDialog().show());
        help.getItems().addAll(about);
        file.getItems().addAll(newApp,exit);
        bar.getMenus().addAll(file,setting,help);
        return bar;
    }
}
