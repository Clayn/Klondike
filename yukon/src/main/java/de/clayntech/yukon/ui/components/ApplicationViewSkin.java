package de.clayntech.yukon.ui.components;

import de.clayntech.klondike.sdk.KlondikeApplication;
import de.clayntech.klondike.sdk.os.OSHandle;
import de.clayntech.klondike.sdk.os.OSType;
import de.clayntech.yukon.Yukon;
import de.clayntech.yukon.controller.EditApplicationController;
import de.clayntech.yukon.ui.FXMLFile;
import de.clayntech.yukon.ui.YukonImage;
import de.clayntech.yukon.util.ImageHelper;
import de.clayntech.yukon.util.LoadedFXML;
import de.clayntech.yukon.util.UILoader;
import javafx.beans.binding.Bindings;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.Style;

import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ApplicationViewSkin extends SkinBase<ApplicationView> {
    static final Map<String, Image> mapOfFileExtToSmallIcon = new HashMap<>();

    private final ContextMenu menu=new ContextMenu();

    protected ApplicationViewSkin(ApplicationView applicationView) {
        super(applicationView);
        ImageView img=new ImageView();
        img.fitHeightProperty().bind(applicationView.viewHeightProperty());
        img.fitWidthProperty().bind(applicationView.viewWidthProperty());
        img.imageProperty().bind(Bindings.createObjectBinding(() -> {
            if (applicationView.getApplication() == null) {
                return null;
            }
            return loadImage(applicationView.getApplication().getExecutable());
        },applicationView.applicationProperty()));
        Button b=new Button();
        b.tooltipProperty().bind(Bindings.createObjectBinding(() -> {
            KlondikeApplication app = applicationView.getApplication();
            if (app == null) {
                return null;
            }
            Tooltip tip = new Tooltip();
            tip.setText(app.getName());
            return tip;
        },applicationView.applicationProperty()));
        b.setGraphic(img);
        b.onActionProperty().bind(applicationView.onActionProperty());
        b.getStyleClass().add("application");
        b.contextMenuProperty().bind(Bindings.createObjectBinding((()->applicationView.getApplication()==null?null:menu),applicationView.applicationProperty()));
        MenuItem openEdit=new MenuItem();
        openEdit.textProperty().bind(Yukon.getTranslator().translate("menu.edit"));
        openEdit.setOnAction(actionEvent -> {
            try {
                LoadedFXML loaded = UILoader.prepare(FXMLFile.EDIT_DIALOG)
                        .with(Style.DARK)
                        .with("/style/yukon.css")
                        .load();
                EditApplicationController controller = loaded.getController();
                controller.setOldName(applicationView.getApplication().getName());
                controller.setApplication(applicationView.getApplication());
                Stage st = new Stage();
                st.initOwner(Yukon.getYukonWindow());
                st.initModality(Modality.APPLICATION_MODAL);
                st.setTitle(openEdit.getText()+" - " + applicationView.getApplication().getName());
                st.setScene(loaded.getScene());
                Yukon.getTranslator().applyTranslations(st.getScene());
                st.setOnCloseRequest(windowEvent -> {
                    windowEvent.consume();
                    controller.onCancel();
                });
                ImageHelper.applyImage(st, YukonImage.LOGO);
                st.show();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        menu.getItems().add(openEdit);
        getChildren().add(b);
    }

    private Image loadImage(File f) {
        String fname=f.getAbsolutePath();
        final String ext = getFileExt(fname);

        Image fileIcon = mapOfFileExtToSmallIcon.get(ext);
        if (fileIcon == null) {

            javax.swing.Icon jswingIcon = null;

            File file = new File(fname);
            if (file.exists()) {
                jswingIcon = getJSwingIconFromFileSystem(file);
            }
            else {
                File tempFile = null;
                try {
                    tempFile = File.createTempFile("icon", ext);
                    jswingIcon = getJSwingIconFromFileSystem(tempFile);
                }
                catch (IOException ignored) {
                    // Cannot create temporary file.
                }
                finally {
                    if (tempFile != null) tempFile.delete();
                }
            }

            if (jswingIcon != null) {
                fileIcon = jswingIconToImage(jswingIcon);
                mapOfFileExtToSmallIcon.put(ext, fileIcon);
            }
        }

        return fileIcon;
    }

    private static String getFileExt(String fname) {
        String ext = ".";
        int p = fname.lastIndexOf('.');
        if (p >= 0) {
            ext = fname.substring(p);
        }
        return ext.toLowerCase();
    }

    private static javax.swing.Icon getJSwingIconFromFileSystem(File file) {

        // Windows {
        javax.swing.Icon icon=null;
        if(OSHandle.getHandle().getOSType()== OSType.WINDOWS) {
            FileSystemView view = FileSystemView.getFileSystemView();
            icon= view.getSystemIcon(file);
        }

        if(OSHandle.getHandle().getOSType()==OSType.MAC_OS) {
            final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
            icon = fc.getUI().getFileView(fc).getIcon(file);
        }

        return icon;
    }

    private static Image getFileIcon(String fname) {
        final String ext = getFileExt(fname);

        Image fileIcon = mapOfFileExtToSmallIcon.get(ext);
        if (fileIcon == null) {

            javax.swing.Icon jswingIcon = null;

            File file = new File(fname);
            if (file.exists()) {
                jswingIcon = getJSwingIconFromFileSystem(file);
            }
            else {
                File tempFile = null;
                try {
                    tempFile = File.createTempFile("icon", ext);
                    jswingIcon = getJSwingIconFromFileSystem(tempFile);
                }
                catch (IOException ignored) {
                    // Cannot create temporary file.
                }
                finally {
                    if (tempFile != null) tempFile.delete();
                }
            }

            if (jswingIcon != null) {
                fileIcon = jswingIconToImage(jswingIcon);
                mapOfFileExtToSmallIcon.put(ext, fileIcon);
            }
        }

        return fileIcon;
    }

    private static Image jswingIconToImage(javax.swing.Icon jswingIcon) {
        BufferedImage bufferedImage = new BufferedImage(jswingIcon.getIconWidth(), jswingIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        jswingIcon.paintIcon(null, bufferedImage.getGraphics(), 0, 0);
        return SwingFXUtils.toFXImage(bufferedImage, null);
    }
}
