package de.clayntech.yukon;

import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.yukon.i18n.YukonTranslator;
import de.clayntech.yukon.ui.FXMLFile;
import de.clayntech.yukon.ui.YukonImage;
import de.clayntech.yukon.util.ImageHelper;
import de.clayntech.yukon.util.UILoader;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.Window;
import jfxtras.styles.jmetro.Style;

public class Yukon extends Application {

    private static final YukonTranslator TRANSLATOR= (YukonTranslator) YukonTranslator.getTranslator();
    private static Window mainWindow;

    public static Window getYukonWindow() {
        return mainWindow;
    }

    @Override
    public void start(Stage stage) throws Exception {
        mainWindow=stage;
        stage.setScene(UILoader.prepare(FXMLFile.MAIN_WINDOW).with(Style.DARK).with("/style/yukon.css").load().getScene());
        KlondikeLoggerFactory.getLogger().debug("Show stage");
        stage.setTitle("Yukon");
        ImageHelper.applyImage(stage, YukonImage.LOGO);
        stage.show();
    }

    public static YukonTranslator getTranslator() {
        return TRANSLATOR;
    }
}
