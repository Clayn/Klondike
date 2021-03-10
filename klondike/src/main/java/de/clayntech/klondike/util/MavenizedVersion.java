package de.clayntech.klondike.util;

import de.clayntech.klondike.log.KlondikeLoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class MavenizedVersion implements Version{

    private final Properties appProperties=new Properties();

    protected abstract String getPropertiesFileName();

    private void loadProperties() {
        String resource="/"+getPropertiesFileName();
        KlondikeLoggerFactory.getLogger().debug("Loading resource: {}",resource);
        try(InputStream input=getClass().getResourceAsStream(resource)) {
            appProperties.load(input);
            KlondikeLoggerFactory.getLogger().debug("Done");
        } catch (IOException e) {
            KlondikeLoggerFactory.getLogger().error("Failed to load: "+resource,e);
        }
    }
    @Override
    public String getApplicationVersion() {
        loadProperties();
        return appProperties.getProperty("app.version");
    }
}
