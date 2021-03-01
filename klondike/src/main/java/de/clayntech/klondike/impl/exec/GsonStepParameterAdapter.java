package de.clayntech.klondike.impl.exec;

import com.google.gson.*;
import de.clayntech.klondike.sdk.param.StepParameter;

import java.lang.reflect.Type;

@SuppressWarnings("rawtypes")
public class GsonStepParameterAdapter implements JsonDeserializer<StepParameter>, JsonSerializer<StepParameter> {

    @Override
    public StepParameter<?> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj=jsonElement.getAsJsonObject();
        String className=obj.get("type").getAsString();
        String name=obj.get("name").getAsString();
        try {
            Class<?> typeClass=Class.forName(className);
            StepParameter<?> param=new StepParameter<>(name,typeClass);
            param.setValue(jsonDeserializationContext.deserialize(obj.get("value"),typeClass));
            param.setOptional(!obj.has("optional") || obj.get("optional").getAsBoolean());
            return param;
        } catch (ClassNotFoundException e) {
            throw new JsonParseException("Unknown type parameter: "+className,e);
        }
    }

    @Override
    public JsonElement serialize(StepParameter stepParameter, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj=new JsonObject();
        obj.addProperty("type",stepParameter.getTypeClass().getName());
        obj.addProperty("name",stepParameter.getName());
        obj.add("value",jsonSerializationContext.serialize(stepParameter.getValue()));
        obj.addProperty("optional",stepParameter.isOptional());
        return obj;
    }
}
