package de.clayntech.klondike.sdk.util;

public interface Formatter<T> {
    String toString(T val);

    T fromString(String str);
}
