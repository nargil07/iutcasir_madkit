/*
 * Created on Mar 20, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package madkit.MadChat;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.filechooser.*;

/*
 * FileChooserDemo2.java is a 1.4 application that requires these files:
 *   ImageFileView.java
 *   ImageFilter.java
 *   ImagePreview.java
 *   Utils.java
 *   images/jpgIcon.gif (required by ImageFileView.java)
 *   images/gifIcon.gif (required by ImageFileView.java)
 *   images/tiffIcon.gif (required by ImageFileView.java)
 *   images/pngIcon.png (required by ImageFileView.java)
 */
public class FileChooserGUI extends JFrame {
    static private String newline = "\n";
    //private JTextArea log;
    private JFileChooser fc;
    private String backgroundPath;
    private Config config;

    public FileChooserGUI(Config cfg) {
    	config=cfg;
    	fc = new JFileChooser();

	    //Add a custom file filter and disable the default
	    //(Accept All) file filter.
            fc.addChoosableFileFilter(new ImageFilter());
            fc.setAcceptAllFileFilterUsed(false);

	    //Add custom icons for file types.
            fc.setFileView(new ImageFileView(config));

	    //Add the preview pane.
            fc.setAccessory(new ImagePreview(fc));

        //Show it.
        int returnVal = fc.showDialog(FileChooserGUI.this,"Appliquer fond d'ecran");

        //Process the results.
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            backgroundPath=file.getPath();
        } else {
        	//dispose();
            //log.append("Attachment cancelled by user." + newline);
        }
        
        //Reset the file chooser for the next time it's shown.
        fc.setSelectedFile(null);
        //dispose();
    }
    
    public String getBackgroundPath(){
    	return backgroundPath;
    }
}
