package de.clayntech.klondike.impl;

import com.google.gson.*;
import de.clayntech.klondike.impl.exec.ExecutionScriptImpl;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.ExecutionScript;

import java.io.File;
import java.lang.reflect.Type;

public class GsonKlondikeApplicationAdapter implements JsonDeserializer<KlondikeApplication>, JsonSerializer<KlondikeApplication> {
    @Override
    public KlondikeApplication deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject object=jsonElement.getAsJsonObject();

        String name=object.get("name").getAsString();
        File exe=jsonDeserializationContext.deserialize(object.get("executable"),File.class);
        ExecutionScript script=jsonDeserializationContext.deserialize(object.get("script"), ExecutionScriptImpl.class);
        KlondikeApplication app=new KlondikeApplicationImpl(name,exe);
        app.getScript().getSteps().addAll(script.getSteps());
        return app;
    }

    @Override
    public JsonElement serialize(KlondikeApplication klondikeApplication, Type type, JsonSerializationContext jsonSerializationContext) {
        String name=klondikeApplication.getName();
        File exe=klondikeApplication.getExecutable();
        ExecutionScript script= klondikeApplication.getScript();
        JsonObject obj=new JsonObject();
        obj.addProperty("name",name);
        obj.add("executable",jsonSerializationContext.serialize(exe));
        obj.add("script",jsonSerializationContext.serialize(script));
        return obj;
    }
}
