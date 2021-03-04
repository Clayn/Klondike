package de.clayntech.klondike;

import de.clayntech.klondike.cli.KlondikeCli;
import de.clayntech.klondike.impl.KlondikeApplicationRepository;
import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.util.KlondikeVersion;
import org.slf4j.Logger;
import picocli.CommandLine;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

@CommandLine.Command(name = "klondike",mixinStandardHelpOptions = true,versionProvider = KlondikeVersion.class)
public class Klondike implements Callable<Integer> {
    private static final Logger LOG= KlondikeLoggerFactory.getLogger();

    private static CommandLine CLI;

    @CommandLine.Option(names = {"-R"},description = "Parameter for the selected repository")
    private Map<String,String> repoParameter;

    @CommandLine.Option(names = {"-r","--repository"},showDefaultValue = CommandLine.Help.Visibility.ALWAYS,paramLabel = "REPOSITORY",description = "The repository to use. Full qualified class name in case of custom repository",defaultValue = "KLONDIKE")
    private String repositoryName;

    @CommandLine.Option(names = {"-i","--interactive"},description = "Starts the interactive mode")
    private boolean commandLine;

    @CommandLine.Option(names = "-u",hidden = true)
    private boolean testMode;

    @CommandLine.Parameters(description = "Execution parameters. Not used when interactive mode is enabled",paramLabel = "COMMANDS")
    private List<String> parameter;
    private final AtomicInteger state=new AtomicInteger(0);
    private ApplicationRepository repository;

    public static void main(String[] args){
        Klondike klondike = new Klondike();
        CLI=new CommandLine(klondike);
        CLI.setUnmatchedArgumentsAllowed(true);
        int exit=CLI.execute(args);
        if(!klondike.testMode&&exit!=0) {
            System.exit(exit);
        }
    }

    @SuppressWarnings("unchecked")
    public ApplicationRepository getRepository() {
        try {
            if(repository!=null) {
                return repository;
            }
            if(repoParameter==null) {
                repoParameter=new HashMap<>();
            }
            if(repositoryName==null||repositoryName.isBlank()||"KLONDIKE".equals(repositoryName)) {
                File directory=new File(repoParameter.getOrDefault("klondike.repository.directory",System.getProperty("user.dir")));
                repository=new KlondikeApplicationRepository(directory.toPath());
            }
            else {
                Class<? extends ApplicationRepository> repositoryClass= (Class<? extends ApplicationRepository>) Class.forName(repositoryName);
                repository=repositoryClass.getConstructor().newInstance();
            }
            repository.configure(repoParameter);
            return repository;
        }catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public void setState(int state) {
        this.state.set(state);
    }

    @Override
    public Integer call() {
        if(CLI.isUsageHelpRequested()) {
            CLI.usage(System.out);
            return 0;
        }
        if(CLI.isVersionHelpRequested()) {
            CLI.printVersionHelp(System.out);
            return 0;
        }
        if(commandLine) {
            new KlondikeCli(this).start();
            return 0;
        }
        if(parameter.isEmpty()) {
            CLI.usage(System.out);
            return 0;
        }
        KlondikeCli cli=new KlondikeCli(this);
        List<String> cliParameter=parameter.size()==1? Collections.emptyList():parameter.subList(1,parameter.size());
        cli.execute(parameter.get(0),cliParameter.toArray(new String[0]));
        return state.get();
    }
}
