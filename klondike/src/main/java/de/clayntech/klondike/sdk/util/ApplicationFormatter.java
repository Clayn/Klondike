package de.clayntech.klondike.sdk.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.clayntech.klondike.impl.KlondikeApplicationImpl;
import de.clayntech.klondike.impl.exec.*;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;
import de.clayntech.klondike.sdk.param.types.Directory;

import java.io.File;
import java.nio.file.Path;

public class ApplicationFormatter implements Formatter<KlondikeApplication> {

    @SuppressWarnings("rawtypes")
    private final Gson gson=new GsonBuilder()
                .registerTypeAdapter(new TypeToken<StepParameter>(){}.getType(),new GsonStepParameterAdapter())
            .registerTypeAdapter(new TypeToken<Step>(){}.getType(),new GsonStepAdapter())
            .registerTypeAdapter(new TypeToken<File>(){}.getType(),new GsonFileAdapter())
            .registerTypeAdapter(new TypeToken<Path>(){}.getType(),new GsonPathAdapter())
            .registerTypeAdapter(new TypeToken<Directory>(){}.getType(),new GsonDirectoryAdapter())
            .disableHtmlEscaping()
                .create();

    @Override
    public String toString(KlondikeApplication val) {
        return gson.toJson(val);
    }

    @Override
    public KlondikeApplication fromString(String str) {
        return gson.fromJson(str, KlondikeApplicationImpl.class);
    }
}
