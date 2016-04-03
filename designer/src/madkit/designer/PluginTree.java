package madkit.designer;

import java.io.*;
import java.lang.reflect.Constructor;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;

import madkit.TreeTools.*;
import madkit.boot.Madkit;
import madkit.kernel.AbstractAgent;
import madkit.kernel.Utils;
import madkit.system.PluginInformation;
import madkit.utils.common.PropertyFile;

import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

/**
 * PluginDesigner interface
 * The general has been (unfortunately) done with JBuilder...
 * @author Jacques Ferber
 */

class PluginTopNode extends GenericTreeNode{

    DirIconDescriptor dirNodeDescriptor; 

    public PluginTopNode(String name){
        super(name);
        dirNodeDescriptor = new DirIconDescriptor(true); 
        this.setDescriptor(dirNodeDescriptor);
    }

    public ImageIcon getLeafIcon(){
    	return dirNodeDescriptor.getLeafIcon();
    }

    public ImageIcon getBranchIcon(int k, boolean open){
    	return dirNodeDescriptor.getBranchIcon(k,open);
    }
}



//class PluginFileNode extends FileNode {
//	static FileIconDescriptor pluginfileNodeDescriptor =
//        new FileIconDescriptor(FileNode.fileNodeDescriptor, 
//        	"/images/kde/document.png",
//            new String[][]{ },
//			new String[][]{ } );
//	
//	PluginFileNode(File file){
//		super(file);
//	}
//}

class PluginTree extends ExplorerTree {

    PluginDesignerGUI gui;
    PluginDesignerAgent ag;
    Map plugins;	// the plugins . The map is shared with the PluginDesignerAgent
  
    
    public PluginTree(PluginDesignerGUI _gui, PluginDesignerAgent _agent){
        super();
        gui = _gui;
        ag = _agent;
		setLocalRoot(new PluginTopNode("plugins"));
		rootPath = System.getProperty("madkit.dir") + File.separator;
		top.add(getLocalRoot());
		buildTree(getLocalRoot(),_agent.getPlugins());
		installTree();
    }
    
    
    protected void buildTree(GenericTreeNode parent, Map _plugins){
    	plugins = _plugins;
    	installAllPlugins(parent);
    }
    
//    void addPluginName(String name){
//    	for(int i=0;i < pluginNames.size();i++){
//    		if (name.compareToIgnoreCase((String)pluginNames.get(i)) < 0)
//    			pluginNames.add(i,name);
//    	}
//    	pluginNames.add(name);
//    }
    public void treeWillExpand(TreeExpansionEvent e) {
    	int n = e.getPath().getPathCount();
//    	System.out.println("path : "+getPath((e.getPath()).getPath())+", taille:"+n);
//    	if (n == 2){
//    		getLocalRoot().removeAllChildren();
//        	installAllPlugins(getLocalRoot());
//    	}
    	if (n > 2){
			GenericTreeNode node =
				(GenericTreeNode) ((e.getPath()).getLastPathComponent());
			node.removeAllChildren();
			File file = new File(getPath((e.getPath()).getPath()));
			buildExplorerTree(node, file);
			//this.treeModel.reload(node);
    	}
	}
    
    void buildTreeFromNode(GenericTreeNode node, File file){
    	buildExplorerTree(node,file);
    }
    
    void reload(GenericTreeNode node){
    	treeModel.reload(node);
    }
    
    void installAllPlugins(GenericTreeNode parent){
    	Set pluginNames = plugins.keySet();
    	for(Iterator it = pluginNames.iterator();it.hasNext();){
    		String name = (String) it.next();
    		PluginInformation info=(PluginInformation) plugins.get(name);
        	//System.out.println("building node:"+name);
    		PluginNode pluginNode = new PluginNode(name,info,gui);
    		parent.add(pluginNode);
    		pluginNode.add(new GenericTreeNode("toto"));
    		//buildExplorerTree(pluginNode,info.getDirectory());
    	}
    }
 
    
    void addPlugin(String name, PluginInformation _info){
    	PluginNode pluginNode = new PluginNode(name,_info,gui);
    	this.addNode(getLocalRoot(),pluginNode);
		pluginNode.add(new GenericTreeNode("toto"));
    	gui.setPluginNode(name, pluginNode);
		//buildExplorerTree(pluginNode,_info.getDirectory());
    }
    
    protected void buildFileTree(GenericTreeNode parent, File file){
    	// parent.add(new FileNode(file));
    	FileNode icon=null;
    	String extens = FileIconDescriptor.getPathExtens(file.getName());
		if (extens != null) {
			if ((extens.equalsIgnoreCase("class"))
				|| (extens.equalsIgnoreCase("bak"))
				|| (file.getName().endsWith("~"))
				|| (file.getName().endsWith("#"))
				|| (extens.equalsIgnoreCase("bat"))
				|| (extens.equalsIgnoreCase("sh"))
				)
				icon = null; // DO NOTHING...
			else if (extens.equalsIgnoreCase("py"))
				icon = new PythonAgentNode(ag, file);
			else if (extens.equalsIgnoreCase("bsh"))
				icon =
					new BeanShellAgentNode(ag, file);
			else if (extens.equalsIgnoreCase("scm"))
				icon = new SchemeAgentNode(ag, file);
			else if (extens.equalsIgnoreCase("clp"))
				icon = new JessAgentNode(ag, file);
			else if (extens.equalsIgnoreCase("fml"))
				icon = new FormalismNode(ag, file);
			else if (extens.equalsIgnoreCase("cfg"))
				icon = new ConfigFileNode(ag, file);
			else if (extens.equalsIgnoreCase("sed"))
				icon = new SEditFileNode(ag, file);
			else if (extens.equalsIgnoreCase("html") || extens.equalsIgnoreCase("htm"))
				icon = new HTMLFileNode(ag, file);
			else if (file.getName().endsWith("build.xml"))
				icon = new AntFileNode(ag, file);
			else if (extens.equalsIgnoreCase("xml") || extens.equalsIgnoreCase("txt") 
						|| extens.equalsIgnoreCase("java"))
				icon = new EditableFileNode(ag, file);
			else if (extens.equalsIgnoreCase("properties")) {
				if (!processPropertyFileNode(ag,file,parent)) {
					icon = new PropertyFileNode(ag,file);
				} else
					icon = null;
			} else
				icon = new FileNode(file);
		} else
			icon = new FileNode(file);
		if (icon != null)
			parent.add(icon);
    }
    
    public boolean processPropertyFileNode(AbstractAgent ag, File file, GenericTreeNode parent) {
    		PropertyFile pf = new PropertyFile();
    		pf.loadFrom(file);
    		String jarName = pf.getProperty("madkit.plugin.name");
    		if (jarName != null) {
    			jarName = jarName + ".jar";
    			String fullJarName = Madkit.libDirectory + File.separator + jarName;
    			File jarFile = new File(fullJarName);
    			if (!jarFile.exists()) {
    				return (false);
    			}
    			String agentNames = pf.getProperty("madkit.plugin.agents");
    			String res=null;
    			if (agentNames == null)
    				res = null;
    			else {
    				if (agentNames.equalsIgnoreCase("${agents}") || agentNames.equalsIgnoreCase("all"))
    					res = null;
    				else if ((agentNames.equalsIgnoreCase("none")) || agentNames.equals(""))
    					res = "";
    				else res = agentNames;
    			}
    			processJarFile(ag, jarFile, parent, res);
    			return true;
    		}
    		return false;
    	}
    
	// this method should be split into several methods...
	void processJarFile(AbstractAgent ag, File f, GenericTreeNode parent, String agentNames) {
		//System.out.println(":: processing " + f);
		String str = null;
		if (agentNames != null){
			StringTokenizer st = new StringTokenizer(agentNames, " ,:;&\t\n\r\f");
			while (st.hasMoreTokens()) {
				str = st.nextToken();
				int r = str.lastIndexOf(".");
				String smallstr = str;
				if (r != -1)
					smallstr = str.substring(r + 1);
				installAgent(smallstr,str,parent, ag);
			}
		} else {
		try {
			JarFile jf = new JarFile(f);
			Manifest mft = jf.getManifest();
			if ((mft != null) && (mft.getEntries().size() > 0)) {
				boolean foundAgent = false;
				Map entries = mft.getEntries();
				//System.out.println("manifest in : " + f + ", size: "+entries.size());
				for (Iterator it = entries.keySet().iterator();it.hasNext();) {
					String key = (String) it.next();
					Attributes attr = (Attributes) entries.get(key);
					String res = attr.getValue("Agent");
					if (res == null)
						res = attr.getValue("Bean");
					//System.out.println(" agent : " + res);
					if ((res != null) && (res.equalsIgnoreCase("true"))) {
						foundAgent = true;
						int k = key.lastIndexOf('.');
						str = key.substring(0, k).replace('/', '.');
						int r = str.lastIndexOf(".");
						String smallstr = str;
						if (r != -1)
							smallstr = str.substring(r + 1);
						installAgent(smallstr,str,parent,ag);
					}
				}
				if (foundAgent)
					return;
			}
			// check entries to get AgentClasses
			else {
				for (Enumeration e = jf.entries(); e.hasMoreElements();) {
					JarEntry je = (JarEntry) e.nextElement();
					String jName = je.getName();
					//println(je.toString());
					if (!je.isDirectory()) {
						if (jName.endsWith(".class")) {
							int k = jName.lastIndexOf('.');
							str = jName.substring(0, k).replace('/', '.');
							int r = str.lastIndexOf(".");
							String smallstr = str;
							if (r != -1)
								smallstr = str.substring(r + 1);

							// Class c = Class.forName(str);
							//System.out.println("loading: " + str);
							installAgent(smallstr,str,parent,ag);
						}
					}
				}
			}
		} catch (IOException ex) {
			System.err.println("Not a good jar file : " + f.getName());
		} catch (Exception ex) {
			System.err.println(
				"Error while loading class : "
					+ str
					+ " check its default constructor");
		}
	  }
	}

	void installAgent(String smallstr,String str, GenericTreeNode parent, AbstractAgent ag) {
		// System.out.println("installing?: "+str);
		try {
			Class c = Utils.loadClass(str);
			if (Utils.loadClass("madkit.kernel.AbstractAgent").isAssignableFrom(c)) {
				Class[] params = new Class[0];
				Constructor def = c.getConstructor(params);
				if (def == null)
					System.err.println("WARNING: class "+ str+ " does not have default constructor");
				else {
					parent.add(new JavaAgentNode(ag, smallstr, str));
					//	 parent.add(new JavaAgentNode(smallstr,str));
				}
			}
		} catch (ClassNotFoundException ex) {
			System.err.println("Error : class not found : " + str);
		} catch (NoSuchMethodException ex) {
			System.err.println("Error : default constructor of class "+ str + " not found : ");
		} catch (Exception ex) {
			System.err.print("Error while loading class : "+ str + " error: "+ex);
		} catch (Throwable ex) {
			System.err.println("Error : class "+ str + " contains dependencies which are not in the classpath");
		}
	}
   
   
    
}
