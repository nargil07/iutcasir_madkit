package madkit.MadChat;
import javax.swing.*;

import madkit.MadChat.CloseAndMaxTabbedPane.CloseAndMaxTabbedPane;
import madkit.MadChat.CloseAndMaxTabbedPane.CloseListener;
import madkit.MadChat.share.AbstractShareAgent;
import madkit.MadChat.share.ServerPanel;
import madkit.MadChat.share.ShareAgent;
import madkit.MadChat.share.ShareAgentPanel;
import madkit.TreeTools.DirEntry;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;




public class MadChat extends JRootPane implements CloseListener{
	
	public static final String ApplicationImagePath = new File(".").getAbsolutePath();
	
	private CloseAndMaxTabbedPane tabbedChan; 
	private MainMenu mainMenu;
	private MainToolbar mainTool;
	//private ChatPanel chat;
	private JPanel tmpPanel;
	private Config configuration;
	
	
	public MadChat(Config cfg){
		configuration=cfg;
		JPanel contentPane = (JPanel) this.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		tabbedChan =new CloseAndMaxTabbedPane(false);
		tabbedChan.setMaxIcon(false);
		tabbedChan.addCloseListener(this);
		
		
		mainTool =new MainToolbar(this,configuration);
		mainTool.setFloatable(false);
		//chat = new ChatPanel("default",configuration);
		mainMenu = new MainMenu(tabbedChan,configuration);
		mainMenu.setPreferredSize(new Dimension(640,30));
		this.setJMenuBar(mainMenu);
		//tabbedChan.add(chat,"default");
		contentPane.add(mainTool,BorderLayout.NORTH);
		contentPane.add(tabbedChan);
		this.setPreferredSize(new Dimension(640, 480));
			
	}
	

	public void insertChatMsgInChan(String channel,String chatter,String message){
		System.out.println("-------------------------------- index de "+channel+" = ");
		int index = tabbedChan.indexOfTab(channel);
		System.out.println(index);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){		
			if( comp instanceof ChatPanel){
				ChatPanel panel= (ChatPanel)comp;
				panel.insertChatMessage(chatter,message);
			}
			else {
				System.out.println("MadChat : On est dans un PV panel");
				PvPanel panel = (PvPanel)comp;
				panel.insertChatMessage(chatter,message);
			}
		}
	}
	
	public void insertInfoMsgInChan(String channel,String message,String coul){
		System.out.println("-------------------------------- index de "+channel+" = ");
		int index = tabbedChan.indexOfTab(channel);
		System.out.println(index);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){		
			ChatPanel panel= (ChatPanel)comp;
			panel.insertInfoMessage(message,coul);
		}
			
		
	}
	
	public ConfigChan getConfigChan(String chan){ 
		int index = tabbedChan.indexOfTab(chan);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp==null) return null;
		
		
		ChatPanel panel= (ChatPanel)comp;
		
		return panel.getConfigChan();
	}
	public Chatter[] getChatterListInChan(String channel){
		
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp==null) return null;
		
		
		ChatPanel panel= (ChatPanel)comp;
		
		return panel.getChatterList();		
	}
	
	public ChatterList getChatListInChan(String channel){
		
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp==null) return null;
		
		
		ChatPanel panel= (ChatPanel)comp;
		
		return panel.getChatList();		
	}
	
	public void slapInChan(String channel, AgentAddress slappeur, String slappe){
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){	
			ChatPanel panel= (ChatPanel)comp;		
			panel.insertInfoMessage(panel.getChatList().getByAgentAddress(slappeur).getNom() +" envoie une truite dans la gueulle de "+slappe,"green");
		}
	}
	
	public void kickInChan(String channel, AgentAddress slappeur, String slappe){
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){	
			ChatPanel panel= (ChatPanel)comp;		
			panel.insertInfoMessage(slappe +" a ete kicke par "+panel.getChatList().getByAgentAddress(slappeur).getNom(),"red");
		}
	}
	
	public void setChatterListInChan(String channel,Chatter[] m){
	
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){	
			ChatPanel panel= (ChatPanel)comp;		
			panel.setChatterList(m);
		}
	}
	
	public void setChatterNickInChan(String channel,AgentAddress a,String nick){
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		System.out.println("Index de "+channel+ " : "+index + " "+nick);
		if(comp!=null){	
			
			ChatPanel panel= (ChatPanel)comp;		
			panel.updateChatterNick(a,nick);
		}
	}

	public void addChatterInChan(String channel,Chatter c){
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){	
			ChatPanel panel= (ChatPanel)comp;
			
			panel.addChatter(c);
		}
	}
	
	public void removeChatterInChan(String channel,AgentAddress chatter){
		int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){	
			if(comp instanceof ChatPanel) { 
				ChatPanel panel= (ChatPanel)comp;
				panel.removeChatter(chatter);
			}
			else { 
				PvPanel panel= (PvPanel)comp;
				panel.removeChatter(chatter);
			}
			
		}
	}
	
	public void setOP(String nomChan, AgentAddress sender, String nomChatter, boolean op){
		int index = tabbedChan.indexOfTab(nomChan);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){	
			ChatPanel panel= (ChatPanel)comp;		
			panel.getChatList().getByLogin(nomChatter).setOp(op);
			System.out.println(nomChatter + " est maintenant OP "+ op);
			if(op) panel.insertInfoMessage(panel.getChatList().getByAgentAddress(sender).getNom() +" donne le status d'operateur a "+ nomChatter,"#000000");
			else panel.insertInfoMessage(panel.getChatList().getByAgentAddress(sender).getNom() +" enleve le status d'operateur a "+ nomChatter,"#000000");
		}
	}
	
	public void closeAllChannel(){
		Component[]  tabComponents= tabbedChan.getComponents();
		for(int i=0;i<tabbedChan.getTabCount();i++){
			
			Component comp= tabbedChan.getComponentAt(i);
			if(comp instanceof ChatPanel){
				ChatPanel chan= (ChatPanel)comp;
				chan.quit();
			
			}
		}
	}

   	public void addChan(String nom){
   		ChatPanel c=new ChatPanel(nom, configuration);
   		tabbedChan.addTab(nom,c);
   		c.join();
		
   	}
   	
	public void addChanPV(Config config, Chatter c){
		PvPanel pvp = new PvPanel(config, c);
   		tabbedChan.addTab("$"+c.getNom(),pvp);
   		//pvp.join();
		
   	}
 	public void addChan(String nom,String pass){
 		ConfigChan confChan=new ConfigChan(nom,"",pass,false,false);
   		ChatPanel c=new ChatPanel(nom, configuration,confChan);
   		tabbedChan.addTab(nom,c);
   		c.join();
		
   	}
   	
   	public void addServerPanel(DirEntry entry, AgentAddress serverAddress){
    	
		JPanel pane = new ServerPanel(this, entry, (AbstractShareAgent)configuration.getShareAgent(), serverAddress);
		tabbedChan.addTab("%"+serverAddress.getKernel().getHost().toString(), pane);
		repaint();
    }
   	
    public void removeServerPanel(){
    	tabbedChan.removeTabAt(tabbedChan.getSelectedIndex());
    	repaint();
    }
   	
    public ConfigChan getCurrentConfigChan(){
    	//int index = tabbedChan.indexOfTab(channel);
    	Component comp= tabbedChan.getSelectedComponent();
    	
		//System.out.println("Index de "+channel+ " : "+index + " "+nick);
		if(comp!=null && comp instanceof ChatPanel){	
			
			ChatPanel panel= (ChatPanel)comp;		
			return panel.getConfigChan();
		}
		return null;
    }
    
    public ChatPanel getCurrentChan(){
    	//int index = tabbedChan.indexOfTab(channel);
    	Component comp= tabbedChan.getSelectedComponent();
    	
		//System.out.println("Index de "+channel+ " : "+index + " "+nick);
		if(comp!=null && comp instanceof ChatPanel){	
			
			ChatPanel panel= (ChatPanel)comp;		
			return panel;
		}
		return null;
    }
    
    public void setConfigInChan(String channel,ConfigChan c){
    	int index = tabbedChan.indexOfTab(channel);
		Component comp= tabbedChan.getComponentAt(index);
		if(comp!=null){	
			ChatPanel panel= (ChatPanel)comp;		
			panel.setConfigChan(c);
		}
    }
    
   	public String[] getAllChan(){
   		String[] listeChan = new String[tabbedChan.getTabCount()];
   		
   		for(int i=0; i<tabbedChan.getTabCount();i++){
   			listeChan[i] = tabbedChan.getTitleAt(i);
   			System.out.println("tab "+i+" : "+listeChan[i]);
   		}
   		return listeChan;
   	}
   	
   	public boolean isChan(String[] chanlist, String nom){
   		boolean bool=false;
   		for (int i=0; i< tabbedChan.getTabCount(); i++){
   			if(chanlist[i].equals(nom)) bool=true;
   		}
   		return bool;
   	}
   	
   	public boolean existChan(String nomChan){
   		boolean bool=false;
   		for (int i=0; i< tabbedChan.getTabCount(); i++){
   			System.out.println("nom du chan : "+tabbedChan.getTitleAt(i));
   			if(tabbedChan.getTitleAt(i).equals(nomChan)) bool=true;
   		}
   		return bool;
   	}
	
   	public JPanel getChan(String nomChan){
   		
   		for (int i=0; i< tabbedChan.getTabCount(); i++){
   			System.out.println("nom du chan : "+tabbedChan.getTitleAt(i));
   			if(tabbedChan.getTitleAt(i).equals(nomChan)) return (JPanel)tabbedChan.getComponentAt(i);
   		}
   		
   		return null;
   	}



	public void closeOperation(MouseEvent e) {
		Component c=tabbedChan.getSelectedComponent();
		if(c instanceof ChatPanel){
			ChatPanel chan=(ChatPanel)c;
			if(chan.getNomChannel().equals("default")==false){
				chan.quit();
				tabbedChan.remove(tabbedChan.getSelectedIndex());
			
			}
			
		}
		else if(c instanceof PvPanel){
			PvPanel chan=(PvPanel)c;
				chan.quit();
				tabbedChan.remove(tabbedChan.getSelectedIndex());
			
			}
		else tabbedChan.remove(tabbedChan.getSelectedIndex());
	}
	
	
}
