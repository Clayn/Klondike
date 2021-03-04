package de.clayntech.yukon.bridge;

import de.clayntech.klondike.log.KlondikeLoggerFactory;
import de.clayntech.klondike.sdk.ApplicationRepository;
import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.util.ApplicationFormatter;
import de.clayntech.klondike.sdk.util.Formatter;
import org.slf4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class YukonRepository implements ApplicationRepository {
    private final Logger LOG= KlondikeLoggerFactory.getLogger();
    private final Formatter<KlondikeApplication> formatter=new ApplicationFormatter();

    public YukonRepository() {

    }

    @Override
    public List<KlondikeApplication> getApplications() {
        String in=null;
        List<KlondikeApplication> applications=new ArrayList<>();
        List<String> candidates=new ArrayList<>();
        for(String candidate:KlondikeBridge.callKlondike("list")) {
            if(candidate.contains("\t-\t")) {
                candidates.add(candidate.substring(0,candidate.indexOf("\t-\t")));
            }
        }
        for(String candidate:candidates) {
            for(String app:KlondikeBridge.callKlondike("get",candidate)) {
                applications.add(formatter.fromString(app));
            }
        }
        return applications;
    }

    @Override
    public void register(KlondikeApplication app) throws IOException {

    }

    @Override
    public void update(KlondikeApplication app) throws IOException {

    }
}
