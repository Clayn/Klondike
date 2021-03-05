package de.clayntech.klondike.sdk.param;

public interface TypeConverter<T> {
    T fromString(String val) throws Exception;

    String toString(T val) throws Exception;
}
