package de.clayntech.yukon.ui.components;

import de.clayntech.klondike.sdk.KlondikeApplication;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.SkinBase;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.embed.swing.SwingFXUtils;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

public class ApplicationViewSkin extends SkinBase<ApplicationView> {
    static Map<String, Image> mapOfFileExtToSmallIcon = new HashMap<>();

    protected ApplicationViewSkin(ApplicationView applicationView) {
        super(applicationView);
        ImageView img=new ImageView();
        img.fitHeightProperty().bind(applicationView.viewHeightProperty());
        img.fitWidthProperty().bind(applicationView.viewWidthProperty());
        img.imageProperty().bind(Bindings.createObjectBinding(new Callable<Image>() {
            @Override
            public Image call() throws Exception {
                if(applicationView.getApplication()==null) {
                    return null;
                }
                return loadImage(applicationView.getApplication().getExecutable());
            }
        },applicationView.applicationProperty()));
        Button b=new Button();
        b.tooltipProperty().bind(Bindings.createObjectBinding(new Callable<Tooltip>() {
            @Override
            public Tooltip call() throws Exception {
                KlondikeApplication app= applicationView.getApplication();;
                if(app==null) {
                    return null;
                }
                Tooltip tip=new Tooltip();
                tip.setText(app.getName());
                return tip;
            }
        },applicationView.applicationProperty()));
        b.setGraphic(img);
        b.onActionProperty().bind(applicationView.onActionProperty());
        b.getStyleClass().add("application");
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
        FileSystemView view = FileSystemView.getFileSystemView();
        javax.swing.Icon icon = view.getSystemIcon(file);
        // }

        // OS X {
        //final javax.swing.JFileChooser fc = new javax.swing.JFileChooser();
        //javax.swing.Icon icon = fc.getUI().getFileView(fc).getIcon(file);
        // }

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
