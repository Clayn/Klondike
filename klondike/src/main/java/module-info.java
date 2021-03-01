module klondike {
    exports de.clayntech.klondike.sdk;
    exports de.clayntech.klondike.sdk.param;
    exports de.clayntech.klondike.sdk.exec;
    exports de.clayntech.klondike.sdk.i18n;
    requires org.slf4j;
    requires com.google.gson;
    requires java.desktop;
    opens de.clayntech.klondike.impl to com.google.gson;
    opens de.clayntech.klondike.impl.exec to com.google.gson;
    opens de.clayntech.klondike.sdk.exec to com.google.gson;
}