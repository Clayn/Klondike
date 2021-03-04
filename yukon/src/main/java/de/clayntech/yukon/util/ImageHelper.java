package de.clayntech.yukon.util;

import de.clayntech.yukon.ui.YukonImage;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class ImageHelper {

    public static Image loadImage(YukonImage image) {
        return new Image(ImageHelper.class.getResource(image.getPath()).toString());
    }


    public static void applyImage(Stage stage,YukonImage image) {
        stage.getIcons().add(loadImage(image));
    }
}
