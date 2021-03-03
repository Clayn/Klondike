package de.clayntech.klondike.sdk.param;

import de.clayntech.klondike.impl.param.KlondikeConverter;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class TypeConverterFactory {
    private static final TypeConverterFactory factory=new TypeConverterFactory();

    private final Map<Class<?>,TypeConverter<?>> converter=new HashMap<>();

    {
        register(new KlondikeConverter());
    }



    @SuppressWarnings("UnusedReturnValue")
    public TypeConverterFactory register(Object converter) {
        List<Method> converterMethods= Arrays.stream(converter.getClass().getDeclaredMethods())
                .filter((m)->m.isAnnotationPresent(Converter.class))
                .collect(Collectors.toList());
        for(Method m:converterMethods) {
            if(m.getParameterCount()!=1&&!m.getParameterTypes()[0].equals(String.class)) {
                continue;
            }
            Class<?> type=m.getReturnType();
            if(this.converter.containsKey(type)) {
                continue;
            }
            TypeConverter<?> conv= (TypeConverter<Object>) val -> m.invoke(converter, val);
            this.converter.put(type,conv);
        }
        return this;
    }


    public static TypeConverterFactory getFactory() {
        return factory;
    }

    public <T> TypeConverter<T> getConverter(Class<T> type) {
        return (TypeConverter<T>) converter.get(type);
    }

}
