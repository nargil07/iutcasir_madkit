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

public class ChatPanel extends JPanel implements ActionListener, KeyListener,MouseListener, ConfigChangeListener {
	
	
	
	// declaration de la zone ou les messages s'affichent
	private HTMLEditor tZoneLecture = null;
	
	// barre d?roulante de la zone de Lecture
	private JScrollPane scrollbarZLecture = null;
	
	// barre d?roulante de la liste des chatters
	private JScrollPane scrollbarZListeChatter = null;
	
	// declaration de la zone d'ecriture
	private JTextField tZoneEcriture = null;
	
	// declaration de la liste des Chatter
	private JList lZoneListeChatter = null;
	

	
	// declaration de la toolbar d'ecriture
	private WriteToolbar tWriteToolbar;
	
	
	private String nomChannel;
	
	private ConfigChan cfgChan;
	
	private Vector historique;	//permet la gestion de l'historique
	private int tailleMaxHistorique;
	private int indexHistorique;
	
	private ChatterList listeIgnored;
	private JPopupMenu popup;
	
	private ChatterList listeChatter;
	private JPopupMenu p;

	private JMenuItem menuIgnore;
	private JMenuItem menuKick;
	private JMenuItem menuOP;
	private JMenuItem menuDEOP;
	private JMenuItem menuSlap;
	private JMenuItem menuPing;
	private JMenuItem menuWhois;
	
	String background;// background courant
	Config config;
	
	public ChatPanel(String nom, Config cfg) {
		
		this(nom,cfg, new ConfigChan(nom, "Bienvenue sur le salon : "+nom, "", false, false));
		

	}
	
	public ChatPanel(String nom, Config cfg,ConfigChan cfgChan) {
		config=cfg;
		config.addConfigChangeListener(this);		
		historique=new Vector();
		tailleMaxHistorique=20;
		indexHistorique=1;
		
		nomChannel = nom;
		
		this.cfgChan =cfgChan; 
		listeChatter=new ChatterList();
		listeIgnored=new ChatterList();
		
		if(config.getOsName().startsWith("Windows"))
			background="<body background=\"file:///"+config.getBackground()+"\">"; // le background par defaut
		else background="<body background=\"file://"+config.getBackground()+"\">"; // le background par defaut		
		
		setLayout(new BorderLayout());
		
		popup = new JPopupMenu();
		menuIgnore = new JMenuItem("Ignorer/Accepter");
		menuIgnore.addActionListener(this); 
		menuKick = new JMenuItem("Kicker");
		menuKick.addActionListener(this);
		menuOP = new JMenuItem("Op");
		menuOP.addActionListener(this);
		menuDEOP = new JMenuItem("Deop");
		menuDEOP.addActionListener(this);
		menuSlap = new JMenuItem("Slap !");
		menuSlap.addActionListener(this);
		menuPing = new JMenuItem("Ping");
		menuPing.addActionListener(this);
		menuWhois = new JMenuItem("Whois");
		menuWhois.addActionListener(this);
		
		popup.add(menuOP);
		popup.add(menuDEOP);
		popup.addSeparator();
		popup.add(menuKick);
		popup.add(menuIgnore);
		popup.addSeparator();
		popup.add(menuSlap);
		popup.addSeparator();
		popup.add(menuPing);
		popup.add(menuWhois);
		
		
		tZoneLecture = new HTMLEditor();
		tZoneLecture.setEditable(false);
		tZoneLecture.addMouseListener(this);
		tZoneLecture.insertHTML(background);
		tZoneLecture.insertHTML("<font color =\"#3355AA\"> Welcome in MadChat ! Type /help to get command list :)</font>");
		//System.out.println(background);
		
		lZoneListeChatter = new JList();
		lZoneListeChatter.setFixedCellWidth(150);
		
		lZoneListeChatter.addMouseListener(this);
		
		
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
		
		// On attache la 2eme scrollbar ? la liste des chatters
		scrollbarZListeChatter = new JScrollPane(lZoneListeChatter);
		scrollbarZListeChatter.setWheelScrollingEnabled(true);
		add(scrollbarZListeChatter, BorderLayout.EAST);
	}
	
	public void setChatterList(Chatter[] c){
		if(c!=null){
			for(int i=0;i<c.length;i++)
				listeChatter.add(c[i]);
			lZoneListeChatter.removeAll();
			lZoneListeChatter.setListData(listeChatter);
		}
		else System.out.println("pb list chatter");
	}
	
	public void addChatter(Chatter c){
		listeChatter.add(c);
		lZoneListeChatter.setListData(listeChatter);
		insertInfoMessage(""+c.getNom()+" vient de se connecter au channel","#0000ff");
	}
	
	public void removeChatter(AgentAddress aa){
		Chatter c =listeChatter.getByAgentAddress(aa);
		if(c!=null){
			lZoneListeChatter.setListData(listeChatter);
			insertInfoMessage(""+c.getNom()+" vient de quitter le channel","#ff0000");
			listeChatter.RemoveByAgentAddress(aa);
			listeIgnored.RemoveByAgentAddress(aa);
			lZoneListeChatter.setListData(listeChatter);
			
		}
	}
	
	public int getChatterCount(){
		return listeChatter.size();
	}
	
	
	public ChatterList getListeIgnored() {
		return listeIgnored;
	}

	public void setListeIgnored(ChatterList listeIgnored) {
		this.listeIgnored = listeIgnored;
	}

	
	
	public Chatter[] getChatterList(){
		
		Chatter[] res=new Chatter[listeChatter.size()];
		for(int i=0;i<listeChatter.size();i++)
			res[i]=(Chatter)listeChatter.get(i);
		return res;		                          
	}
	
	public ChatterList getChatList(){
		return this.listeChatter;
	}
	
	public void updateChatterNick(AgentAddress a,String nick){
		
		Chatter c=listeChatter.getByAgentAddress(a);
		if(c!=null){
			System.out.println("changement nick");
			String old=c.getNom();
			c.setNom(nick);
			lZoneListeChatter.setListData(listeChatter);
			tZoneLecture.insertHTML("<font color =\"#FF0000\">"+old+" change de nom pour \""+c.getNom()+"\" </font>");
		}
		
	}
	
	public void quit(){
		//on quitte le channel
		MadChatHello ag=(MadChatHello)config.getChatAgent();
		ag.leaveGroup("MadChat",getNomChannel());
		
		//et on le dit a tout le monde
		CommandMessage m =new CommandMessage(ag.getAddress(),CommandMessage.MSG_QUIT,getNomChannel(),config.getLogin());
		config.getChatAgent().broadcastMessage("MadChat",getNomChannel(),"chatter",m);
		
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
		//on s'incrit dans le channel
		config.getChatAgent().requestRole("MadChat",getNomChannel(),"chatter",null);
		
		//demande la liste des connect?
		MadChatHello ag=(MadChatHello)config.getChatAgent();
		CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.REQ_LIST,getNomChannel(),"");
		ag.sendMessageToAnyone("MadChat",getNomChannel(),"chatter",m);
		
		//envoie ses informations personnelles a tout les chatters du chan
		Chatter c= new Chatter(config.getLogin(),false,ag.getAddress());
		if(ag.getAgentsWithRole("MadChat",getNomChannel(),"chatter").length==1) c.setOp(true);
		ChatterMessage m2=new ChatterMessage(c,getNomChannel());
		ag.broadcastMessage("MadChat",getNomChannel(),"chatter",m2);
	}
	
	public void insertChatMessage(String sender,String message) {
		Chatter c= listeIgnored.getByLogin(sender);
		if(c==null){
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
	
	public void gestionIgnored(){
		
		
		PanneauIgnored pI=new PanneauIgnored(config,this.listeChatter,this.listeIgnored);
	}
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==tZoneEcriture){
			String text = tZoneEcriture.getText();
			ChatMessage msg=new ChatMessage(config.getLogin(),getNomChannel(),text);
		
			// send a tout le monde
			config.getChatAgent().broadcastMessage("MadChat",getNomChannel(),"chatter",msg);
			//insertMessage("toto",text);	
		
			if(historique.size()==tailleMaxHistorique) historique.remove(historique.size()-1);
			historique.add(0,text);
			indexHistorique=-1;
		
			//gstion de l'historique
			tZoneEcriture.setText("");
		}
		else if(e.getSource()== menuOP){
			if(!lZoneListeChatter.isSelectionEmpty()){
				if(getMe().isOp()){
					if(!((Chatter)lZoneListeChatter.getSelectedValue()).isOp()){
						MadChatHello ag=(MadChatHello)config.getChatAgent();
						CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_OP,ag.getDisplay().getCurrentChan().getNomChannel(),((Chatter)lZoneListeChatter.getSelectedValue()).getNom());
						ag.broadcastMessage("MadChat",ag.getDisplay().getCurrentChan().getNomChannel(),"chatter",m);	
					}
					else insertInfoMessage("OP failed : "+((Chatter)lZoneListeChatter.getSelectedValue()).getNom()+" est deja Operateur !","orange");
				}
				else insertInfoMessage("OP failed : Vous n'etes pas OP !", "red");
			}
		}
		else if(e.getSource()== menuDEOP){
			if(!lZoneListeChatter.isSelectionEmpty()){
				if(getMe().isOp()){
					if(((Chatter)lZoneListeChatter.getSelectedValue()).isOp()){
						MadChatHello ag=(MadChatHello)config.getChatAgent();
						CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_DEOP,ag.getDisplay().getCurrentChan().getNomChannel(),((Chatter)lZoneListeChatter.getSelectedValue()).getNom());
						ag.broadcastMessage("MadChat",ag.getDisplay().getCurrentChan().getNomChannel(),"chatter",m);
					}
					else insertInfoMessage("DEOP failed :"+((Chatter)lZoneListeChatter.getSelectedValue()).getNom()+" n'est pas Operateur !","orange");
				}
				else insertInfoMessage("DEOP failed : Vous n'etes pas OP !", "red");
			}
		}
		else if(e.getSource()== menuSlap){
			MadChatHello ag=(MadChatHello)config.getChatAgent();
			CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_SLAP,ag.getDisplay().getCurrentChan().getNomChannel(),((Chatter)lZoneListeChatter.getSelectedValue()).getNom());
			ag.broadcastMessage("MadChat",ag.getDisplay().getCurrentChan().getNomChannel(),"chatter",m);
		}
		else if(e.getSource()== menuKick){
			MadChatHello ag=(MadChatHello)config.getChatAgent();
			CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_KICK,ag.getDisplay().getCurrentChan().getNomChannel(),((Chatter)lZoneListeChatter.getSelectedValue()).getNom());
			ag.broadcastMessage("MadChat",ag.getDisplay().getCurrentChan().getNomChannel(),"chatter",m);
		}
		else if(e.getSource()== menuPing){
			if(!lZoneListeChatter.isSelectionEmpty()){
				MadChatHello ag=(MadChatHello)config.getChatAgent();
				Date d = new Date();
				System.out.println("Time : "+d.getTime());
				CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_PING,ag.getDisplay().getCurrentChan().getNomChannel(),d.getTime());
				ag.sendMessage(((Chatter)lZoneListeChatter.getSelectedValue()).getAdresse(), m);
			}
		}
		else if(e.getSource()== menuWhois){
			if(!lZoneListeChatter.isSelectionEmpty()){
				MadChatHello ag=(MadChatHello)config.getChatAgent();
				CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_WHOIS,ag.getDisplay().getCurrentChan().getNomChannel(),((Chatter)lZoneListeChatter.getSelectedValue()).getNom());
				ag.sendMessage(((Chatter)lZoneListeChatter.getSelectedValue()).getAdresse(), m);
			}
		}		
		else if(e.getSource()== menuIgnore){
			Chatter c= (Chatter)lZoneListeChatter.getSelectedValue();
			//System.out.println("nomchatter "+c.getNom());
			//System.out.println("nomchatter2 "+config.getLogin());
			if(c!=null){
				
				if(!c.getNom().equals(config.getLogin())){
					System.out.println("ds la liste "+listeIgnored.isInList(c.getNom()));
					if(!listeIgnored.isInList(c.getNom())){
						listeIgnored.add(c);
						insertInfoMessage(c.getNom()+" est maintenant inscrit sur la liste des ignores, vous ne recevrez plus ses messages.","yellow");
						//envoyer un message a l'autre
					}
					else{
						listeIgnored.remove(c);
						insertInfoMessage(c.getNom()+" a ete enleve de la liste des ignores, vous recevrez maintenant ses messages.","yellow");
//						envoyer un message a l'autre
					}
				}
				else insertInfoMessage("Vous ne pouvez pas vous ignorer vous meme.","yellow");
			}
		
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
		return listeChatter.getByLogin(config.getLogin());
	}
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==lZoneListeChatter){
			JList list = (JList)e.getSource();
			Chatter c;
			CommandMessage cmdMsg;
			if (e.getClickCount() == 2) {          // Double-click
				// Get item index
				int index = list.locationToIndex(e.getPoint());
				c=(Chatter)list.getSelectedValue();
				cmdMsg=new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.REQ_SHARE_SRV,getNomChannel(),null);
				config.getChatAgent().sendMessage(c.getAdresse(),cmdMsg);
        	}
			else if(SwingUtilities.isRightMouseButton(e)){
				try
				{
					Robot robot = new java.awt.Robot();
					robot.mousePress(InputEvent.BUTTON1_MASK);
					robot.mouseRelease(InputEvent.BUTTON1_MASK);
				}
				catch (AWTException ae) {}
			}
        	
        }
	
		else if(e.getSource()==tZoneLecture){
			 if (e.getClickCount() == 2) {
			 	//PanneauTopic
				Chatter c=listeChatter.getByLogin(config.getLogin());
			 	PanneauTopic pt = new PanneauTopic(config, this.getConfigChan(),c.isOp());
			 	pt.pack();
			 	pt.setTitle("Informations de "+nomChannel);
			 	pt.show();
			 }
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if(e.getSource()==lZoneListeChatter){
			if (e.isPopupTrigger())
			{
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
			else if(SwingUtilities.isRightMouseButton(e)) popup.show(e.getComponent(), e.getX(), e.getY());
		}
		
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