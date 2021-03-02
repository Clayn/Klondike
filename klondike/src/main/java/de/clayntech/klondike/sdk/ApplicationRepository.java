package de.clayntech.klondike.sdk;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ApplicationRepository {
    List<KlondikeApplication> getApplications();

    void register(KlondikeApplication app) throws IOException;

    default void configure(Map<String,String> parameter) {}
}
