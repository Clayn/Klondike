package de.clayntech.yukon.util;

import de.clayntech.klondike.util.MavenizedVersion;

public class YukonVersion extends MavenizedVersion {
    @Override
    protected String getPropertiesFileName() {
        return "yukon_app.properties";
    }
}
