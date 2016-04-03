/*
 * Created on 27 oct. 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.designer;

import java.io.File;

import madkit.TreeTools.*;
import madkit.kernel.*;
import javax.swing.*;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PropertyFileNode extends EditableFileNode {

	/**
	 * @param ag
	 * @param file
	 * @param desc
	 * @param iconSize
	 * @param iconPanel
	 */
	

		static AgentNodeDescriptor propertyFileDescriptor =
			new AgentNodeDescriptor("/images/agents/AgentIconColor16.gif",
				new String[][]{ // commands
						{"launch","execute"}
					});

	   public ImageIcon getLeafIcon(){ return propertyFileDescriptor.getImage();}
   
	   public PropertyFileNode(AbstractAgent ag, File file){
		   super(ag, file);
			this.setDescriptor(propertyFileDescriptor);
	   }


		public void execute(){
			JOptionPane.showMessageDialog(null, "MadKit Agents", "He you...", 
					JOptionPane.INFORMATION_MESSAGE);
		}


}
