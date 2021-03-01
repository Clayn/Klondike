package de.clayntech.klondike.impl.param;

import de.clayntech.klondike.sdk.param.SharedParameterList;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ParameterListImpl implements SharedParameterList {
    protected final Map<String,Object> parameters=new HashMap<>();

    @Override
    public boolean contains(String parameter) {
        return parameters.containsKey(parameter);
    }

    @Override
    public boolean contains(String parameter, Class<?> type) {
        return parameters.containsKey(parameter)&&type.isAssignableFrom(parameters.get(parameter).getClass());
    }

    @Override
    public <T> T get(String parameter, T def) {
        if(!contains(parameter,def.getClass())) {
            return def;
        }
        return (T) parameters.getOrDefault(parameter,def);
    }

    @Override
    public <T> T get(String parameter, Class<T> type) {
        if(!contains(parameter,type)) {
            return null;
        }
        return (T) parameters.get(parameter);
    }

    @Override
    public <T> T get(String parameter) {
        return (T) parameters.get(parameter);
    }

    @Override
    public void set(String parameter, Object value) {
        parameters.put(parameter,value);
    }
}
