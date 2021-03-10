package de.clayntech.yukon.ui.dialog;

import de.clayntech.klondike.util.KlondikeVersion;
import de.clayntech.klondike.util.Version;
import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.ui.YukonImage;
import de.clayntech.yukon.util.ImageHelper;
import de.clayntech.yukon.util.YukonVersion;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Window;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DialogBuilder {

    private final Alert.AlertType type;
    private Node graphic;
    private String title;
    private String header;
    private String content;
    private Node graphicContent;
    private Window owner;
    private final List<ButtonType> buttons=new ArrayList<>();


    public static class ButtonOption {
        private final List<ButtonType> types=new ArrayList<>();

        public ButtonOption(ButtonType... options) {
            types.addAll(Arrays.asList(options));
        }

        public static ButtonOption createYesNoCancelOption() {
            ButtonType yes=createType("button.yes", ButtonBar.ButtonData.YES);
            ButtonType no=createType("button.no", ButtonBar.ButtonData.NO);
            ButtonType cancel=createType("button.cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            return new ButtonOption(yes,no,cancel);
        }

        public static ButtonType createType(String translationKey, ButtonBar.ButtonData data) {
            return new ButtonType(Yukon.getTranslator().translate(translationKey).get(),data);
        }
    }

    public DialogBuilder(Alert.AlertType type) {
        this.type = type;
    }

    public static DialogBuilder prepare(Alert.AlertType type) {
        return new DialogBuilder(type);
    }

    public DialogBuilder with(ButtonType type,ButtonType... additional)
    {
        buttons.add(type);
        if(additional!=null&&additional.length>0) {
            buttons.addAll(Arrays.asList(additional));
        }
        return this;
    }

    public DialogBuilder with(ButtonOption option) {
        this.buttons.addAll(option.types);
        return this;
    }

    public DialogBuilder with(Window owner) {
        this.owner=owner;
        return this;
    }

    public DialogBuilder withGraphic(Node graphic) {
        this.graphic=graphic;
        return this;
    }

    public DialogBuilder withTitle(String title) {
        this.title=title;
        return this;
    }

    public DialogBuilder withHeader(String header) {
        this.header=header;
        return this;
    }

    public DialogBuilder withContent(String content) {
        this.content=content;
        return this;
    }

    public DialogBuilder withContent(Node content) {
        this.graphicContent=content;
        return this;
    }

    public Alert build() {
        Alert al=new Alert(type);
        al.setTitle(title);
        al.setHeaderText(header);
        al.setContentText(content);
        al.initModality(Modality.APPLICATION_MODAL);
        al.initOwner(owner);
        if(!buttons.isEmpty()) {
            al.getButtonTypes().clear();
            al.getButtonTypes().addAll(buttons);
        }
        if(graphic!=null) {
            al.setGraphic(graphic);
        }
        if(graphicContent!=null) {
            al.getDialogPane().setContent(graphicContent);
        }
        JMetro metro=new JMetro(Style.DARK);
        metro.setAutomaticallyColorPanes(true);
        metro.setParent(al.getDialogPane());
        return al;
    }

    public static Alert getAboutDialog() {
        Node content;
        VBox box=new VBox(5);
        Version klondikeVersion=new KlondikeVersion();
        Version yukonVersion=new YukonVersion();

        Label klondike=new Label(String.format("Klondike: %s",klondikeVersion.getApplicationVersion()));
        Label yukon=new Label(String.format("Yukon: %s",yukonVersion.getApplicationVersion()));
        Label java=new Label(String.format("Java: %s",System.getProperty("java.version")));

        box.getChildren().addAll(klondike,yukon,java);

        content=box;

        return DialogBuilder.prepare(Alert.AlertType.INFORMATION)
                .withTitle("About")
                .withContent(content)
                .withGraphic(new ImageView(ImageHelper.loadImage(YukonImage.LOGO)))
                .with(ButtonOption.createType("button.ok", ButtonBar.ButtonData.OK_DONE))
                .build();
    }
}
