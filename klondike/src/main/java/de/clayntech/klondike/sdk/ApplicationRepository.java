package de.clayntech.klondike.sdk;

import java.io.IOException;
import java.util.List;

public interface ApplicationRepository {
    List<KlondikeApplication> getApplications();

    void register(KlondikeApplication app) throws IOException;
}
