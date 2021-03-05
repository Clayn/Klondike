package de.clayntech.yukon.controller;

public interface DataController<T> {
    boolean hasChanges();

    boolean save();

    T getControlledObject();

    void setControlledObject(T object);

    abstract class BaseDataController<T> implements DataController<T> {

        private T controlledObject;

        @Override
        public T getControlledObject() {
            return controlledObject;
        }

        @Override
        public void setControlledObject(T controlledObject) {
            this.controlledObject = controlledObject;
        }
    }
}
