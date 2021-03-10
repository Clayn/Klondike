package de.clayntech.klondike.sdk.param;

@SuppressWarnings("RedundantThrows")
public interface TypeConverter<T> {
    T fromString(String val) throws Exception;

    String toString(T val) throws Exception;
}
