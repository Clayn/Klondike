package de.clayntech.yukon.controller;

import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.i18n.Translator;
import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.action.ActionManager;
import de.clayntech.yukon.bridge.YukonRepository;
import de.clayntech.yukon.i18n.YukonTranslationHelper;
import de.clayntech.yukon.ui.components.ApplicationInformationController;
import de.clayntech.yukon.ui.components.EditControl;
import de.clayntech.yukon.ui.components.StepView;
import de.clayntech.yukon.ui.dialog.DialogBuilder;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditApplicationController implements Initializable,DataController<KlondikeApplication> {
    @FXML
    private TreeView<Object> appInfo;
    @FXML
    private BorderPane content;

    private final ObjectProperty<KlondikeApplication> application=new SimpleObjectProperty<>();
    private final StringProperty oldName=new SimpleStringProperty();
    private final Map<Object,Node> cachedNodes=new HashMap<>();
    private final Map<Object,DataController<?>> cachedData=new HashMap<>();

    private void populateTree(Observable observable) {
        cachedNodes.clear();
        appInfo.setRoot(null);
        if(application.get()!=null) {
            KlondikeApplication app=application.get();
            TreeItem<Object> root=new TreeItem<>(app);
            for(Step step:app.getScript().getSteps()) {
                TreeItem<Object> stepItem=new TreeItem<>(step);
                root.getChildren().add(stepItem);
            }
            root.setExpanded(true);
            appInfo.setRoot(root);
        }
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

    public void setOldName(String oldName) {
        this.oldName.set(oldName);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        application.addListener(this::populateTree);
        appInfo.setCellFactory(new Callback<>() {
            @Override
            public TreeCell<Object> call(TreeView<Object> objectTreeView) {
                return new TreeCell<>() {
                    @Override
                    protected void updateItem(Object item, boolean empty) {
                        super.updateItem(item, empty);
                        setText("");
                        if (empty || item == null) {
                            return;
                        }
                        if (item instanceof KlondikeApplication) {
                            KlondikeApplication app = (KlondikeApplication) item;
                            setText(app.getName());
                        } else if (item instanceof Step) {
                            Step step = (Step) item;
                            StepDefinition def = step.getClass().getAnnotation(StepDefinition.class);
                            if (def == null) {
                                throw new RuntimeException();
                            }
                            Translator translator = YukonTranslationHelper.getTranslator(step);
                            String stepName = translator == null ? def.name() : translator.translate(def.name(), Yukon.getTranslator().getLocale());
                            setText(stepName);
                        }
                    }
                };
            }
        });
        appInfo.getSelectionModel().selectedItemProperty().addListener((observableValue, objectTreeItem, t1) -> {
            content.setCenter(null);
            if (t1 != null) {
                Object value = t1.getValue();

                if (value == null) {
                    return;
                } else if (cachedNodes.containsKey(value)) {
                    content.setCenter(cachedNodes.get(value));
                } else if (value instanceof KlondikeApplication) {
                    KlondikeApplication app = (KlondikeApplication) value;
                    ApplicationInformationController controller = new ApplicationInformationController();
                    controller.setApplication(app);
                    if (!cachedData.containsKey(value)) {
                        cachedData.put(value, controller);
                    }

                    Yukon.getTranslator().applyTranslation(controller);
                    content.setCenter(controller);
                } else if (value instanceof Step) {
                    EditControl<?> control = buildStepNode((Step) value);
                    if (!cachedData.containsKey(value)) {
                        cachedData.put(value, control);
                    }
                    Yukon.getTranslator().applyTranslation(control);
                    content.setCenter(control);
                }
                if (!cachedNodes.containsKey(value)) {
                    cachedNodes.put(value, content.getCenter());
                }

            }
        });
    }

    private EditControl<Step> buildStepNode(Step step) {
        StepView view=new StepView();
        view.setControlledObject(step);
        return view;
    }

    @Override
    public boolean hasChanges() {
        return cachedData.values()
                .stream().anyMatch(DataController::hasChanges);
    }

    @Override
    public boolean save() {
        for(DataController<?> controller:cachedData.values()) {
            if(!controller.save()) {
                return false;
            }
        }
        boolean updateName=!oldName.get().equals(application.get().getName());
        ApplicationRepository repository=new YukonRepository();
        try {
            if (updateName) {
                String newName=application.get().getName();
                application.get().setName(oldName.get());
                repository.update(application.get(), newName);
            } else {
                repository.update(application.get());
            }
        }catch (Exception ex) {
            throw new RuntimeException(ex);
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

    @FXML
    private void onSave() {
        if(!hasChanges()) {
            onCancel(false);
        }
        if(save()) {
            onCancel(false);
        }
    }

    private void onCancel(boolean check) {
        if(check&&hasChanges()) {
            Optional<ButtonType> type= DialogBuilder.prepare(Alert.AlertType.CONFIRMATION)
                    .withTitle(Yukon.getTranslator().translate("message.changes.title").get())
                    .withContent(Yukon.getTranslator().translate("message.changes").get())
                    .with(DialogBuilder.ButtonOption.createYesNoCancelOption())
                    .with(Yukon.getYukonWindow())
                    .build().showAndWait();
            if(type.isPresent()) {
                if(type.get().getButtonData()== ButtonBar.ButtonData.OK_DONE) {
                    onSave();
                }else if(type.get().getButtonData()== ButtonBar.ButtonData.CANCEL_CLOSE){
                    return;
                }else {
                    onCancel(false);
                }
            }
        }
        content.getScene().getWindow().hide();
        try {
            ActionManager.getInstance().execute(ActionManager.NativeAction.RELOAD_APPLICATIONS.getActionName());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void onCancel() {
        onCancel(true);
    }
}
