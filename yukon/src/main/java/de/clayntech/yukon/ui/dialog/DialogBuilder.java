package de.clayntech.yukon.ui.dialog;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Window;

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

        public static ButtonOption createYesNoCancelOption(String textYes, String textNo, String textCancel) {
            ButtonType yes=new ButtonType(textYes, ButtonBar.ButtonData.OK_DONE);
            ButtonType no=new ButtonType(textNo, ButtonBar.ButtonData.NO);
            ButtonType cancel=new ButtonType(textCancel, ButtonBar.ButtonData.CANCEL_CLOSE);
            return new ButtonOption(yes,no,cancel);
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
        al.getButtonTypes().clear();
        if(graphic!=null) {
            al.setGraphic(graphic);
        }
        if(graphicContent!=null) {
            al.getDialogPane().setContent(graphicContent);
        }
        al.getButtonTypes().addAll(buttons);
        return al;
    }
}
