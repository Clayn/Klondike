package de.clayntech.klondike.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import de.clayntech.klondike.impl.exec.*;
import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.exec.Step;
import de.clayntech.klondike.sdk.param.StepParameter;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class KlondikeApplicationRepository implements ApplicationRepository {

    private static final Logger LOG= KlondikeLoggerFactory.getLogger();
    private final Path directory;
    private final List<KlondikeApplication> applications=new ArrayList<>();

    public KlondikeApplicationRepository(Path directory) throws IOException {
        Objects.requireNonNull(directory);
        if(!Files.isDirectory(directory)) {
            throw new IllegalArgumentException();
        }
        if(!Files.exists(directory)) {
            Files.createDirectories(directory);
        }
        this.directory = directory;
        loadApplications();
    }

    private void loadApplications() throws IOException {
        applications.clear();
        PathMatcher matcher = FileSystems.getDefault().getPathMatcher("glob:**.kapp");
        applications.addAll(Files.list(directory)
                .filter(Files::isRegularFile)
                .filter(matcher::matches).map(this::parseApp).collect(Collectors.toList()));

    }

    private KlondikeApplication parseApp(Path p) {
        Gson gson=new GsonBuilder()
                .registerTypeAdapter(new TypeToken<StepParameter>(){}.getType(),new GsonStepParameterAdapter())
                .registerTypeAdapter(new TypeToken<Step>(){}.getType(),new GsonStepAdapter())
                .registerTypeAdapter(new TypeToken<File>(){}.getType(),new GsonFileAdapter())
                .registerTypeAdapter(new TypeToken<Path>(){}.getType(),new GsonPathAdapter())
                .disableHtmlEscaping()
                .create();

        try {
            return gson.fromJson(readJson(p),KlondikeApplicationImpl.class);
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
        return applications;
    }

    @Override
    public void register(KlondikeApplication app) throws IOException {
        boolean launchFound=false;
        for(KlondikeApplication existing:getApplications()) {
            if(existing.getName().equals(app.getName())) {
                throw new IllegalArgumentException();
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
            app.getScript().getSteps().add(launchStep);
        }
        try(BufferedWriter writer=Files.newBufferedWriter(directory.resolve(app.getName()+".kapp"))) {
            Gson gson=new GsonBuilder()
                    .registerTypeAdapter(new TypeToken<StepParameter>(){}.getType(),new GsonStepParameterAdapter())
                    .registerTypeAdapter(new TypeToken<Step>(){}.getType(),new GsonStepAdapter())
                    .registerTypeAdapter(new TypeToken<File>(){}.getType(),new GsonFileAdapter())
                    .registerTypeAdapter(new TypeToken<Path>(){}.getType(),new GsonPathAdapter())
                    .disableHtmlEscaping()
                    .create();
            writer.write(gson.toJson(app));
            writer.flush();
        }
    }
}
