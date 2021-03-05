package de.clayntech.yukon.ui.components;

import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.controller.DataController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

public class ApplicationInformationController extends VBox implements DataController<KlondikeApplication> {
    private final ObjectProperty<EventHandler<ActionEvent>> onSave=new SimpleObjectProperty<>();
    private final ObjectProperty<EventHandler<ActionEvent>> onCancel=new SimpleObjectProperty<>();
    private final ObjectProperty<KlondikeApplication> application=new SimpleObjectProperty<>();

    public Window getWindow() {
        return nameField.getScene().getWindow();
    }

    @FXML
    private TextField nameField;

    @FXML
    private TextField pathField;

    public String getName() {
        return nameField.getText();
    }

    public String getPath() {
        return pathField.getText();
    }

    public KlondikeApplication getApplication() {
        return application.get();
    }

    public ObjectProperty<KlondikeApplication> applicationProperty() {
        return application;
    }

    public void setApplication(KlondikeApplication application) {
        this.application.set(application);
    }

    @FXML
    private void onCancel(ActionEvent evt) {
        if(onCancel.get()!=null) {
            onCancel.get().handle(evt);
        }
    }

    @FXML
    private void onSave(ActionEvent evt) {
        if(onSave.get()!=null) {
            onSave.get().handle(evt);
        }
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

    public ApplicationInformationController() {
        FXMLLoader loader=new FXMLLoader(getClass().getResource("/fxml/components/ApplicationInformation.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        application.addListener((observableValue, klondikeApplication, t1) -> {
            nameField.setText("");
            pathField.setText("");
            if (t1 != null) {
                nameField.setText(t1.getName());
                pathField.setText(t1.getExecutable().getAbsolutePath());
            }
        });
    }

    public EventHandler<ActionEvent> getOnSave() {
        return onSave.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onSaveProperty() {
        return onSave;
    }

    public void setOnSave(EventHandler<ActionEvent> onSave) {
        this.onSave.set(onSave);
    }

    public EventHandler<ActionEvent> getOnCancel() {
        return onCancel.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onCancelProperty() {
        return onCancel;
    }

    public void setOnCancel(EventHandler<ActionEvent> onCancel) {
        this.onCancel.set(onCancel);
    }

    @Override
    public boolean hasChanges() {
        if(application.get()==null) {
            return !nameField.getText().isBlank()||!pathField.getText().isBlank();
        }else {
            String name=nameField.getText();
            String path=pathField.getText();
            return !name.equals(application.get().getName())||!path.equals(application.get().getExecutable().getAbsolutePath());
        }
    }

    @Override
    public boolean save() {
        if(hasChanges()) {
            application.get().setName(nameField.getText());
            application.get().setExecutable(new File(pathField.getText()));
        }
        return true;
    }

    @Override
    public KlondikeApplication getControlledObject() {
        return getApplication();
    }

    @Override
    public void setControlledObject(KlondikeApplication object) {
        setApplication(object);
    }
}
