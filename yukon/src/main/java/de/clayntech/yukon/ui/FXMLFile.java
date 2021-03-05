package de.clayntech.yukon.ui;

public enum FXMLFile {
    MAIN_WINDOW("MainWindow.fxml"),NEW_DIALOG("NewApplication.fxml"),EDIT_DIALOG("EditApplication.fxml");

    private final String fxml;

    FXMLFile(String s) {
        this.fxml=s;
    }

    public String getFXML() {
        return fxml;
    }
}
