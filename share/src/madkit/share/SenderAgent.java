package madkit.share;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Vector;


import madkit.TreeTools.DirEntry;
//import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;


/*===============================================================================================*/
/*==================================== Class SenderAgent =========================================*/
/*===============================================================================================*/

public class SenderAgent extends AbstractServerAgent
{

    public String pluginsPath;             //see initDir method
    

    /*=====================================================*/
    /*======================= Attributs ===================*/
   // DirEntry dir;
   // FicDataBase dataBase;
    FicDataBase pluginDataBase;
	
    ListPluginInfo listPluginInfo;
    
    
   // public FicDataBase getData(){return dataBase;}
    	 
    /*===================================================================================*/
    /*======================= Initialisation de l'interface graphique ===================*/
    public void initGUI()
    {
	display = new SenderAgentPanel(this);
	setGUIObject(display);
    }


    /*=======================================================================*/
    /*======================= Activation de l'agent==========================*/
    public void activate(){
		createGroup(true,groupName,null,null);
		requestRole(groupName,roleName,null);
		live=true;
	
		initDir();
	
		//Creation de la base de donn�e contenant les fichiers partag�s
		dataBase = new FicDataBase();
		//Creation de la base de donn�e contenant les plugins disponibles
		listPluginInfo = new ListPluginInfo();
		initPluginsDataBase(dataBase);
    }


    /*========================================================================*/
    /*======================= Boucle principale de l'agent ===================*/
    public void live(){
		while(live) {
			Message m = waitNextMessage();
			try {
				handleMessage(m);
			}
			catch(IOException exc){System.out.println("Error : SenderAgent --> HandleMessage");}
		    }
	    }
	    	
    public void end(){
		for(int i=0;i<smallSenderAgentList.size();i++)
		    killAgent( (SmallSenderAgent)smallSenderAgentList.elementAt(i));
//		leaveRole(groupName,roleName);
//		leaveGroup(groupName);
    }


    /*=======================================================================*/
    /*======================= Traitement des messages =======================*/
    void handleMessage(Message m) throws IOException{
		if(m instanceof RequestPluginInfoMessage) { 
			System.out.println("(server) RequestPluginInfoMessage received");
			AgentAddress shareAddress=((RequestPluginInfoMessage)m).getSender();
			sendMessage(shareAddress,new PluginInfoMessage(listPluginInfo));	
		    }
		
		
		else if(m instanceof RequestTreeMessage) {
			System.out.println("(server) RequestTreeMessage received");
			String txt=((RequestTreeMessage)m).getText();
			
			if((txt).equals("request-tree")){
			    AgentAddress shareAddress=((RequestTreeMessage)m).getSender();
			    sendMessage(shareAddress,new TreeMessage( (DirEntry)((SenderAgentPanel)display).getEntry() ));	
			}
		}
		else if(m instanceof FicMessage){   //Nous sommes en presence d'un message de telechargement.
					//System.out.println("(server) FicMessage received");
					String nameClient=((FicMessage)m).getSender().getKernel().getHost().toString();
					AgentAddress addressClient=((FicMessage)m).getSender();
					String virtualPath = ((FicMessage)m).getPath();
					boolean requestPlugin = ((FicMessage)m).getUpdatePlugin();
					System.out.println("(server) Request file " + virtualPath);
					String realPath = dataBase.searchRealPath(virtualPath);
					if(realPath.equals("error")){
						System.out.println("Fichier non autorise :"+virtualPath);
						return;
					}
					ListFic index = new ListFic();
					File fic = new File(realPath);
					if(fic.isDirectory()){
						if(requestPlugin)
						dirPluginIndex(virtualPath,realPath,index);
						else dirIndex(virtualPath,realPath,index);
					}
					else{
						int sizeFic = (int)fic.length();
						FileInfo fi = new FileInfo(virtualPath,sizeFic);
						(index.getVector()).addElement(fi);
					}
					sendMessage(addressClient,new IndexMessage(index,this.getAddress(),requestPlugin));
				}
		else if(m instanceof RequestSearchFileMessage) {
					AgentAddress addressClient=((RequestSearchFileMessage)m).getSender();
					String path = ((RequestSearchFileMessage)m).getFile();
					//System.out.println("(server) SearchMessage received"); 
					Vector res = dataBase.searchFile(((RequestSearchFileMessage)m).getFile());
					if(!res.isEmpty()){
						System.out.println("---> File "+((RequestSearchFileMessage)m).getFile()+" found");
						for(int i=0;i<res.size();i++)
						sendMessage(addressClient,new SearchFileMessage(path,
												((AssocPath)res.elementAt(i)).getVirtualPath(),
												((AssocPath)res.elementAt(i)).getSizeFile(),
												((AssocPath)res.elementAt(i)).isDirectory()));
					}
				}
				if(m instanceof OffsetMessage) {
					AgentAddress clientAddress = ((OffsetMessage)m).getSender();
					String virtualPath = ((OffsetMessage)m).getName();
					int offsetStart = ((OffsetMessage)m).getOffsetStart();
					int offsetEnd = ((OffsetMessage)m).getOffsetEnd();
	
					//System.out.println("(server) OffsetMessage received");
					nbrSmallSenderAgent++;
					SmallSenderAgent ssa = new SmallSenderAgent(clientAddress,virtualPath,offsetStart,offsetEnd,dataBase,this);
					launchAgent(ssa,"smallSenderAgent"+nbrSmallSenderAgent,false);
					nbrSmallSenderAgent++;
					smallSenderAgentList.add(ssa);
					//System.out.println("(server) SmallSenderAgent created");
				}
				else if(m instanceof RequestSearchFileMessage) {
					AgentAddress addressClient=((RequestSearchFileMessage)m).getSender();
					String path = ((RequestSearchFileMessage)m).getFile();
					//System.out.println("(server) SearchMessage received"); 
					Vector res = dataBase.searchFile(((RequestSearchFileMessage)m).getFile());
					if(!res.isEmpty()){
						System.out.println("---> File "+((RequestSearchFileMessage)m).getFile()+" found");
						for(int i=0;i<res.size();i++)
						sendMessage(addressClient,new SearchFileMessage(path,
												((AssocPath)res.elementAt(i)).getVirtualPath(),
												((AssocPath)res.elementAt(i)).getSizeFile(),
												((AssocPath)res.elementAt(i)).isDirectory()));
					}
				}
		else super.handleMessage(m);
    }
	
 
//    public void windowClosing(WindowEvent we)
//    {
//	System.exit(0);
//    }
    	
    	
    /*===============================================================================================*/
    /*==================================== Methodes Annexes =========================================*/
    /*===============================================================================================*/
    public void initDir(){		   madkitDirectory = System.getProperty("madkit.dir");
		
		System.out.println("Madkit --> "+madkitDirectory);
		
		File f = new File(madkitDirectory+File.separator+groupName);
		f.mkdir();
		
		pluginsPath=madkitDirectory+File.separator+"plugins";
    }
    
    /*===============================================================================================*/
    public void dirIndex(String virtualPath,String realPath, ListFic index)
    {
	File fic = new File(realPath);
	File[] liste = fic.listFiles();
	File ficn;
	if (liste != null){
	    for(int i=0; i<liste.length; i++)
		{
		    ficn=liste[i];
		    if(ficn.isDirectory())
			{
			    dirIndex( (virtualPath + separator + ficn.getName()), (realPath + separator + ficn.getName()), index);
			}
		    else if (ficn.canRead())
			{
			    int sizeFic = (int)ficn.length();
			    FileInfo fi = new FileInfo( (virtualPath + separator + ficn.getName()),sizeFic);
			    (index.getVector()).addElement(fi);
			}
		}
	}
    }
    /*===============================================================================================*/
    public void dirPluginIndex(String virtualPath,String realPath, ListFic index)
    {
	File fic = new File(realPath);
	File[] liste = fic.listFiles();
	File ficn;
	if (liste != null){
	    for(int i=0; i<liste.length; i++){
		ficn=liste[i];
		if( !((ficn.getName()).equals("build")) ){
		    if(ficn.isDirectory()){
			dirPluginIndex( (virtualPath + separator + ficn.getName()), (realPath + separator + ficn.getName()), index);
		    }
		    else if (ficn.canRead()){
			int sizeFic = (int)ficn.length();
			FileInfo fi = new FileInfo( (virtualPath + separator + ficn.getName()),sizeFic);
			(index.getVector()).addElement(fi);
		    }
		}
	    }
	}
    }

    /*==============================================================================================*/

    public void initPluginsDataBase(FicDataBase dataBase){
    	System.out.println("Server Plugins Path --> "+pluginsPath);
		File plugDir = new File(pluginsPath);
		if(plugDir.exists()){
		    dataBase.addDir(pluginsPath);
	
		    File dirPlugins = new File(pluginsPath);
		    File[] listPlugins = dirPlugins.listFiles();
		    File plugin;
		    
		    if (listPlugins != null){
				for(int i=0; i<listPlugins.length; i++){
				    plugin=listPlugins[i];
				    String pluginName = plugin.getName();
				    
				    File pluginProperties = new File(pluginsPath+File.separator+
				    									pluginName+File.separator+pluginName+".properties");
				    
				    if(pluginProperties.exists() && pluginProperties.canRead()){
					try{
					    RandomAccessFile raf = new RandomAccessFile(pluginProperties,"r");
					    String line;
					    while ( (line = raf.readLine()) != null ){
						int start;
						if( (start = line.indexOf("madkit.plugin.version=")) != -1){
						    PluginVersionNumber pluginVersion = new PluginVersionNumber(line.substring(start+22,line.length()));
						    listPluginInfo.add(new PluginInfo(pluginName,pluginVersion,"plugins/"+pluginName));
						}
					    }
					    raf.close();
					}
					catch(IOException ioe){System.out.println("(Server) Can't read plugin "+ pluginName+" properties ");}  
				    }
				 }
				listPluginInfo.display();
			}
		}
    }
    /*==============================================================================================*/		    

}//fin classe
