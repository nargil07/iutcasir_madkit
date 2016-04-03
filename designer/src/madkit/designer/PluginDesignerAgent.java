package madkit.designer;

import madkit.kernel.*;
import madkit.boot.*;
import java.io.*;
import madkit.messages.*;
import madkit.system.*;
import java.util.*;

import org.apache.tools.ant.*;

public class PluginDesignerAgent extends Agent {

	Map<String, PluginInformation> plugins=new HashMap<String, PluginInformation>();
	Map<String, PluginNode> pluginNodes = new HashMap<String, PluginNode>();
	
	PluginDesignerGUI gui;
	
	Map getPlugins(){
		return plugins;
	}
	
	public PluginDesignerAgent()
    {
        plugins = new HashMap();
        gui = null;
    }

    public void initGUI()
    {
        gui = new PluginDesignerGUI(this);
        setGUIObject(gui);
    }

    public void activate()
    {
        createGroup(false, "system", null, null);
        requestRole("system", "designer", null);
        broadcastMessage("system", "plugin", new ActMessage("info"));
    }


	 public void live(){
	 	while(true){
	 		Message m = waitNextMessage();
			if (m instanceof ActMessage){
	      		handleMessage((ActMessage) m);
	      	}
	    }
	  }
	  
	  void handleMessage(ActMessage msg){
		String action = msg.getAction();
		if ("infos".equalsIgnoreCase(action)){
			//System.out.println("handling: "+msg);
			PluginInformation pluginfo=(PluginInformation) msg.getObject();
			String pluginName = pluginfo.getName();
			if (!plugins.containsKey(pluginName)){
				plugins.put(pluginfo.getName(),pluginfo);
				if (hasGUI()){
					gui.addPlugin(pluginfo.getName(),false);
				}
			}	
		}if ("newPlugin".equalsIgnoreCase(action)){
			PluginInformation pluginfo=(PluginInformation) msg.getObject();
			String pluginName = pluginfo.getName();
			if (!plugins.containsKey(pluginName)){
				plugins.put(pluginfo.getName(),pluginfo);
				if (hasGUI()){
					gui.addPlugin(pluginfo.getName(),true);
				}
			}	
		}
	  }
  
boolean createJavaAgent(String pluginName, String agentName, String agentType){
 	if (agentType.equalsIgnoreCase("Standard Java agent")){
 		createEntity("newJavaAgent", pluginName, agentName, "standard_java", "java");
		System.out.println("Created the Java file: " + agentName + " with agent type : "+agentType);
		return true;
 	} else if (agentType.equalsIgnoreCase("Standard Java agent with GUI")){
 		createEntity("newJavaAgentWithGUI", pluginName, agentName, "standard_java2", "java");
		System.out.println("Created Java files: " + agentName + " with agent type : "+agentType);
		return true;
 	}
	else 
		System.out.println("Cannot create a Java agent of type : "+agentType);
	// System.out.println("You could have a Java agent with name : "+agentName);
	return false;
}
  
 void createPythonAgent(String pluginName, String agentName, String agentType){
 	if (agentType.equalsIgnoreCase("Standard Python Agent")){
 		createEntity("newScriptAgent", pluginName, agentName, "standard_python", "py");
		System.out.println("Created a Python agent of type : "+agentType);
 	}
	else 
		System.out.println("Cannot create a Python Agent of type : "+agentType);
}
 
 void createJessAgent(String pluginName, String agentName, String agentType){
 	if (agentType.equalsIgnoreCase("Default Jess Agent")){
 		createEntity("newScriptAgent", pluginName, agentName, "standard_jess", "clp");
		System.out.println("Created a Jess agent of type : "+agentType);
 	}
	else 
		System.out.println("Cannot create a Jess agent of type : "+agentType);
}
 
 void createBshAgent(String pluginName, String agentName, String agentType){
 	if (agentType.equalsIgnoreCase("Default BeanShell Agent")){
 		createEntity("newScriptAgent", pluginName, agentName, "standard_bsh", "bsh");
		System.out.println("Created a BeanShell agent of type : "+agentType);
 	}
	else 
		System.out.println("Cannot create a BeanShell agent of type : "+agentType);
}
 
 void createSchemeAgent(String pluginName, String agentName, String agentType){
 	if (agentType.equalsIgnoreCase("Default Scheme Agent")){
 		createEntity("newScriptAgent", pluginName, agentName, "standard_scheme", "scm");
		System.out.println("Created a Scheme agent of type : "+agentType);
 	}
	else 
		System.out.println("Cannot create a Scheme agent of type : "+agentType);
}

 void createEntity(String target, String pluginName, String agentName, String agentType, String extens){
 		System.out.println("Creating a script agent of type "+agentType);
  		Project creatorProject;
  		Project buildProject;
  		File pluginDir = new File(Madkit.madkitDirectory, Madkit.pluginsDirName);
  		File pluginDesignerDir = new File(pluginDir,"designer");
  		File scriptDir = new File(pluginDesignerDir,"scripts");
		
  		BuildLogger logger = new DefaultLogger();
  		logger.setMessageOutputLevel(Project.MSG_INFO);
  		logger.setOutputPrintStream(System.out);
  		logger.setErrorPrintStream(System.err);
  		
  		File creatorFile = new File(scriptDir,"create.xml");
		File newPluginDir = new File(pluginDir,pluginName);
  		
		creatorProject = new Project();
	    creatorProject.addBuildListener(logger);
		creatorProject.init();
		ProjectHelper.configureProject(creatorProject,creatorFile);
		creatorProject.setUserProperty("agent.type",agentType);
		creatorProject.setUserProperty("newProject",pluginName);
		creatorProject.setUserProperty("script.extens",extens);
		creatorProject.setUserProperty("agent.name",agentName);
		try {
			creatorProject.executeTarget(target);
			System.out.println("Scripted agent : "+agentName+"."+extens+ " created");
		} catch(BuildException e){
			System.err.println("SCRIPT AGENT CREATION FAILED");
			System.err.println(e.getMessage());
		}
  	}
 
 boolean createPlugin(String name, String type){
  		System.out.println("Creating a plugin of type "+type);
  		Project creatorProject;
  		Project buildProject;
  		File pluginDir = new File(Madkit.madkitDirectory, Madkit.pluginsDirName);
  		File pluginDesignerDir = new File(pluginDir,"designer");
  		File scriptDir = new File(pluginDesignerDir,"scripts");
		
  		BuildLogger logger = new DefaultLogger();
  		logger.setMessageOutputLevel(Project.MSG_INFO);
  		logger.setOutputPrintStream(System.out);
  		logger.setErrorPrintStream(System.err);
  		
  		File creatorFile = new File(scriptDir,"create.xml");
		File newPluginDir = new File(pluginDir,name);
		boolean b = newPluginDir.mkdir();
		if (!b){
			System.err.println("Cannot create directory: "+newPluginDir);
			return false;
		}
  		
		creatorProject = new Project();
	    creatorProject.addBuildListener(logger);
		creatorProject.init();
		ProjectHelper.configureProject(creatorProject,creatorFile);
		creatorProject.setUserProperty("plugin.type",type);
		creatorProject.setUserProperty("newProject",name);
		try {
			creatorProject.executeTarget(creatorProject.getDefaultTarget());

			buildProject = new Project();
			buildProject.addBuildListener(logger);
			buildProject.init();
			File buildFile = new File(newPluginDir,"build.xml");
			
			ProjectHelper.configureProject(buildProject,buildFile);
			buildProject.executeTarget(buildProject.getDefaultTarget());
			PluginAgent pluginAgent = new PluginAgent();
			pluginAgent.init(newPluginDir);
			this.launchAgent(pluginAgent,"plugin-"+pluginAgent.getPluginName(),false);
			System.out.println("Plugin: "+name+ " created");
			return true;
			
		} catch(BuildException e){
			System.err.println("BUILD FAILED");
			System.err.println(e.getMessage());
		} return false;
  	}
  }


