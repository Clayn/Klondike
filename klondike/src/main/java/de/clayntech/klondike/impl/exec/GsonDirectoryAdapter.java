package de.clayntech.klondike.impl.exec;

import com.google.gson.*;
import de.clayntech.klondike.sdk.param.types.Directory;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GsonDirectoryAdapter implements JsonDeserializer<Directory>, JsonSerializer<Directory> {
    final boolean simple=true;
    @Override
    public Directory deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return new Directory(new File(jsonElement.getAsString()));

    }

    @Override
    public JsonElement serialize(Directory dir, Type type, JsonSerializationContext jsonSerializationContext) {
            return jsonSerializationContext.serialize(dir.getDirectory().getAbsolutePath());


    }
}
