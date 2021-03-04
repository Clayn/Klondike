module yukon {
    opens de.clayntech.yukon to javafx.graphics;
    opens de.clayntech.yukon.controller to javafx.graphics,javafx.fxml;
    opens de.clayntech.yukon.util to javafx.fxml;
    requires klondike;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.desktop;
    requires javafx.swing;
    requires org.jfxtras.styles.jmetro;
}