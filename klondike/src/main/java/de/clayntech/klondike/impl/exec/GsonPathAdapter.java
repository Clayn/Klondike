package de.clayntech.klondike.impl.exec;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.nio.file.Path;

public class GsonPathAdapter implements JsonDeserializer<Path>, JsonSerializer<Path> {
    @Override
    public Path deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return Path.of(jsonElement.getAsString());
    }

    @Override
    public JsonElement serialize(Path file, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(file.toFile().getAbsolutePath());
    }
}
