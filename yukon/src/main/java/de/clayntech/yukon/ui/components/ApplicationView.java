package de.clayntech.yukon.ui.components;

import de.clayntech.klondike.Klondike;
import de.clayntech.klondike.sdk.KlondikeApplication;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;

public class ApplicationView extends Control {

    private final ObjectProperty<KlondikeApplication> application=new SimpleObjectProperty<>();
    private final DoubleProperty viewWidth=new SimpleDoubleProperty(64);
    private final DoubleProperty viewHeight=new SimpleDoubleProperty(64);
    private final ObjectProperty<EventHandler<ActionEvent>> onAction=new SimpleObjectProperty<>();


    public EventHandler<ActionEvent> getOnAction() {
        return onAction.get();
    }

    public ObjectProperty<EventHandler<ActionEvent>> onActionProperty() {
        return onAction;
    }

    public void setOnAction(EventHandler<ActionEvent> onAction) {
        this.onAction.set(onAction);
    }

    public ObjectProperty<KlondikeApplication> applicationProperty() {
        return application;
    }

    public KlondikeApplication getApplication() {
        return applicationProperty().get();
    }

    public void setViewWidth(double viewWidth) {
        this.viewWidth.set(viewWidth);
    }

    public void setViewHeight(double viewHeight) {
        this.viewHeight.set(viewHeight);
    }

    public double getViewWidth() {
        return viewWidth.get();
    }

    public DoubleProperty viewWidthProperty() {
        return viewWidth;
    }

    public double getViewHeight() {
        return viewHeight.get();
    }

    public DoubleProperty viewHeightProperty() {
        return viewHeight;
    }

    public void setApplication(KlondikeApplication app) {
        applicationProperty().set(app);
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        return new ApplicationViewSkin(this);
    }
}
