package madkit.MadChat;

import java.text.SimpleDateFormat;
import java.util.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.GroupIdentifier;
import madkit.kernel.Message;

import java.awt.event.*;
import java.awt.*;
import java.io.*;

public class PvPanel extends JPanel implements ActionListener, KeyListener,MouseListener, ConfigChangeListener {
	
	
	
	// declaration de la zone ou les messages s'affichent
	private HTMLEditor tZoneLecture = null;
	
	// barre d?roulante de la zone de Lecture
	private JScrollPane scrollbarZLecture = null;
	
	// barre d?roulante de la liste des chatters
	private JScrollPane scrollbarZListeChatter = null;
	
	// declaration de la zone d'ecriture
	private JTextField tZoneEcriture = null;
	
	// declaration de la toolbar d'ecriture
	private WriteToolbar tWriteToolbar;
	
	
	private String nomChannel;
	
	private ConfigChan cfgChan;
	
	private Vector historique;	//permet la gestion de l'historique
	private int tailleMaxHistorique;
	private int indexHistorique;

	private Chatter chatter,me;
	
	String background;// background courant
	Config config;
	
	public PvPanel(Config cfg, Chatter c) {
		
		this("$"+c.getNom(),cfg, new ConfigChan("$"+c.getNom(), "Bienvenue sur le salon de discussion privee avec "+c.getNom(), "", false, false));
		chatter = c;
		
	}
	
	public PvPanel(String nom, Config cfg,ConfigChan cfgChan) {
		config=cfg;
		config.addConfigChangeListener(this);		
		historique=new Vector();
		tailleMaxHistorique=20;
		indexHistorique=1;
		
		me=new Chatter(config.getLogin(),false,config.getChatAgent().getAddress());
		
		nomChannel = nom;
		
		this.cfgChan =cfgChan; 
		
		if(config.getOsName().startsWith("Windows"))
			background="<body background=\"file:///"+config.getBackground()+"\">"; // le background par defaut
		else background="<body background=\"file://"+config.getBackground()+"\">"; // le background par defaut		
		
		setLayout(new BorderLayout());
		
		
		tZoneLecture = new HTMLEditor();
		tZoneLecture.setEditable(false);
		tZoneLecture.addMouseListener(this);
		tZoneLecture.insertHTML(background);
		
		JPanel writePanel = new JPanel();
		writePanel.setLayout(new BorderLayout());
		
		tZoneEcriture = new JTextField();
		tZoneEcriture.addKeyListener(this);
		tZoneEcriture.addActionListener(this);
		
		tWriteToolbar = new WriteToolbar(tZoneEcriture, cfg);
		tWriteToolbar.setFloatable(false);
		
		writePanel.add(tWriteToolbar, BorderLayout.NORTH);
		writePanel.add(tZoneEcriture, BorderLayout.SOUTH);
		
		// On attache la 1ere scrollbar ? la zone de lecture
		scrollbarZLecture = new JScrollPane(tZoneLecture,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollbarZLecture.setWheelScrollingEnabled(true);
		add(scrollbarZLecture, BorderLayout.CENTER);
		add(writePanel, BorderLayout.SOUTH);
		
	}

	public void addChatter(Chatter c){
		insertInfoMessage(""+c.getNom()+" vient de se connecter au channel","#0000ff");
	}
	
	public void removeChatter(AgentAddress aa){
		if(getHim() !=null){
			insertInfoMessage(""+getHim().getNom()+" vient de quitter le channel","#ff0000");	
			chatter=null;
		}
	}

	
	public void updateChatterNick(AgentAddress a,String nick){
		if(chatter!=null){
			System.out.println("changement nick");
			String old=getHim().getNom();
			chatter.setNom(nick);
			tZoneLecture.insertHTML("<font color =\"#FF0000\">"+old+" change de nom pour \""+chatter.getNom()+"\" </font>");
			
//			 il faudra aussi changer le nom du channel !
			
			
		}
		
	}
	
	public void quit(){
		//on quitte le channel
		MadChatHello ag=(MadChatHello)config.getChatAgent();
		//ag.leaveGroup("MadChat",getNomChannel());
		
		//et on le dit a tout le monde
		System.out.println("QUIT le channel $"+getMe().getNom());
		CommandMessage m =new CommandMessage(ag.getAddress(),CommandMessage.MSG_QUIT,"$"+getMe().getNom(),"$"+getMe().getNom());
		if(getHim() !=null) config.getChatAgent().sendMessage(getHim().getAdresse(),m);
		
		//pour finir on effectue quelques sauvegardes si necessaire
		if(tWriteToolbar.isSaveLog()){
			try {
				FileWriter fw = new FileWriter(config.getLogPath()+this.getNomChannel()+".log",true);
				fw.write(this.getConversation());
				fw.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
	
	public void join(){		
		//demande la liste des connect?
		MadChatHello ag=(MadChatHello)config.getChatAgent();
		
		//envoie ses informations personnelles a tout les chatters du chan
		Chatter c= new Chatter(config.getLogin(),false,ag.getAddress());
		ChatterMessage m2=new ChatterMessage(c,getNomChannel());
	}
	
	public void insertChatMessage(String sender,String message) {
		if(getHim() != null){
			String couleur = Integer.toHexString(
			tWriteToolbar.getCurrentColor().getRGB()).substring(2);
		
			message = filterHTML(message);
	
			if (config.isTimestamp()) {
				Date d = new Date();
				SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
				tZoneLecture.insertHTML("<font color=\"white\" size=2>&lt;"+sender+" "+ formatter.format(d)
						+ "&gt;</font> <font color=\"#" + couleur + "\" face="
						+ tWriteToolbar.getPolice() + " size=\""
						+ tWriteToolbar.getTaillePolice() + "\">" + message
						+ "</font>");
			} 
			else {
				tZoneLecture.insertHTML("<font color=\"white\" size=2>&lt;"+sender+"&gt;</font><font color=\"#" + couleur + "\" face="
				+ tWriteToolbar.getPolice() + " size=\""
				+ tWriteToolbar.getTaillePolice() + "\">" + message
				+ "</font>");
			}
		System.out.println("Message: "+message);
		}
		else insertInfoMessage("L'utilisateur "+chatter.getNom()+" n'est pas connecte sur ce channel.","\"yellow\"");
	
	}
	
	public void insertInfoMessage(String message,String couleur) {
					
			message = filterHTML(message);
	
				tZoneLecture.insertHTML("<b><font color=\""+couleur+"\">"+message+"</font></b>");
				
		
		System.out.println("Message: "+message);
		
	
	}

	
	private String filterHTML(String text) {
		String result = text.replaceAll("<", "&lt;");
		result = result.replaceAll(">", "&gt;");
		result = result.replaceAll("\\[g\\]", "<b>");
		result = result.replaceAll("\\[/g\\]", "</b>");
		result = result.replaceAll("\\[i\\]", "<i>");
		result = result.replaceAll("\\[/i\\]", "</i>");
				
		SmileyTab smileyTab=config.getSmileyTab();
		String tab[]=smileyTab.getKeys();
		for(int i=0;i<tab.length;i++){
			String smiley=smileyTab.getPathBySmiley(tab[i]);
			if(smiley!=null){
				smiley="<img src=\""+smiley+"\">";
				
				int start = result.indexOf (tab[i]);
				if (start!=-1){
					int lf = tab[i].length();
					char [] targetChars = result.toCharArray();
					StringBuffer buffer = new StringBuffer();
					int copyFrom=0;
					while (start != -1) {
						buffer.append (targetChars, copyFrom, start-copyFrom);
						buffer.append (smiley);
						copyFrom=start+lf;
						start = result.indexOf (tab[i], copyFrom);
					}
					buffer.append (targetChars, copyFrom, targetChars.length-copyFrom);
					result= buffer.toString();
				}
			}
	    }
		
		
		
		
		System.out.println(result);
		// String result=text.replaceAll("[b]","<b>");
		return result;
	}
	
	
	public String getNomChannel() {
		return nomChannel;
	}
	
	public ConfigChan getConfigChan(){
		return cfgChan;
	}
	
	public void setConfigChan(ConfigChan cfgChan) {
		this.cfgChan = cfgChan;
	}

	// retourne tout l contenu de la conversation
	public String getConversation() {
		return tZoneLecture.getText();
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==tZoneEcriture){
			//System.out.println("Utilisateurs sur le salon : "+listeChatter.size());
			//System.out.println(getMe().getNom());
			//System.out.println(getHim().getNom());
			
			String text = tZoneEcriture.getText();
			if(getHim()!= null){
				ChatMessage monmsg=new ChatMessage(config.getLogin(),"$"+getHim().getNom(),text);
				ChatMessage sonmsg=new ChatMessage(config.getLogin(),"$"+getMe().getNom(),text);
				// send a tout le monde
				config.getChatAgent().sendMessage(getMe().getAdresse(),monmsg);
				config.getChatAgent().sendMessage(getHim().getAdresse(),sonmsg);
				//insertMessage("toto",text);	
			
				if(historique.size()==tailleMaxHistorique) historique.remove(historique.size()-1);
				historique.add(0,text);
				indexHistorique=-1;
			}
			else this.insertInfoMessage("La connexion avec votre correspondant a ?t? rompue.","red");
		
			//gstion de l'historique
			tZoneEcriture.setText("");
		}
	}

	public void keyPressed(KeyEvent e) {
		
		if(e.getKeyCode()==KeyEvent.VK_UP) {
			String text;
			System.out.println("Histo : "+indexHistorique+" "+(historique.size()-1));
			if(indexHistorique<historique.size()-1) indexHistorique++;
			text=(String)historique.get(indexHistorique);
			tZoneEcriture.setText(text);
			
			
		}
		else if(e.getKeyCode()==KeyEvent.VK_DOWN){
			String text;
			if(indexHistorique>-1) indexHistorique--;
			if(indexHistorique>-1) text=(String)historique.get(indexHistorique);
			else text="";
			tZoneEcriture.setText(text);
		}
		
	}
	
	public Chatter getMe(){
		return me;
	}
	
	public Chatter getHim(){
		return this.chatter;
	}
	
	public void setHim(Chatter c){
		this.chatter=c;
		insertInfoMessage(chatter.getNom()+" vient de rejoindre le chat prive.","yellow");
	}
	
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseClicked(MouseEvent e) {

		if(e.getSource()==tZoneLecture){
			 if (e.getClickCount() == 2) {
			 	//double click sur la zone de lecture
			 }
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	
	
	public void configChanged(String msg) {
		if(msg.equals("background"))
		{
			String ancien = tZoneLecture.getText();
			tZoneLecture.setText("");
			if(config.getOsName().startsWith("Windows"))
				tZoneLecture.insertHTML("<body background=\"file:///"+config.getBackground()+"\">"); // le background par defaut
			else tZoneLecture.insertHTML("<body background=\"file://"+config.getBackground()+"\">"); // le background par defaut
			//System.out.println(ancien);
			tZoneEcriture.setText("");
			tZoneLecture.insertHTML(ancien);
			
		}
		else if(msg.equals("login")){
			//envoie msg		
				MadChatHello ag=(MadChatHello)config.getChatAgent();
				CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_NICK,getNomChannel(),config.getLogin());
				ag.broadcastMessage("MadChat",getNomChannel(),"chatter",m);
		}
		else if(msg.equals("theme")){
			System.out.println("theme change");
		}
	}
}