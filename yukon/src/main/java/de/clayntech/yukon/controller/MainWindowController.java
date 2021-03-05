package de.clayntech.yukon.controller;

import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.yukon.action.ActionManager;
import de.clayntech.yukon.bridge.KlondikeBridge;
import de.clayntech.yukon.bridge.YukonRepository;
import de.clayntech.yukon.ui.components.ApplicationView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {

    @FXML
    private BorderPane pane;
    @FXML
    private FlowPane content;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadApplications();
        ActionManager.getInstance().register(ActionManager.NativeAction.RELOAD_APPLICATIONS.getActionName(),this::loadApplications);
    }

    @SuppressWarnings("SameReturnValue")
    private Void loadApplications() {
        content.getChildren().clear();
        for(KlondikeApplication app:new YukonRepository().getApplications()) {
            ApplicationView view=new ApplicationView();
            view.setApplication(app);
            view.setOnAction((evt)->KlondikeBridge.callKlondike("exec",app.getName()));
            content.getChildren().add(view);
        }
        return null;
    }
}
