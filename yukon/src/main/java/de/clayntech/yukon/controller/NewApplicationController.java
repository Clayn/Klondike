package de.clayntech.yukon.controller;

import de.clayntech.klondike.impl.KlondikeApplicationImpl;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.yukon.action.ActionManager;
import de.clayntech.yukon.bridge.YukonRepository;
import de.clayntech.yukon.ui.components.ApplicationInformationController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewApplicationController implements Initializable {

    @FXML
    private ApplicationInformationController controller;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        controller.setOnSave(this::onSave);
        controller.setOnCancel(this::onCancel);
    }

    @FXML
    private void onSave(ActionEvent evt) {
        KlondikeApplication app=new KlondikeApplicationImpl();
        app.setName(controller.getName());
        app.setExecutable(new File(controller.getPath()));
        ApplicationRepository repository=new YukonRepository();
        try {
            repository.register(app);
            ActionManager.getInstance().execute(ActionManager.NativeAction.RELOAD_APPLICATIONS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onCancel(ActionEvent evt) {
        controller.getWindow().hide();
    }
}
