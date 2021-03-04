package de.clayntech.yukon.ui;


public enum YukonImage {
    LOGO("/images/yukon.png");

    private final String path;

    YukonImage(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }


}
