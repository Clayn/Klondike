package de.clayntech.klondike.cli;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.sdk.ApplicationRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.Mockito;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Scanner;

public abstract class CommandTest {
    @TempDir
    File directory;

    protected ApplicationRepository repository;
    protected Klondike klondike;
    @SuppressWarnings("CanBeFinal")
    protected Scanner scanner=null;

    @BeforeEach
    public void setup() throws URISyntaxException {
        File pseudoFile = new File(getClass().getResource("/pseudo.txt").toURI());
        Assertions.assertNotNull(pseudoFile);
        Assertions.assertTrue(pseudoFile.exists());
        repository=Mockito.mock(ApplicationRepository.class);
        Mockito.when(repository.getApplication(Mockito.anyString())).thenCallRealMethod();
        klondike= Mockito.mock(Klondike.class);
        Mockito.when(klondike.getRepository()).thenReturn(repository);
        setup0();
    }

    protected abstract void setup0();
}
