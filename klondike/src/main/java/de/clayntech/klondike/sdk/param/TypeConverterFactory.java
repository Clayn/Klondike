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
        for(Object conv:KlondikeConverter.getConverterClasses()) {
            register(conv);
        }
    }



    @SuppressWarnings("UnusedReturnValue")
    public TypeConverterFactory register(Object converter) {
        Converter conv=converter.getClass().getAnnotation(Converter.class);
        if(conv!=null&&converter instanceof TypeConverter) {
            this.converter.put(conv.value(), (TypeConverter<?>) converter);
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
