package de.clayntech.klondike.impl.exec;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.List;

public class GsonStepAdapter implements JsonDeserializer<Step>, JsonSerializer<Step> {

    @Override
    public Step deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject obj= jsonElement.getAsJsonObject();
        String stepClass=obj.get("provider").getAsString();
        List<StepParameter<?>> param=jsonDeserializationContext.deserialize(obj.get("parameter"),new TypeToken<List<StepParameter<?>>>(){}.getType());
        try {
            Step step= (Step) Class.forName(stepClass).getConstructor().newInstance();
            step.getParameter().addAll(param);
            return step;
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException e) {
            throw new JsonParseException(e);
        }
    }

    @Override
    public JsonElement serialize(Step step, Type type, JsonSerializationContext jsonSerializationContext) {
        JsonObject obj=new JsonObject();
        obj.addProperty("provider",step.getClass().getName());
        obj.add("parameter",jsonSerializationContext.serialize(step.getParameter()));
        return obj;
    }
}
