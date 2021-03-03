package de.clayntech.klondike;

import de.clayntech.klondike.impl.KlondikeApplicationRepository;
import de.clayntech.klondike.impl.exec.LaunchStep;
import de.clayntech.klondike.impl.exec.LogStep;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class KlondikeTest {

    @TempDir
    File directory;

    private File pseudoFile;

    private ApplicationRepository repository;

    @BeforeEach
    public void setup() throws IOException, URISyntaxException {
        pseudoFile=new File(getClass().getResource("/pseudo.txt").toURI());
        Assertions.assertNotNull(pseudoFile);
        Assertions.assertTrue(pseudoFile.exists());
        repository=new KlondikeApplicationRepository(directory.toPath());
    }

    private void executeKlondike(String... args) {
        int size=2+(args==null?0:args.length);
        String repoDir=directory.getAbsolutePath();
        String repoCommand="-Rklondike.repository.directory="+repoDir;
        List<String> argList=new ArrayList<>(size);
        argList.add("-u");
        argList.add(repoCommand);
        if (size != 2) {
            argList.addAll(Arrays.asList(args));
        }
        Klondike.main(argList.toArray(new String[0]));
    }

    @Test
    public void testAddApplication() {
        String command="add";
        String name="Test";
        String file=pseudoFile.getAbsolutePath();
        executeKlondike(command,name,file);
        Assertions.assertEquals(1, Objects.requireNonNull(directory.list((dir, name1) -> name1.endsWith(".kapp"))).length);
        List<KlondikeApplication> apps=repository.getApplications();
        Assertions.assertEquals(1,apps.size());
        Assertions.assertEquals(name,apps.get(0).getName());
        Assertions.assertTrue(apps.get(0).getScript().getSteps().stream().anyMatch((step)->LaunchStep.class.isAssignableFrom(step.getClass())));
    }

    @Test
    public void testAddStep() {
        testAddApplication();
        String command="add";
        String name="Test";
        int scriptSize=repository.getApplication(name).getScript().getSteps().size();
        executeKlondike("add-step",name, LogStep.class.getName(),"-SP"+LogStep.MESSAGE_PARAMETER+"=Test");
        Assertions.assertEquals(scriptSize+1,repository.getApplication(name).getScript().getSteps().size());
    }
}
