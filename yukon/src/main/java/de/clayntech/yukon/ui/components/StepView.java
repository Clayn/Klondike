package de.clayntech.yukon.ui.components;


import de.clayntech.klondike.sdk.exec.Step;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Skin;

import java.util.function.Supplier;

public class StepView extends EditControl<Step>{

    private final ObjectProperty<Supplier<Boolean>> saveAction=new SimpleObjectProperty<>();
    private final ObjectProperty<Supplier<Boolean>> changeCheck=new SimpleObjectProperty<>();

    Supplier<Boolean> getChangeCheck() {
        return changeCheck.get();
    }

    ObjectProperty<Supplier<Boolean>> changeCheckProperty() {
        return changeCheck;
    }

    void setChangeCheck(Supplier<Boolean> changeCheck) {
        this.changeCheck.set(changeCheck);
    }

    Supplier<Boolean> getSaveAction() {
        return saveAction.get();
    }

    ObjectProperty<Supplier<Boolean>> saveActionProperty() {
        return saveAction;
    }

    void setSaveAction(Supplier<Boolean> saveAction) {
        this.saveAction.set(saveAction);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new StepViewSkin(this);
    }

    @Override
    public boolean isChanged() {
        return getChangeCheck()==null|| getChangeCheck().get();
    }

    @Override
    public boolean save() {
        return getSaveAction() == null || getSaveAction().get();
    }
}
