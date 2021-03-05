package de.clayntech.klondike.impl.param;

import de.clayntech.klondike.sdk.param.Converter;
import de.clayntech.klondike.sdk.param.TypeConverter;
import de.clayntech.klondike.sdk.param.types.Directory;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class KlondikeConverter {


    public String parseStringRaw(String str) {
        return str;
    }

    //@Converter(Integer.class)
    public Integer parseStringInteger(String str) {
        return parseStringInt(str);
    }

    //@Converter(int.class)
    public int parseStringInt(String str) {
        return Integer.parseInt(str);
    }


    public File parseStringFile(String str) {
        return new File(str);
    }

    //@Converter(Path.class)
    public Path parseStringPath(String str) {
        return Path.of(str);
    }

    @Converter(String.class)
    private static class StringConverter implements TypeConverter<String> {

        @Override
        public String fromString(String val) {
            return val;
        }

        @Override
        public String toString(String val) {
            return val;
        }
    }
    @Converter(File.class)
    private static class FileConverter implements TypeConverter<File> {

        @Override
        public File fromString(String val) {
            return new File(val);
        }

        @Override
        public String toString(File val) {
            return val.getAbsolutePath();
        }
    }

    @Converter(Directory.class)
    private static class DirectoryConverter implements TypeConverter<Directory> {

        @Override
        public Directory fromString(String val) {
            return new Directory(new File(val));
        }

        @Override
        public String toString(Directory val) {
            return val.getDirectory().getAbsolutePath();
        }
    }

    public static List<Object> getConverterClasses() {
        return Arrays.asList(new StringConverter(),new FileConverter(),new DirectoryConverter());
    }
}
