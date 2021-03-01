package de.clayntech.klondike;

import de.clayntech.klondike.cli.KlondikeCli;
import de.clayntech.klondike.log.KlondikeLoggerFactory;
import org.slf4j.Logger;

public class Klondike  {
    private static final Logger LOG= KlondikeLoggerFactory.getLogger();

    public static void main(String[] args) throws Exception {
        new KlondikeCli().start();
    }
}
