package de.clayntech.yukon.bridge;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.log.KlondikeLoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KlondikeBridge {

    /**
     * Calls the Klondike application with the given arguments and returns all lines printed by Klondike.
     * @param arguments the arguments for the execution
     * @return the lines that where printed to System.out by Klondike
     */
    public static List<String> callKlondike(String... arguments) {
        PrintStream stream=System.out;
        PipedInputStream pIn=new PipedInputStream();
        PipedOutputStream pOut=new PipedOutputStream();
        BufferedReader reader;

        List<String> back=new ArrayList<>();
        try {
            pIn.connect(pOut);
            System.setOut(new PrintStream(pOut));
            reader=new BufferedReader(new InputStreamReader(pIn));
            String in;
            new Thread(() -> {
                Klondike.main(arguments);
                try {
                    pOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();
            while ((in = reader.readLine()) != null) {
                back.add(in);
            }
            pIn.close();
            return back;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }finally {
            System.setOut(stream);
            KlondikeLoggerFactory.getLogger().debug("Received: {}",back);
        }
    }
}
