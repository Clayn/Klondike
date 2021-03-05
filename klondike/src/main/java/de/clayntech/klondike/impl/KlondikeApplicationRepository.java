package de.clayntech.klondike.impl;

import de.clayntech.klondike.impl.exec.LaunchStep;
import de.clayntech.klondike.impl.exec.LogStep;
import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.err.NameInUseException;
import de.clayntech.klondike.sdk.evt.Events;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;
import de.clayntech.klondike.sdk.util.ApplicationFormatter;
import de.clayntech.klondike.sdk.util.Formatter;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class KlondikeApplicationRepository implements ApplicationRepository {

    private static final Logger LOG= KlondikeLoggerFactory.getLogger();
    private final Path directory;
    private final List<KlondikeApplication> applications=new ArrayList<>();
    private final Formatter<KlondikeApplication> formatter=new ApplicationFormatter();

    public KlondikeApplicationRepository(Path directory) throws IOException {
        Objects.requireNonNull(directory);
        this.directory = directory;
        loadApplications();
    }

    private void loadApplications() throws IOException {
        if(Files.exists(directory)&&!Files.isDirectory(directory)) {
            throw new IllegalArgumentException();
        }
        if(!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        applications.clear();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.kapp");
        applications.addAll(Files.list(directory)
                .filter(Files::isRegularFile)
                .filter(matcher::matches).map(this::parseApp).collect(Collectors.toList()));

    }

    private KlondikeApplication parseApp(Path p) {
        try {
            return formatter.fromString(readJson(p));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readJson(Path p) throws IOException {
        StringBuilder builder=new StringBuilder();
        try(BufferedReader reader=Files.newBufferedReader(p)) {
            String line;
            while((line=reader.readLine())!=null) {
                builder.append(line);
            }
        }
        return builder.toString();
    }

    @Override
    public List<KlondikeApplication> getApplications() {
        try {
            loadApplications();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return applications;
    }

    @Override
    public void register(KlondikeApplication app) throws IOException {
        boolean launchFound=false;
        for(KlondikeApplication existing:getApplications()) {
            if(existing.getName().equals(app.getName())) {
                throw new NameInUseException();
            }
        }
        for(Step steps:app.getScript().getSteps()) {
            if(steps instanceof LaunchStep) {
                launchFound=true;
                break;
            }
        }
        if(!launchFound) {
            Step launchStep=new LaunchStep();
            StepParameter<File> workingDir=new StepParameter<>("launch.workingdir",File.class);
            workingDir.setValue(app.getExecutable().getParentFile());
            launchStep.getParameter().add(workingDir);
            Step preLogStep=new LogStep();
            preLogStep.getTrigger().addAll(Arrays.asList(Events.POST_EXECUTION,Events.PRE_EXECUTION));
            app.getScript().getSteps().add(launchStep);
            app.getScript().getSteps().add(preLogStep);
        }
        writeApplicationFile(app);
    }

    private void writeApplicationFile(KlondikeApplication app) throws IOException {
        try(BufferedWriter writer=Files.newBufferedWriter(directory.resolve(app.getName()+".kapp"), StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE)) {
            writer.write(formatter.toString(app));
            writer.flush();
        }
        loadApplications();
    }

    @Override
    public void update(KlondikeApplication app) throws IOException {
        boolean exists=getApplication(app.getName())!=null;
        if(!exists) {
            register(app);
            return;
        }
        writeApplicationFile(app);
    }

    @Override
    public void update(KlondikeApplication app, String newName) throws IOException {
        boolean exists=getApplication(app.getName())!=null;
        boolean nameUsed=getApplication(newName)!=null;
        if(nameUsed) {
            throw new NameInUseException();
        }
        String oldName=app.getName();
        app.setName(newName);
        if(!exists) {
            register(app);
            return;
        }else {
            Files.deleteIfExists(directory.resolve(oldName+".kapp"));
        }
        writeApplicationFile(app);
    }
}
