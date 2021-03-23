package de.clayntech.klondike.cli;

public class InvalidParameterException extends RuntimeException{
    private final String commandName;
    private final int argumentCount;
    private final String[] arguments;


    public InvalidParameterException(String commandName, int argumentCount, String... arguments) {
        this.commandName = commandName;
        this.argumentCount = argumentCount;
        this.arguments = arguments==null?new String[]{}:arguments;
    }

    @Override
    public String getMessage() {
       StringBuilder builder=new StringBuilder();
       builder.append(String.format("Argument '%s' needs at least '%d' arguments. Usage: '%s",commandName,argumentCount,commandName));
       for(String arg:arguments) {
           builder.append(' ').append(arg);
       }
       builder.append('\'');
       return builder.toString();
    }
}
