/*
 * Created on 20 avr. 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.designer;

import java.io.File;

import javax.swing.ImageIcon;

import madkit.TreeTools.GenericIconDescriptor;
import madkit.system.PluginInformation;
import madkit.TreeTools.*;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public	class PluginNode extends GenericTreeNode {
		static GenericIconDescriptor pluginNodeDescriptor =
		    new GenericIconDescriptor("/images/plugin.gif", //"/images/agents/AgentIconColor16.gif",
	            new String[][]{ // commands
		    			{"select", "select"},    
						{"properties", "execute"},
	                    {"build","build"},
	                    {"refresh","refresh"},
						//{"make doc","doc"},
						// {"clean","clean"}
						//,{"delete","delete"}
	                });
		
		PluginDesignerGUI gui;
		PluginInformation info;
		
		public PluginNode(String name, PluginInformation _info, PluginDesignerGUI _gui){
			super(name);
			gui = _gui;
			info = _info;
			this.setDescriptor(pluginNodeDescriptor);
		}
		public ImageIcon getLeafIcon(){ return pluginNodeDescriptor.getImage();}
		public ImageIcon getBranchIcon(int num, boolean bool){ return pluginNodeDescriptor.getImage();}
		
	    protected String getName(){
	    	return (String) this.getUserObject();
	    }
		
		public void execute(){
			gui.setPluginNode(getName(), this);
			gui.showPlugin(getName());
		}
		
		public void build(){
			gui.setPluginNode(getName(), this);
			gui.buildProject(getName());
		}
		
		public void refresh(){
			this.removeAllChildren();
			File file = info.getDirectory();
			gui.treePanel.buildTreeFromNode(this, file);
			gui.treePanel.reload(this);
			// System.out.println("Warning: feature not implemented yet");
		}
		
		public void select(){
			gui.setPluginNode(getName(), this);
			gui.showPlugin(getName());
		}
	}
