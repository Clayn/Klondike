package de.clayntech.klondike.impl.exec;

import com.google.gson.*;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GsonFileAdapter implements JsonDeserializer<File>, JsonSerializer<File> {
    final boolean simple=true;
    @Override
    public File deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if(simple) {
            return new File(jsonElement.getAsString());
        }
        JsonObject root=jsonElement.getAsJsonObject();
        List<String> parts=new ArrayList<>();
        while(root!=null) {
            parts.add(root.get("path").getAsString());
            root=root.has("next")?root.getAsJsonObject("next"):null;
        }
        StringBuilder builder=new StringBuilder();
        for(int i=0;i<parts.size()-1;++i) {
            builder.append(parts.get(i)).append(File.pathSeparator);
        }
        builder.append(parts.size()-1);
        return new File(builder.toString());
    }

    @Override
    public JsonElement serialize(File file, Type type, JsonSerializationContext jsonSerializationContext) {
        if(simple) {
            return jsonSerializationContext.serialize(file.getAbsolutePath());
        }
        System.out.println("Serialize file. "+File.pathSeparator+" - "+File.separator+"- "+file.getAbsolutePath());
        String path=file.getAbsolutePath();
        String[] parts=path.split(File.pathSeparator);
        System.out.println("Serialize: "+ Arrays.toString(parts));
        JsonObject root=new JsonObject();
        root.addProperty("path",parts[0]);
        JsonObject current=root;
        for(int i=1;i<parts.length;++i) {
            JsonObject obj=new JsonObject();
            obj.addProperty("path",parts[i]);
            current.add("next",obj);
            current=obj;
        }
        return root;
    }
}
