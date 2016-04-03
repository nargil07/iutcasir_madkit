/*
 * Created on Mar 14, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package madkit.MadChat;
import madkit.MadChat.share.SenderAgent;
import madkit.MadChat.share.ShareAgent;
import madkit.kernel.*;
import java.io.*;
import java.awt.*;
import java.util.*;

public class Config {
		private Agent chatAgent;
		private SenderAgent senderAgent;
		private ShareAgent shareAgent;
		private SmileyTab smileyTab;
		private String configPath;
		private String iconsPath;
		private String smileyPath;
		private String fileSeparator;
		private String configFile;
		private String theme;
		private boolean logAuto;
		private boolean timestamp;
		private String background;
		private String defaultColor; //de la forme #xxxxxx
		private String login;
		private String osName;
		private String logPath;
		private Vector configChangeListeners;
		private String themePath;
		private String lang;
		private Hashtable langProperties;
		private boolean share;
		private boolean shareRunning;
		
		private Vector tabRepertoiresPartages;
		
	public Config(Agent a){
		tabRepertoiresPartages= new Vector();
		chatAgent=a;
		configChangeListeners=new Vector();
		configFile="madchat.ini";
		osName=System.getProperty("os.name");
		smileyTab=new SmileyTab();
		fileSeparator=System.getProperty("file.separator");
		configPath=System.getProperty("user.dir")+fileSeparator+"plugins"+fileSeparator+"MadChat"+fileSeparator+"madchat"+fileSeparator;
		themePath= new String(configPath+"Themes"+fileSeparator);
		langProperties=new Hashtable();
		File f=new File(configPath+configFile);
		if(f.exists()){
			//si le fichier de config existe, on le lit et on cr?e les var. 
			//correspondant au fichier de config
		    String ligne;

		    try
		      {
		    	BufferedReader fileIn = new BufferedReader(new FileReader(f));
		    	while ((ligne = fileIn.readLine()) != null){
		    		int pos=ligne.indexOf("=");
		    		if(pos>-1){
		    			String param=ligne.substring(0,pos);
		    			String value=ligne.substring(pos+1);
		    			if(param.equalsIgnoreCase("theme")){
		    				theme=value;
		    			}
		    			else if(param.equalsIgnoreCase("share")){
		    				share=value.equals("1");
		    				shareRunning=value.equals("1");
		    			}
		    			else if(param.equalsIgnoreCase("nomrep")){
		    				tabRepertoiresPartages.add(value);
		    			}
		    			else if(param.equalsIgnoreCase("logauto")){
		    				logAuto=value.equals("1");
		    			}
		    			else if(param.equalsIgnoreCase("timestamp")){
		    				timestamp=value.equals("1");
		    			}
		    			else if(param.equalsIgnoreCase("background")){
		    				if(!value.equals(""))
		    					background=value;
		    				else
		    					background=configPath+"Themes"+fileSeparator+theme+fileSeparator+"background.jpg";
		    			}
		    			else if(param.equalsIgnoreCase("color")){
		    				defaultColor=value;
		    			}
		    			else if(param.equalsIgnoreCase("lang")){
		    				lang=value;
		    			}
		    			else if(param.equalsIgnoreCase("login")){
		    				login=value;
		    			}
		    			else if(param.equalsIgnoreCase("logpath")){
		    				logPath=value;
		    			}
		    			
		    			
		    		}
		    	}
		    		
				fileIn.close();
		      }
		    catch(Exception exc)
		      {
		    	System.out.println("Erreur d'ouverture");
		    	
		      }
		    
		}
		else{
			share=true;
			shareRunning=true;
			theme="Aqua";
			background=configPath+"Themes"+fileSeparator+theme+fileSeparator+"background.jpg";
			logPath=configPath+"log"+fileSeparator;
			logAuto=false;
			timestamp=false;
			defaultColor="#FFFFFF";
			login="MadChatteur"+(int)(Math.random()*1000);
			lang="francais";
		}
		
		
		smileyPath=configPath+"Themes"+fileSeparator+theme+fileSeparator+"Smileys"+fileSeparator;
		iconsPath=configPath+"Themes"+fileSeparator+theme+fileSeparator+"Icons"+fileSeparator;
		
		insertSmileys();
		loadLang();
		
		System.out.println("smileyPath="+smileyPath);
		System.out.println("iconsPath="+iconsPath);
		System.out.println("logauto="+logAuto);
		System.out.println("timestamp="+timestamp);
		System.out.println("background="+background);
		System.out.println("color="+defaultColor);
		System.out.println("login="+login);
		System.out.println("themePath= "+themePath);
		
		save();
	}
	
	private void loadLang(){
    	try {
			BufferedReader fileIn = new BufferedReader(new FileReader(configPath+"Lang"+File.separator+lang+".lang"));
			String ligne;
			langProperties.clear();
			
		
				while ((ligne = fileIn.readLine()) != null){
					int pos=ligne.indexOf("=");
					if(pos>-1){
						String param=ligne.substring(0,pos);
						String value=ligne.substring(pos+1);
						System.out.println("LangPropertie : "+param+"="+value);
						langProperties.put(param,value);
					}
				}
				fileIn.close();
		
		} catch (Exception e) {
			System.out.println("Erreur pendant le chargement de "+configPath+"Lang"+File.separator+lang+".lang!");
			
		} 
		
		
	}
	
	public boolean isShareRunning(){
		return shareRunning;
	}
	
	public void setShareRunning(boolean b){
		shareRunning=b;
	}
	
	public Agent getChatAgent() {
		return chatAgent;
	}
	
	public ShareAgent getShareAgent() {
		return shareAgent;
	}

	public void setShareAgent(ShareAgent shareAgent) {
		this.shareAgent = shareAgent;
	}

	public void setSenderAgent(SenderAgent senderAgent) {
		this.senderAgent = senderAgent;
	}

	public SenderAgent getSenderAgent() {
		return senderAgent;
	}

	public void save(){
		try {
			PrintWriter ecrivain =  new PrintWriter(new BufferedWriter(new FileWriter(configPath+configFile)));
			
			ecrivain.println("theme="+theme);
			if(share) ecrivain.println("share=1");
			else ecrivain.println("share=0");
			for(int i=0;i<tabRepertoiresPartages.size();i++){
				ecrivain.println(tabRepertoiresPartages.get(i));
			}
			if(logAuto) ecrivain.println("logauto=1");
			else ecrivain.println("logauto=0");
			if(timestamp) ecrivain.println("timestamp=1");
			else ecrivain.println("timestamp=0");
			ecrivain.println("background="+background);
			ecrivain.println("color="+defaultColor);
			ecrivain.println("login="+login);
			ecrivain.println("logpath="+logPath);
			ecrivain.println("lang="+lang);

			ecrivain.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		if(this.background.equals(background)==false){
				this.background = background;
				fireConfigChanged("background");
		}
	}

	public String getDefaultColor() {
		return defaultColor;
	}

	public void setDefaultColor(String defaultColor) {
		if(this.defaultColor.equals(defaultColor)==false){
			this.defaultColor = defaultColor;
			fireConfigChanged("defaultcolor");
		}
	}
	
	
	
	public boolean isShare() {
		return share;
	}

	public void setShare(boolean share) {
		if(this.share!=share){
			this.share = share;
			fireConfigChanged("share");
		}
	}

	public Vector getTabRepertoiresPartages() {
		return tabRepertoiresPartages;
	}

	public void setTabRepertoiresPartages(Vector tabRepertoiresPartages) {
		this.tabRepertoiresPartages = tabRepertoiresPartages;
	}

	public boolean isLogAuto() {
		return logAuto;
	}

	public void setLogAuto(boolean logAuto) {
		if(this.logAuto!=logAuto){
			this.logAuto = logAuto;
			fireConfigChanged("logauto");
		}
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		if(this.login.equals(login)==false){
			this.login = login;
			fireConfigChanged("login");
		}
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		if(this.theme.equals(theme)==false){
			this.theme = theme;
			smileyPath=configPath+"Themes"+fileSeparator+theme+fileSeparator+"Smileys"+fileSeparator;
			iconsPath=configPath+"Themes"+fileSeparator+theme+fileSeparator+"Icons"+fileSeparator;
			insertSmileys();
			fireConfigChanged("theme");
		}
	}

	public boolean isTimestamp() {
		return timestamp;
	}

	public void setTimestamp(boolean timestamp) {
		if(this.timestamp!=timestamp){
			this.timestamp = timestamp;
			fireConfigChanged("timestamp");
		}
	}

	public String getFileSeparator() {
		return fileSeparator;
	}



	public String getConfigPath() {
		return configPath;
	}

	public String getIconsPath() {
		return iconsPath;
	}

	public String getSmileyPath() {
		return smileyPath;
	}

	public SmileyTab getSmileyTab() {
		return smileyTab;
	}

	public String getOsName() {
		return osName;
	}
	
	
	public String getLogPath() {
		return logPath;
	}

	public void setLogPath(String logPath) {
		if(this.logPath.equals(logPath)==false){
			this.logPath = logPath;
			fireConfigChanged("logpath");
		}
	}

	public String getThemePath(){
		return themePath;
	}
		
	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		if(this.lang.equals(lang)==false){
			this.lang = lang;
			loadLang();
			fireConfigChanged("lang");
		}
	}
	
	public String getLangProperty(String property) {
		System.out.println("getLangProperty("+property+")");
		return (String)langProperties.get(property);
	}

	
	
	public void addConfigChangeListener(ConfigChangeListener c){
		configChangeListeners.add(c);
	}
	public void removeConfigChangeListener(ConfigChangeListener c){
		configChangeListeners.remove(c);
	}
	public void fireConfigChanged(String msg){
		for(int i=0;i<configChangeListeners.size();i++)
			((ConfigChangeListener)configChangeListeners.get(i)).configChanged(msg);
	}
	
	public void insertSmileys(){
		smileyTab.clear();
		smileyTab.insertSmiley(":)",smileyPath+"smile1.gif");
		smileyTab.insertSmiley(":-)",smileyPath+"smile2.gif");
		smileyTab.insertSmiley(":(",smileyPath+"negat1.gif");
		smileyTab.insertSmiley(":-(",smileyPath+"negat2.gif");
		smileyTab.insertSmiley(":'(",smileyPath+"pleure.gif");
		smileyTab.insertSmiley(";)",smileyPath+"oeil1.gif");
		smileyTab.insertSmiley(";-)",smileyPath+"oeil2.gif");
		smileyTab.insertSmiley(":D",smileyPath+"content1.gif");
		smileyTab.insertSmiley(":-D",smileyPath+"content2.gif");
		
		smileyTab.insertSmiley(":S",smileyPath+"mhh2.gif");
		smileyTab.insertSmiley("^^",smileyPath+"huhu.gif");
		smileyTab.insertSmiley("O:)",smileyPath+"ange.gif");
		smileyTab.insertSmiley(":!",smileyPath+"demon.gif");
		smileyTab.insertSmiley(":p",smileyPath+"langue1.gif");
		smileyTab.insertSmiley(":-p",smileyPath+"langue2.gif");
		smileyTab.insertSmiley(":X",smileyPath+"muet.gif");
		smileyTab.insertSmiley("8-)",smileyPath+"lunette.gif");
		smileyTab.insertSmiley(":B",smileyPath+"beer.gif");
	}
	
}
