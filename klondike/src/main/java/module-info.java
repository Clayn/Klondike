module klondike {
    exports de.clayntech.klondike.sdk;
    exports de.clayntech.klondike.sdk.param;
    exports de.clayntech.klondike.sdk.exec;
    exports de.clayntech.klondike.sdk.i18n;
    exports de.clayntech.klondike;
    exports de.clayntech.klondike.util;
    exports de.clayntech.klondike.sdk.os;
    requires org.slf4j;
    requires com.google.gson;
    requires java.desktop;
    requires info.picocli;
    opens de.clayntech.klondike.impl to com.google.gson;
    opens de.clayntech.klondike.impl.exec to com.google.gson;
    opens de.clayntech.klondike.sdk.exec to com.google.gson;
    opens de.clayntech.klondike to info.picocli;
    opens de.clayntech.klondike.cli to info.picocli;
    opens de.clayntech.klondike.util to info.picocli;
    exports de.clayntech.klondike.sdk.util;
    exports de.clayntech.klondike.log;
    exports de.clayntech.klondike.impl.i18n to yukon;
    exports de.clayntech.klondike.impl to yukon;
    exports de.clayntech.klondike.sdk.param.types;
}