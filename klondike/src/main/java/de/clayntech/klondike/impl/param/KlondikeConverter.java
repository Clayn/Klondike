package de.clayntech.klondike.impl.param;

import de.clayntech.klondike.sdk.param.Converter;

import java.io.File;
import java.nio.file.Path;

public class KlondikeConverter {

    @Converter(String.class)
    public String parseStringRaw(String str) {
        return str;
    }

    @Converter(Integer.class)
    public Integer parseStringInteger(String str) {
        return parseStringInt(str);
    }

    @Converter(int.class)
    public int parseStringInt(String str) {
        return Integer.parseInt(str);
    }

    @Converter(File.class)
    public File parseStringFile(String str) {
        return new File(str);
    }

    @Converter(Path.class)
    public Path parseStringPath(String str) {
        return Path.of(str);
    }
}
