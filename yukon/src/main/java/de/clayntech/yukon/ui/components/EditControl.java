package de.clayntech.yukon.ui.components;

import de.clayntech.yukon.controller.DataController;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Control;

public abstract class EditControl<T> extends Control implements DataController<T> {
    private final ReadOnlyBooleanWrapper changed=new ReadOnlyBooleanWrapper();
    protected final ObjectProperty<T> controlledObject=new SimpleObjectProperty<>();
    public boolean isChanged() {
        return changed.get();
    }

    public ReadOnlyBooleanProperty changedProperty() {
        return changed.getReadOnlyProperty();
    }

    void setChanged(boolean changed) {
        this.changed.set(changed);
    }

    @Override
    public boolean hasChanges() {
        return isChanged();
    }

    @Override
    public T getControlledObject() {
        return controlledObject.get();
    }

    public ObjectProperty<T> controlledObjectProperty() {
        return controlledObject;
    }

    public void setControlledObject(T controlledObject) {
        this.controlledObject.set(controlledObject);
    }
}
