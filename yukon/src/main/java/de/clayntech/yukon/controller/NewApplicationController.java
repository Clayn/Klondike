package de.clayntech.yukon.controller;

import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.bridge.KlondikeBridge;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class NewApplicationController implements Initializable {

    @FXML
    private TextField nameField;

    @FXML
    private TextField pathField;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void onSelectFile() {
        String current=pathField.getText();
        File f=current.isBlank()?null:new File(current);
        boolean setDirectory=f!=null&&(f.exists()||f.getParentFile()!=null&&f.getParentFile().exists());
        FileChooser chooser=new FileChooser();
        if(setDirectory) {
            chooser.setInitialDirectory(f.getParentFile());
            chooser.setInitialFileName(f.getName());
        }
        File selected= chooser.showOpenDialog(Yukon.getYukonWindow());
        if(selected!=null) {
            pathField.setText(selected.getAbsolutePath());
        }
    }

    @FXML
    private void onSave() {
        KlondikeBridge.callKlondike("add",nameField.getText(),pathField.getText());
    }

    @FXML
    private void onCancel() {
        nameField.getParent().getScene().getWindow().hide();
    }
}
