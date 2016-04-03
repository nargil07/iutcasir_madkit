package madkit.explorer;

import java.io.*;
import madkit.TreeTools.*;
import java.awt.*;
import madkit.kernel.*;
import java.net.URL;
import javax.swing.*;
import madkit.utils.graphics.GraphicUtils;;

public class FileIcon extends Icon {

    File file;
    int iconSize;
  
	
    public FileIcon(AbstractAgent ag, File file, GenericIconDescriptor desc, int iconSize, IconPanel iconPanel){
		super(ag, desc, file.getName(), iconSize, iconPanel);
		this.file = file;
		this.setDescriptor(desc);
    	this.iconSize = iconSize;
    }

    public File getFile(){return file;}
    
	protected Image getImage(){
		ImageIcon im = desc.getImage(file);
		if (im == null){
			URL url = this.getClass().getResource("/images/kde/document.png");
			if (url != null){
				im = new ImageIcon(url);
			}
		}
		if (im == null)
			return null;
		else
			return im.getImage();
	}
	
	public void info(){
		JOptionPane.showMessageDialog(iconPanel, 
				"NodeType: "+getClass().getName()+
				"\nFile: "+getFile(), "Properties of "+getName(), 
				JOptionPane.INFORMATION_MESSAGE);
	}

//	fonction rename file    
	 public void rename(){

		 String url = file.getAbsolutePath();
		 String currentPath = file.getParent();
		 String oldName = file.getName();
		 String shortOldName = Utils.getFileNameFromPath(oldName);
        
		 String arg1 ="TO"; 
		 String arg2 ="RENAME FILE "; 
		 String newName  = ExplorerOptionPane.askForName(shortOldName,arg1,arg2);
		 // System.out.println("newName = "+newName);
		 File oldFile = new File(url);
		 File newFile = new File(url);
		
		 if ((newName != null)  && (!newName.equals(shortOldName)))
		 {
 			 iconPanel.renameItem(desc, oldName, newName);
  			 newFile = new File(currentPath + File.separatorChar + newName);
			 oldFile.renameTo(newFile);
  			 file = newFile;
		 }

 		
	  }
    
//	fonction rename file    
	 public void delete(){

		
		String url = file.getAbsolutePath();
		String currentPath = file.getParent();
		String firstMessage = "Do you really want to delete this file ?";
		String secondMessage = "DELETE FILE ";
		int request = ExplorerOptionPane.yesNo(firstMessage, secondMessage);
		if (request == JOptionPane.YES_OPTION)
		{

			File newFile = new File(currentPath);
			boolean delet = file.delete();
			if(delet)
			{		
				String oldName = file.getName();
				iconPanel.renameItem(desc, oldName, null);
				file = newFile;
			}
		}
 	 		
	  }
    
    
    public void execute(){
    	GraphicUtils.execute(file.getAbsolutePath());
    }
}
