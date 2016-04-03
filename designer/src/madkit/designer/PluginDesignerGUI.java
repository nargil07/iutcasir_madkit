package madkit.designer;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;
import javax.swing.border.*;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.BuildLogger;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

import madkit.system.PluginInformation;
import madkit.boot.Madkit;

import madkit.kernel.Utils;
import madkit.utils.graphics.GraphicUtils;
import madkit.utils.graphics.LoadDialog;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * PluginDesigner interface
 * The general has been (unfortunately) done with JBuilder...
 * @author Jacques Ferber
 */

public class PluginDesignerGUI extends JRootPane implements ActionListener {


  Map<String, PluginNode> pluginNodes = new HashMap<String, PluginNode>();
  String madkitDir = System.getProperty("madkit.dir");
  Map plugins;
  PluginInformation currentPlugin;
  boolean pluginModified=false;		// says if the currentPlugin has been modified
  
  DefaultListModel dependListModel = new DefaultListModel();
  DefaultListModel requireListModel = new DefaultListModel();

  BorderLayout borderLayout1 = new BorderLayout();
  JSplitPane horizSplitPanel = new JSplitPane();
  JTabbedPane tabbedPane = new JTabbedPane();
//  LocalTree treePanel = new LocalTree(new File(System.getProperty("user.dir")));

  PluginTree treePanel = null;
  
 // JTree jTree1 = new JTree();
  JToolBar buttonToolBar = new JToolBar();
  JButton bNew, bNewJavaAgent, bNewPythonAgent, bNewSchemeAgent, bNewBshAgent, bNewJessAgent;
  //JButton bBuild = new JButton();
  JPanel generalPanel = new JPanel();
  JPanel dependsPanel = new JPanel();
  JMenuBar project_menubar = new JMenuBar();
  JMenu jproject_menu = new JMenu();
  JMenuItem jMenuItem1 = new JMenuItem();
  JMenuItem jMenuItem2 = new JMenuItem();
  BorderLayout borderLayout2 = new BorderLayout();
  Border border1;
  Border border2;
  JPanel valuePanel = new JPanel();
  BorderLayout borderLayout3 = new BorderLayout();
  JLabel projectNameField = new JLabel();
  GridLayout gridLayout1 = new GridLayout(0,1);
  JTextField versionNameField = new JTextField();
  JPanel fieldPanel = new JPanel();
  JTextField authorNameField = new JTextField();
  JLabel jarName = new JLabel();
  JLabel dirName = new JLabel();
  JLabel dirLabel = new JLabel("Directory : ");
  JLabel authorLabel = new JLabel("Author(s) name :  ");
  JLabel jarLabel = new JLabel("Jar name : ");
  JLabel nameLabel = new JLabel();
  JPanel labelPanel = new JPanel();
  GridLayout gridLayout2 = new GridLayout(0,1);
  JLabel versionLabel = new JLabel("Version number :  ");
  JPanel contentPanel = new JPanel();
  //JButton bDocs = new JButton();
  //JButton bClean = new JButton();
  //JButton jbBuildAll = new JButton();
  //JSplitPane verticalSplitPanel = new JSplitPane();
  //JScrollPane jScrollPane1 = new JScrollPane();
  //JTextArea outputArea = new JTextArea(5,20);
  BorderLayout borderLayout4 = new BorderLayout();
  JSplitPane splitReqPane = new JSplitPane();
  JPanel projectReqPanel = new JPanel();
  JPanel jarReqPanel = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  BorderLayout borderLayout6 = new BorderLayout();
  TitledBorder titledBorder1;
  TitledBorder titledBorder2;
  Border border3;
  Border border4;
  TitledBorder titledBorder3;
  Border border5;


  JList reqProjectsList;
  JList reqJarList;


// local attributes
  PluginDesignerAgent ag;
  JPanel depButtonPanel = new JPanel();
  JButton depAdd = new JButton();
  JButton depRemove = new JButton();
  JPanel reqButtonPanel = new JPanel();
  JButton reqAdd = new JButton();
  JButton reqRemove = new JButton();
  JPanel genButtonPanel = new JPanel();
  JButton genValidate = new JButton();
  JButton butBuild = new JButton();
  JTextField descrField = new JTextField();
  JLabel descrLabel = new JLabel();
  JTextField catField = new JTextField();
  JLabel catLabel = new JLabel();

  public PluginDesignerGUI(PluginDesignerAgent _ag) {
    ag = _ag;
    try {
    	plugins = _ag.getPlugins();
    	treePanel = new PluginTree(this,_ag);
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
    border1 = BorderFactory.createCompoundBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED,Color.white,Color.white,new Color(103, 101, 98),new Color(148, 145, 140)),BorderFactory.createEmptyBorder(10,10,10,10));
    border2 = BorderFactory.createCompoundBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),BorderFactory.createEmptyBorder(20,20,20,20));
    titledBorder1 = new TitledBorder("");
    titledBorder2 = new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Reguired plugins");
    border3 = BorderFactory.createCompoundBorder(titledBorder2,BorderFactory.createEmptyBorder(5,5,5,5));
    border4 = BorderFactory.createEmptyBorder(5,5,5,5);
    titledBorder3 = new TitledBorder(new EtchedBorder(EtchedBorder.RAISED,Color.white,new Color(148, 145, 140)),"Jars required");
    border5 = BorderFactory.createCompoundBorder(new TitledBorder(BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140)),"Required jars"),BorderFactory.createEmptyBorder(5,5,5,5));
   // this.setPreferredSize(new Dimension(400, 300));
    this.getContentPane().setLayout(borderLayout1);
    horizSplitPanel.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    //horizSplitPanel.setMinimumSize(new Dimension(12, 22));
    treePanel.setPreferredSize(new Dimension(160, 280));
    treePanel.setMinimumSize(new Dimension(160,10));
    //verticalSplitPanel.setOrientation(JSplitPane.VERTICAL_SPLIT);
    
    bNew = GraphicUtils.addTool(this, buttonToolBar, "new_project", "New project",  "/images/pluginagent.gif"); 
    bNewJavaAgent = GraphicUtils.addTool(this, buttonToolBar, "new_java_agent", "New Java agent", "/images/javaagent.gif");
    bNewPythonAgent = GraphicUtils.addTool(this, buttonToolBar, "new_python_agent", "New Python agent", "/images/agents/agentPython.gif");
    bNewSchemeAgent = GraphicUtils.addTool(this, buttonToolBar, "new_scheme_agent",  "New Scheme (Kawa) agent", "/images/agents/schemeAgent.gif");
    bNewBshAgent = GraphicUtils.addTool(this, buttonToolBar, "new_bsh_agent", "New BeanShell (Java interpreted) agent", "/images/agents/agentBeanShell.gif");
    bNewJessAgent = GraphicUtils.addTool(this, buttonToolBar, "new_jess_agent", "New Jess (Rule based system) agent", "/images/agents/jessAgent.gif");


    dependListModel.addElement("madkitkernel");
    
    jMenuItem1.setText("new project");
    jMenuItem2.setText("exit");
    generalPanel.setLayout(borderLayout2);
    generalPanel.setBorder(border2);
    valuePanel.setLayout(borderLayout3);
    projectNameField.setPreferredSize(new Dimension(51, 21));
    projectNameField.setFont(new java.awt.Font("Dialog", 1, 11));
    projectNameField.setText("<project>");
    gridLayout1.setColumns(1);
    gridLayout1.setHgap(2);
    gridLayout1.setRows(7);
    gridLayout1.setVgap(5);
    versionNameField.setText("1.0");
    fieldPanel.setLayout(gridLayout1);
    fieldPanel.setBorder(null);
    authorNameField.setText("<author(s)>");
    jarName.setText("<project>.jar");
    authorLabel.setFont(new java.awt.Font("Dialog", 1, 11));
    jarLabel.setFont(new java.awt.Font("Dialog", 1, 11));
    nameLabel.setFont(new java.awt.Font("Dialog", 1, 11));
    nameLabel.setText("Project name : ");
    dirLabel.setFont(new java.awt.Font("Dialog", 1, 11));
    labelPanel.setLayout(gridLayout2);
    gridLayout2.setColumns(1);
    gridLayout2.setHgap(2);
    gridLayout2.setRows(7);
    gridLayout2.setVgap(5);
    versionLabel.setFont(new java.awt.Font("Dialog", 1, 11));
//    bDocs.setActionCommand("make_docs");
//    bDocs.setText("Build doc");
//    bClean.setActionCommand("clean");
//    bClean.setContentAreaFilled(true);
//    bClean.setText("Clean");
//    jbBuildAll.setActionCommand("build_all");
//    jbBuildAll.setText("Build All");
    dependsPanel.setDebugGraphicsOptions(0);
    dependsPanel.setLayout(borderLayout4);
    splitReqPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
    projectReqPanel.setLayout(borderLayout5);
    jarReqPanel.setLayout(borderLayout6);

    reqProjectsList = new JList(dependListModel);
    reqJarList = new JList(requireListModel);
    JScrollPane reqProjectsListScroller = new JScrollPane(reqProjectsList);
    reqProjectsListScroller.setPreferredSize(new Dimension(180, 100));
    
    reqProjectsListScroller.setBorder(null);
    projectReqPanel.setBorder(border3);
    jarReqPanel.setBorder(border5);
    depAdd.setText("Add ...");
    depAdd.setActionCommand("add_depend");
    depAdd.addActionListener(this);
    depRemove.setText("Remove");
    depRemove.setActionCommand("remove_depend");
    depRemove.addActionListener(this);
    //reqAdd.setMinimumSize(new Dimension(67, 25));
    reqAdd.setText("Add ...");
    reqAdd.setActionCommand("add_require");
    reqAdd.addActionListener(this);
    reqRemove.setText("Remove");
    reqRemove.setActionCommand("remove_require");
    reqRemove.addActionListener(this);
    
    genValidate.setText("Validate");
    genValidate.setEnabled(false);
    butBuild.setText("Build");
    butBuild.setEnabled(false);
    genValidate.addActionListener(this);
    butBuild.addActionListener(this);
    tabbedPane.setPreferredSize(new Dimension(200, 280));
    descrField.setToolTipText("");
    descrField.setText("The (still) unknown project");
    descrLabel.setFont(new java.awt.Font("Dialog", 1, 11));
    descrLabel.setText("Description");
    catField.setText("demos");
    catLabel.setText("Category");
    catLabel.setFont(new java.awt.Font("Dialog", 1, 11));
    horizSplitPanel.add(treePanel, JSplitPane.LEFT);
    
    JPanel rightContentPane=new JPanel(new BorderLayout());
    horizSplitPanel.add(rightContentPane, JSplitPane.RIGHT);
    rightContentPane.add(tabbedPane,BorderLayout.CENTER);
    rightContentPane.add(genButtonPanel,BorderLayout.SOUTH);

    // treePanel.add(jTree1, null);

    this.getContentPane().add(buttonToolBar, BorderLayout.NORTH);
    this.getContentPane().add(horizSplitPanel, BorderLayout.CENTER);
    //buttonToolBar.add(bNew, null);
    //buttonToolBar.add(jbBuildAll, null);
    //buttonToolBar.add(bBuild, null);
    tabbedPane.add(generalPanel,  "General");
    tabbedPane.add(dependsPanel,    "Java build path");
    dependsPanel.add(splitReqPane, BorderLayout.CENTER);
    splitReqPane.add(projectReqPanel, JSplitPane.LEFT);
    this.setMenuBar(project_menubar);
    project_menubar.add(jproject_menu);
    jproject_menu.add(jMenuItem1);
    jproject_menu.add(jMenuItem2);
    generalPanel.add(valuePanel, BorderLayout.CENTER);
    valuePanel.add(fieldPanel,  BorderLayout.CENTER);
    fieldPanel.add(projectNameField, null);
    fieldPanel.add(jarName, null);
    fieldPanel.add(authorNameField, null);
    fieldPanel.add(versionNameField, null);
    fieldPanel.add(descrField, null);
    valuePanel.add(labelPanel,  BorderLayout.WEST);
    labelPanel.add(nameLabel, null);
    labelPanel.add(jarLabel, null);
    labelPanel.add(authorLabel, null);
    tabbedPane.add(contentPanel,  "doc");
//    buttonToolBar.add(bDocs, null);
//    buttonToolBar.add(bClean, null);
//    verticalSplitPanel.add(tabbedPane, JSplitPane.TOP);
 //   verticalSplitPanel.add(jScrollPane1, JSplitPane.BOTTOM);
    //jScrollPane1.getViewport().add(outputArea, null);
    splitReqPane.add(projectReqPanel, JSplitPane.TOP);
    splitReqPane.add(jarReqPanel, JSplitPane.BOTTOM);
    projectReqPanel.add(reqProjectsListScroller, BorderLayout.CENTER);
    projectReqPanel.add(depButtonPanel,  BorderLayout.SOUTH);
    jarReqPanel.add(reqJarList,  BorderLayout.CENTER);
    depButtonPanel.add(depAdd, null);
    depButtonPanel.add(depRemove, null);
    jarReqPanel.add(reqButtonPanel,  BorderLayout.SOUTH);
    reqButtonPanel.add(reqAdd, null);
    reqButtonPanel.add(reqRemove, null);
    //valuePanel.add(genButtonPanel,  BorderLayout.SOUTH);
    genButtonPanel.add(genValidate, null);
    genButtonPanel.add(butBuild, null);
    labelPanel.add(versionLabel, null);
    labelPanel.add(descrLabel, null);
    fieldPanel.add(catField, null);
    labelPanel.add(catLabel, null);
	fieldPanel.add(dirName, null);
	labelPanel.add(dirLabel, null);
  }
  Map getPlugins(){
  	return plugins;
  }

  void showPlugin(String name){
  		PluginInformation info=(PluginInformation) plugins.get(name);
  		currentPlugin = info;
  		 genValidate.setEnabled(true);
  		 butBuild.setEnabled(true);
  		projectNameField.setText(name);
  	    versionNameField.setText(info.getVersion().toString());
  	    authorNameField.setText(info.getAuthorName());
  	    jarName.setText(name+".jar");
  	    descrField.setText(info.getDescription());
  	    dirName.setText(info.getDirectory().toString());
  	    // System.out.println("Depends: "+info.getDependList());
  	    dependListModel.removeAllElements();
  	    Vector v = info.getDependList();
  	    for (int i=0;i<v.size();i++){
  	    	dependListModel.addElement(v.get(i));
  	    }
  	    dependsPanel.revalidate();
  	    dependsPanel.repaint();
  	    requireListModel.removeAllElements();
	    Vector vReq = info.getRequireList();
	    for (int i=0;i<vReq.size();i++){
	    	requireListModel.addElement(vReq.get(i));
	    }
	    jarReqPanel.revalidate();
	    jarReqPanel.repaint();
  }
  
  void addDepends(Object[] v){
  	if (v.length > 0) {
  		boolean modif = false;
	  	for (int i=0;i<v.length;i++){
	  		if (!dependListModel.contains(v[i])){
		    	dependListModel.addElement(v[i]);
		    	currentPlugin.addDepend((String) v[i]);
	  			modif = true;
		    }
  		}
	  	if (modif){
		  	pluginModified = true;
			dependsPanel.revalidate();
			dependsPanel.repaint();
	  	}
  	}
  }
  
  void removeDepend(Object[] v){
	if (v.length > 0) {
  		boolean modif = false;
	  	for (int i=0;i<v.length;i++){
	  		if (dependListModel.contains(v[i])){
		    	dependListModel.removeElement(v[i]);
		    	currentPlugin.removeDepend((String) v[i]);
	  			modif = true;
		    }
  		}
	  	if (modif){
		  	pluginModified = true;
			dependsPanel.revalidate();
			dependsPanel.repaint();
	  	}
  	}
  }
  void addRequire(Object[] v){
  	if (v.length > 0) {
  		boolean modif = false;
	  	for (int i=0;i<v.length;i++){
	  		if (!requireListModel.contains(v[i])){
	  			requireListModel.addElement(v[i]);
		    	currentPlugin.addRequire((String) v[i]);
	  			modif = true;
		    }
  		}
	  	if (modif){
		  	pluginModified = true;
		  	jarReqPanel.revalidate();
		  	jarReqPanel.repaint();
	  	}
  	}
  }
  
  void removeRequire(Object[] v){
	if (v.length > 0) {
  		boolean modif = false;
	  	for (int i=0;i<v.length;i++){
	  		if (requireListModel.contains(v[i])){
	  			requireListModel.removeElement(v[i]);
		    	currentPlugin.removeRequire((String) v[i]);
	  			modif = true;
		    }
  		}
	  	if (modif){
		  	pluginModified = true;
		  	jarReqPanel.revalidate();
		  	jarReqPanel.repaint();
	  	}
  	}
  }
  
  void updatePlugin(){
  	if (currentPlugin == null){
  		System.err.println("No project selected.. Please select a project first");
  	}
  	currentPlugin.setVersion(versionNameField.getText());
  	currentPlugin.setAuthorName(authorNameField.getText());
  	currentPlugin.setDescription(descrField.getText());
  }
  
  void addPlugin(String name, boolean select){
  	PluginInformation plugin = (PluginInformation) plugins.get(name);
  	if (plugin == null){
  		System.err.println("Error: not a real madkit plugin: " + name);
  	}
  	String system = plugin.getSystem();
  	if (system.equalsIgnoreCase("false")){
  		treePanel.addPlugin(name,(PluginInformation) plugins.get(name));
  		if (select) {
  			showPlugin(name);
  			buildProject(name);
  		}
  	}
  }
  
  void buildCurrentProject(){
  	if (currentPlugin == null){
  		System.err.println("Not project selected.. Please select a project first");
  	}
  	buildProject(currentPlugin);
  }
  
  void buildProject(String name){
  	PluginInformation plugin = (PluginInformation) plugins.get(name);
  	if (plugin == null){
  		System.err.println("Error: not a correct project ");
  	} else buildProject(plugin);
  }
  
  void buildProject(PluginInformation plugin){
  	File buildFile = new File(plugin.getDirectory(),"build.xml");
  	if (!buildFile.isFile()){
  		System.err.println("Error: cannot build project, cannot find build.xml file");
  		return;
  	}
  	try {
		Project project = new Project();
		BuildLogger logger = new DefaultLogger();
		logger.setMessageOutputLevel(Project.MSG_INFO);
		logger.setOutputPrintStream(System.out);
		logger.setErrorPrintStream(System.err);
		project.addBuildListener(logger);
		project.init();
		ProjectHelper.configureProject(project,buildFile);
	
		String systemProject = project.getProperty("system.plugin");
		if ((systemProject == null) || (!"true".equalsIgnoreCase(systemProject))){
			project.executeTarget(project.getDefaultTarget());
			// reload the jar...
			String jarName = project.getProperty("jar.name");
			//System.out.println("Reloading jar: "+jarName);
			String jarPath = Madkit.libDirectory+File.separator+jarName;
			File jarFile = new File(jarPath);
			if (jarFile.isFile()){
				File[] fileLst = new File[1];
				fileLst[0]=jarFile;
				Madkit.newMadkitClassLoader(fileLst);
			}
			System.out.println("BUILD SUCCESSFUL");
			// Met a jour le plugin
			getPluginNode(plugin.getName()).refresh();
		}
		else
			System.out.println("Cannot build the system project : "+plugin.getName());
  	} catch(BuildException e){
		System.err.println("BUILD FAILED");
		System.err.println(e.getMessage());
	}
  }
  
 void setPluginNode(String name, PluginNode node){
	 pluginNodes.put(name, node);
 }
 
 PluginNode getPluginNode(String name){
	 return pluginNodes.get(name);
 }
  
 void createPlugin(String s, String type){
  			boolean b = ag.createPlugin(s,type);
  			if (b) this.buildProject(s);
  }
  
 void createJavaAgent(String s, String type){
		boolean b = ag.createJavaAgent(currentPlugin.getName(),s,type);
		if (b) buildCurrentProject();
}
  
 void createPythonAgent(String s, String type){
	ag.createPythonAgent(currentPlugin.getName(),s,type);
}
 
 void createJessAgent(String s, String type){
	ag.createJessAgent(currentPlugin.getName(),s,type);
}
 void createBshAgent(String s, String type){
	ag.createBshAgent(currentPlugin.getName(),s,type);
}
 void createSchemeAgent(String s, String type){
	ag.createSchemeAgent(currentPlugin.getName(),s,type);
}
 
 boolean checkCurrentPlugin(){
 	if (currentPlugin == null){
 		JOptionPane.showMessageDialog(null, "Select a plugin first", "No plugin selected", 
				JOptionPane.INFORMATION_MESSAGE);
 		return false;
 	} else 
 		return true;
 }
  
  public void actionPerformed(ActionEvent e){
  	String c = e.getActionCommand();
  	if (c.equalsIgnoreCase("new_project")){
  		JDialog dialog = new NewPluginDialog(this,"Create new plugin");
  		dialog.show();
  	} else if (c.equalsIgnoreCase("new_java_agent")){
  		if (!checkCurrentPlugin()) return;
		JDialog dialog = new NewJavaAgentDialog(this,"Create new Java Agent");
		dialog.show();
	} else if (c.equalsIgnoreCase("new_python_agent")){
  		if (!checkCurrentPlugin()) return;
		JDialog dialog = new NewPythonAgentDialog(this,"Create new Python Agent");
		dialog.show();
  	} else if (c.equalsIgnoreCase("new_scheme_agent")){
  		if (!checkCurrentPlugin()) return;
		JDialog dialog = new NewSchemeAgentDialog(this,"Create new Scheme Agent");
		dialog.show();
	} else if (c.equalsIgnoreCase("new_jess_agent")){
  		if (!checkCurrentPlugin()) return;
		JDialog dialog = new NewJessAgentDialog(this,"Create new Jess Agent");
		dialog.show();
	} else if (c.equalsIgnoreCase("new_bsh_agent")){
		if (!checkCurrentPlugin()) return;
		JDialog dialog = new NewBshAgentDialog(this,"Create new BeanShell Agent");
		dialog.show();
	} else if (c.equalsIgnoreCase("Validate")) {
  		validate();
  	} else if (c.equalsIgnoreCase("Build")) {
  		buildCurrentProject();
  	} else if (c.equalsIgnoreCase("remove_depend")){
  		if (!checkCurrentPlugin()) return;
  		if (JOptionPane.showConfirmDialog(Utils.getFrameParent(this),
  	  			"Do you really want to remove this dependencies?",
  				"Removing dependencies",
  				JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
  			Object[] values = reqProjectsList.getSelectedValues();
  			removeDepend(values);
  		}
  	} else if (c.equalsIgnoreCase("add_depend")){
  		if (!checkCurrentPlugin()) return;
		JDialog dialog = new AddDependElement(this,Utils.getRealFrameParent(this));
		dialog.show();
  	} else if (c.equalsIgnoreCase("add_require")){
  		addRequireElement();
  	} else if (c.equalsIgnoreCase("remove_require")){
  		if (!checkCurrentPlugin()) return;
  		if (JOptionPane.showConfirmDialog(Utils.getFrameParent(this),
  	  			"Do you really want to remove this jar?",
  				"Removing external jars",
  				JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION){
  			Object[] values = reqJarList.getSelectedValues();
  			removeRequire(values);
  		}
  	}
  }
  
  public void validate(){
  	if (currentPlugin == null){
  		System.err.println("Error: select a plugin first");
  		return;
  	}
  	updatePlugin(); // get information from the fields..
  	File dir = currentPlugin.getDirectory();
  	//System.out.println("dir: "+dir);
  	File buildFile = new File(dir,"build.xml");
  	String buildText=null;
  	if (!buildFile.isFile()){
  		System.err.println("Error: cannot validate modifs, cannot find build.xml file");
  		return;
  	}
  	try {
	    FileReader reader = new FileReader(buildFile);
	    StringWriter writer = new StringWriter();
	    Utils.copyToWriter( reader, writer );
	    buildText = writer.toString();
  	} catch(Exception ex){
  		System.err.println("Error in validating the build.xml file");
  	}
  	// Apply the substitutions to the string
  	buildText = propertySubst("author",currentPlugin.getAuthorName(),buildText);
  	buildText = propertySubst("description",currentPlugin.getDescription(),buildText);
	buildText = propertySubst("depend",currentPlugin.getDepends(),buildText);
	buildText = propertySubst("requires",currentPlugin.getRequires(),buildText);
  	buildText = propertySubst("version",currentPlugin.getVersion().toString(),buildText);
  	if (currentPlugin.getDocFile() != null)
  		buildText = propertySubst("docfile",currentPlugin.getDocFile(),buildText);
  	buildText = propertySubst("category",currentPlugin.getCategory(),buildText);
  	buildText = classpathSubst(buildText);
  	//buildText = propertySubst("category",currentPlugin.getAgentNames(),buildText);
  	//System.out.println(buildText);
  	// save the build.xml file
  	try{
	  	StringReader stringReader = new StringReader(buildText);
	    FileWriter fileWriter = new FileWriter(buildFile);
	    Utils.copyToWriter(stringReader, fileWriter);
  	}
    catch(Exception ex){
  		System.err.println("Error in saving the build.xml file");
    }
    System.out.println("Plugin: "+currentPlugin.getName()+" validated");
  }
  
  String propertySubst(String prop, String value, String text){
  	//System.out.println("Substitute: "+prop+ " with "+value);
  	Pattern p1 = Pattern.compile("<property name=\""+prop+"\"( +)value=\"(.*)\"/>");
	Matcher m1 = p1.matcher(text);
	return m1.replaceFirst("<property name=\""+prop+"\" value=\""+value+"\"/>");
  }
  
  String classpathSubst(String text){
  	Pattern pl = Pattern.compile("<fileset id=\"project.class.path\"(.*)</fileset>",Pattern.DOTALL);
	Matcher m1 = pl.matcher(text);
  	//System.out.println("pattern found :"+m1.find());
	String st = "";
	Vector depends = currentPlugin.getDependList();
	int dependsize = depends.size();
	for (int i=0;i<dependsize;i++){
		st = st + "\t\t<include name=\""+depends.get(i)+".jar\"/>\n";
	}
	Vector requires = currentPlugin.getRequireList();
	int reqsize = requires.size();
	for (int i=0;i<reqsize;i++){
		st = st + "\t\t<include name=\""+requires.get(i)+"\"/>\n";
	}

  	//System.out.println("st="+st);
	String res= m1.replaceFirst("<fileset id=\"project.class.path\" dir=\"\\${jars.dir}\">\n"
			+st+"</fileset>");
	//System.out.println("res="+res);
	return res;
  }
  
  void addRequireElement(){
  	  String dir=System.getProperty("madkit.dir")+File.separator+Madkit.libDirName;
	  LoadDialog ld = new LoadDialog(this, dir, LoadDialog.LOAD,"add external jars","jar");
	  if (ld.isFileChoosed()) {
	  	addRequire(new String[]{ld.getFileName()});
	  }
  }


}

class AddDependElement extends JDialog implements ActionListener {
	JList allPluginsList;
	protected JPanel buttonPanel;
	JButton addButton, cancelButton;
	PluginDesignerGUI gui;
	
	AddDependElement(PluginDesignerGUI gui, Frame parent){
		super(parent, "Add plugin dependencies");
		this.gui = gui;
		allPluginsList = new JList(new Vector(gui.getPlugins().keySet()));
	    JScrollPane scroller = new JScrollPane(allPluginsList);
	    scroller.setPreferredSize(new Dimension(140, 100));
		this.getContentPane().add(scroller,BorderLayout.CENTER);
	    
	    buttonPanel = new JPanel();
		this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);
			
		addButton = new JButton();
		buttonPanel.add(addButton);
		addButton.setText("Add");
		addButton.addActionListener(this);

		cancelButton = new JButton();
		buttonPanel.add(cancelButton);
		cancelButton.setText("Cancel");
		cancelButton.addActionListener(this);
		pack();
		show();
	}
	
	
	
	public void actionPerformed(ActionEvent e){
		if (e.getSource()==addButton){
			Object[] values = allPluginsList.getSelectedValues();
			//System.out.println("value 0 :"+values[0]+", "+values[0].getClass());
			gui.addDepends(values);
			this.dispose();
		} else
			this.dispose();
	}
}
