package de.clayntech.klondike.sdk;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ApplicationRepository {
    List<KlondikeApplication> getApplications();

    void register(KlondikeApplication app) throws IOException;

    void update(KlondikeApplication app) throws IOException;

    default KlondikeApplication getApplication(String name) {
        return getApplications().stream().filter((app)->name.equals(app.getName()))
                .findFirst().orElse(null);
    }

    default void configure(Map<String,String> parameter) {}
}
