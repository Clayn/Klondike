package de.clayntech.klondike.cli;

import java.util.Scanner;

public interface Command {
    @SuppressWarnings("SameReturnValue")
    String perform(Scanner input, String... args) throws Exception;
}
