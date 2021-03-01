package de.clayntech.klondike.sdk.param;

public class StepParameter<T> {
    private final String name;
    private final Class<T> typeClass;
    private boolean optional=true;
    private T value;

    public StepParameter(String name, Class<T> typeClass) {
        this.name=name;
        this.typeClass = typeClass;
    }

    public Class<T> getTypeClass() {
        return typeClass;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
