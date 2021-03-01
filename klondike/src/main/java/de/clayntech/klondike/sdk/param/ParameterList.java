package de.clayntech.klondike.sdk.param;

public interface ParameterList {

    boolean contains(String parameter);

    boolean contains(String parameter, Class<?> type);

    <T> T get(String parameter, T def);

    <T> T get(String parameter, Class<T> type);

    <T> T get(String parameter);
}
