package madkit.MadChat;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

public class ImageFileView extends FileView {
	private Config config;
	ImageIcon jpgIcon;
	ImageIcon gifIcon;
	ImageIcon tiffIcon;
	ImageIcon pngIcon;
	
	public ImageFileView(Config cfg){
		config=cfg;
	    jpgIcon = Utils.createImageIcon(config.getIconsPath()+"jpgIcon.gif");
	    gifIcon = Utils.createImageIcon(config.getIconsPath()+"gifIcon.gif");
	    tiffIcon = Utils.createImageIcon(config.getIconsPath()+"tiffIcon.gif");
	    pngIcon = Utils.createImageIcon(config.getIconsPath()+"pngIcon.png");
	}
	
    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        String type = null;

        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)) {
                type = "JPEG Image";
            } else if (extension.equals(Utils.gif)){
                type = "GIF Image";
            } else if (extension.equals(Utils.tiff) ||
                       extension.equals(Utils.tif)) {
                type = "TIFF Image";
            } else if (extension.equals(Utils.png)){
                type = "PNG Image";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        String extension = Utils.getExtension(f);
        Icon icon = null;

        if (extension != null) {
            if (extension.equals(Utils.jpeg) ||
                extension.equals(Utils.jpg)) {
                icon = jpgIcon;
            } else if (extension.equals(Utils.gif)) {
                icon = gifIcon;
            } else if (extension.equals(Utils.tiff) ||
                       extension.equals(Utils.tif)) {
                icon = tiffIcon;
            } else if (extension.equals(Utils.png)) {
                icon = pngIcon;
            }
        }
        return icon;
    }
}
