package de.clayntech.yukon.ui.components;

import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.exec.StepDefinition;
import de.clayntech.klondike.sdk.i18n.Translator;
import de.clayntech.klondike.sdk.param.ParameterDefinition;
import de.clayntech.klondike.sdk.param.StepParameter;
import de.clayntech.klondike.sdk.param.types.Directory;
import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.i18n.YukonTranslationHelper;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class StepViewSkin extends SkinBase<StepView> {

    private final List<Supplier<Boolean>> saveActions=new ArrayList<>();
    private final List<Supplier<Boolean>> changeChecks=new ArrayList<>();

    protected StepViewSkin(StepView stepView) {
        super(stepView);
        stepView.controlledObjectProperty().addListener((observableValue, step, t1) -> {
            getChildren().clear();
            saveActions.clear();
            changeChecks.clear();
            getChildren().add(build());
        });
        getChildren().add(build());
        stepView.setSaveAction(() -> {
            for (Supplier<Boolean> save : saveActions) {
                if (!save.get()) {
                    return false;
                }
            }
            return true;
        });
        stepView.setChangeCheck(() -> changeChecks.stream().anyMatch(Supplier::get));
    }

    private Step getStep() {
        return getSkinnable().getControlledObject();
    }

    private Node buildParameterNode(ParameterDefinition definition, Translator translator) {
        Class<?> type=definition.type();
        if(String.class.equals(type)) {
            return buildStringNode(definition,translator.translate(definition.name(), Yukon.getTranslator().getLocale()));
        } else if(File.class.equals(type)) {
            return buildFileNode(definition,translator.translate(definition.name(),Yukon.getTranslator().getLocale()));
        }else if(Directory.class.equals(type)) {
            return buildDirectoryNode(definition,translator.translate(definition.name(),Yukon.getTranslator().getLocale()));
        }
        return null;
    }

    private <T> void saveParameterValue(ParameterDefinition definition,T value) {
        StepParameter<?> parameter=getStep().getParameter().stream().filter((sp)->sp.getName().equals(definition.name())).findFirst().orElse(null);
        if(parameter==null) {
            parameter=new StepParameter<>(definition.name(),definition.type());
            getStep().getParameter().add(parameter);
        }
        StepParameter<T> valParameter= (StepParameter<T>) parameter;
        valParameter.setValue(value);
    }

    private Node buildDirectoryNode(ParameterDefinition definition,String text) {
        VBox box=new VBox(5);
        box.setPadding(new Insets(5));
        box.setFillWidth(true);
        Label l=new Label(text);
        HBox fileBox=new HBox(5);
        TextField field=new TextField();
        for(StepParameter<?> parameter:getStep().getParameter()) {
            if(parameter.getName().equals(definition.name())) {
                StepParameter<Directory> fileParameter= (StepParameter<Directory>) parameter;
                field.setText(fileParameter.getValue()==null?"":fileParameter.getValue().getDirectory().getAbsolutePath());

                break;
            }
        }
        field.setUserData(field.getText());
        saveActions.add(() -> {
            saveParameterValue(definition, new Directory(new File(field.getText())));
            return true;
        });
        changeChecks.add(() -> !field.getText().equals(String.valueOf(field.getUserData())));
        HBox.setHgrow(field, Priority.ALWAYS);
        Button b=new Button();
        b.textProperty().bind(Yukon.getTranslator().translate("button.select"));
        b.setOnAction(actionEvent -> {
            String current = field.getText();
            File f = current.isBlank() ? null : new File(current);
            boolean setDirectory = f != null && (f.exists() || f.getParentFile() != null && f.getParentFile().exists());
            DirectoryChooser chooser = new DirectoryChooser();
            if (setDirectory) {
                chooser.setInitialDirectory(f.getParentFile());
            }
            File selected = chooser.showDialog(Yukon.getYukonWindow());
            if (selected != null) {
                field.setText(selected.getAbsolutePath());
            }
        });
        fileBox.getChildren().addAll(field,b);
        box.getChildren().addAll(l,fileBox);
        return box;
    }

    private Node buildFileNode(ParameterDefinition definition,String text) {
        VBox box=new VBox(5);
        box.setPadding(new Insets(5));
        box.setFillWidth(true);
        Label l=new Label(text);
        HBox fileBox=new HBox(5);
        TextField field=new TextField();
        for(StepParameter<?> parameter:getStep().getParameter()) {
            if(parameter.getName().equals(definition.name())) {
                StepParameter<File> fileParameter= (StepParameter<File>) parameter;
                field.setText(fileParameter.getValue()==null?"":fileParameter.getValue().getAbsolutePath());

                break;
            }
        }
        field.setUserData(field.getText());
        saveActions.add(() -> {
            saveParameterValue(definition, new File(field.getText()));
            return true;
        });
        changeChecks.add(() -> !field.getText().equals(String.valueOf(field.getUserData())));
        HBox.setHgrow(field, Priority.ALWAYS);
        Button b=new Button("Select");
        b.setOnAction(actionEvent -> {
            String current = field.getText();
            File f = current.isBlank() ? null : new File(current);
            boolean setDirectory = f != null && (f.exists() || f.getParentFile() != null && f.getParentFile().exists());
            FileChooser chooser = new FileChooser();
            if (setDirectory) {
                chooser.setInitialDirectory(f.getParentFile());
                chooser.setInitialFileName(f.getName());
            }
            File selected = chooser.showOpenDialog(Yukon.getYukonWindow());
            if (selected != null) {
                field.setText(selected.getAbsolutePath());
            }
        });
        fileBox.getChildren().addAll(field,b);
        box.getChildren().addAll(l,fileBox);
        return box;
    }

    private Node buildStringNode(ParameterDefinition definition,String text) {
        VBox box=new VBox(5);
        box.setPadding(new Insets(5));
        box.setFillWidth(true);
        Label l=new Label(text);
        TextField field=new TextField();
        for(StepParameter<?> parameter:getStep().getParameter()) {
            if(parameter.getName().equals(definition.name())) {
                StepParameter<String> stringParameter= (StepParameter<String>) parameter;
                field.setText(String.valueOf(parameter.getValue()));
                break;
            }
        }
        field.setUserData(field.getText());
        saveActions.add(() -> {
            saveParameterValue(definition, field.getText());
            return true;
        });
        changeChecks.add(() -> !field.getText().equals(String.valueOf(field.getUserData())));
        box.getChildren().addAll(l,field);
        return box;
    }

    private Node build() {
        VBox box=new VBox();
        box.setFillWidth(true);
        if(getSkinnable().getControlledObject()!=null) {
            StepDefinition definition=getSkinnable().getControlledObject().getClass().getAnnotation(StepDefinition.class);
            if(definition!=null) {
                ParameterDefinition[] parameter =definition.parameter();
                if(parameter.length==0) {
                    box.setAlignment(Pos.CENTER);
                    box.getChildren().add(new Label("No Parameter available"));
                }else {
                    Translator translator= YukonTranslationHelper.getTranslator(getSkinnable().getControlledObject());
                    for(ParameterDefinition def:parameter) {
                        box.getChildren().add(buildParameterNode(def,translator));
                    }
                }
            }
        }
        return box;
    }
}
