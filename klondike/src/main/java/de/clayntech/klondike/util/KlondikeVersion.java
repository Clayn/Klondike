package de.clayntech.klondike.util;

import picocli.CommandLine;

import java.io.PrintStream;

public class KlondikeVersion extends MavenizedVersion implements CommandLine.IVersionProvider{

    private static final String VERSION_STRING="  _  __ _                    _  _  _               ___    __  \n" +
            " | |/ /| |                  | |(_)| |             / _ \\  /_ | \n" +
            " | ' / | |  ___   _ __    __| | _ | | __ ___     | | | |  | | \n" +
            " |  <  | | / _ \\ | '_ \\  / _` || || |/ // _ \\    | | | |  | | \n" +
            " | . \\ | || (_) || | | || (_| || ||   <|  __/    | |_| |_ | | \n" +
            " |_|\\_\\|_| \\___/ |_| |_| \\__,_||_||_|\\_\\\\___|     \\___/(_)|_| \n" +
            "                                                              ";

    @Override
    public String[] getVersion() {
        return new String[]{"@|green "+VERSION_STRING+"|@"};
    }

    public static void printHello(PrintStream ps) {
        ps.println(VERSION_STRING);
    }

    public static Version getInstance() {
        return new KlondikeVersion();
    }

    @Override
    protected String getPropertiesFileName() {
        return "klondike_app.properties";
    }
}
