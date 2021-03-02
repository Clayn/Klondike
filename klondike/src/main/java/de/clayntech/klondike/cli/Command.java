package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;

import java.util.Scanner;

public interface Command {
    @SuppressWarnings("SameReturnValue")
    String perform(Klondike klondike, Scanner input, String... args) throws Exception;
}
