package madkit.MadChat;
import javax.swing.*;
import javax.swing.border.*;

import madkit.MadChat.share.SenderAgent;
import madkit.MadChat.share.SenderAgentPanel;

import java.awt.*;
import java.awt.event.*;
import java.net.URL;

public class MainToolbar extends JToolBar implements ActionListener,ConfigChangeListener{
	private JButton bKick;
	private JButton bIgnore;
	private JButton bPM;
	private JButton bTopic;
	private JButton bList;
	private JButton bJoin;
	private JButton bConfig;
	private JButton bShare;
	private JButton bAffichShare;
	private MadChat  chat;
	private ConfigChan cfgChan;
	protected String path;
    Config config;
	
   public MainToolbar(MadChat m, Config cfg){
	   super();
	  config=cfg;
	  chat= m;
		System.out.println("=======>"+config.getIconsPath());
		
		if(config.isShare()){
			bShare=new JButton(new ImageIcon(config.getIconsPath()+"server_on.gif"));
			bShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_STOP_TOOLTIP")); 
			bShare.setActionCommand("1");
		}
		else {
			bShare=new JButton(new ImageIcon(config.getIconsPath()+"server_off.gif"));
			bShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_START_TOOLTIP")); 
			bShare.setActionCommand("0");	
		}
		bShare.setBorderPainted(false);
		bShare.addActionListener(this); 
		
		bAffichShare=new JButton(new ImageIcon(config.getIconsPath()+"server.gif"));
		bAffichShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_CONFIG_TOOLTIP")); 
		bAffichShare.setBorderPainted(false);
		bAffichShare.addActionListener(this);
		
		bKick =new JButton(new ImageIcon(config.getIconsPath()+"kick.gif"));
		bKick.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_KICK_TOOLTIP"));
		bKick.setBorderPainted(false);
			
		bIgnore  =new JButton(new ImageIcon(config.getIconsPath()+"ban.gif"));
	    //bBan.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
		bIgnore.setBorderPainted(false);
		bIgnore.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_IGNORE_TOOLTIP"));
		bIgnore.addActionListener(this); 
	    
	    bJoin =new JButton(new ImageIcon(config.getIconsPath()+"join.gif"));
	    bJoin.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_JOIN_TOOLTIP"));
	    bJoin.setBorderPainted(false);
	    bJoin.addActionListener(this); 
	    
	    bList =new JButton(new ImageIcon(config.getIconsPath()+"liste.gif"));
	    bList.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_CHANLIST_TOOLTIP"));
	    bList.setBorderPainted(false);
	    bList.addActionListener(this);
	    
	    bPM= new JButton(new ImageIcon(config.getIconsPath()+"pm.gif"));
	    bPM.setBorderPainted(false);
	    bPM.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_PM_TOOLTIP"));
	    bPM.addActionListener(this);

		bTopic=new JButton(new ImageIcon(config.getIconsPath()+"topic.gif"));
		bTopic.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_TOPIC_TOOLTIP"));
		bTopic.setBorderPainted(false);
		bTopic.addActionListener(this);
		
		bConfig= new JButton(new ImageIcon(config.getIconsPath()+"config.gif"));
		bConfig.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_CONFIG_TOOLTIP"));
		bConfig.setBorderPainted(false);
	    bConfig.addActionListener(this);
	    
		add(bJoin);
		
		add(bList);
		//addSeparator(new Dimension(1,20));
		add(bTopic);
		addSeparator(new Dimension(20,50));
						
		add(bIgnore);
		//addSeparator(new Dimension(1,20));
		add(bKick);
		addSeparator(new Dimension(20,50));
		
		add(bPM);
		addSeparator(new Dimension(20,50));
		
		add(bShare);
		//addSeparator(new Dimension(1,20));
		add(bAffichShare);
		//addSeparator(new Dimension(1,20));
		add(bConfig);
		config.addConfigChangeListener(this);
		
		
		
		
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==bJoin){
			//String ans = JOptionPane.showInputDialog(null,"Nom du salon de discussion");			
			PanneauJoin fjoin=new PanneauJoin(config);
			
			fjoin.pack();
			fjoin.setTitle(config.getLangProperty("PANEL_JOIN_TITLE"));
			fjoin.setVisible(true);
			/*
			*/
		}
		else if (e.getSource()==bShare){
			if(e.getActionCommand().equals("1")){
				bShare.setActionCommand("0");
				System.out.println("===========================> coucou on arete le share");
				//config.setShare(false);
				config.getChatAgent().killAgent(config.getSenderAgent());
				config.setSenderAgent(new SenderAgent());
				config.setShareRunning(false);
				bShare.setIcon(new ImageIcon(config.getIconsPath()+"server_off.gif"));
				bShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_START_TOOLTIP")); 
			}
			else{
				bShare.setActionCommand("1");
				System.out.println("===========================> coucou on lance le share");
				//config.setShare(true);
				config.setShareRunning(true);
				bShare.setIcon(new ImageIcon(config.getIconsPath()+"server_on.gif"));
				bShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_STOP_TOOLTIP")); 
				config.getChatAgent().launchAgent(config.getSenderAgent(),"serveur",false);
			}
		}
		else if(e.getSource()==bAffichShare){
			JFrame frame=new JFrame();
			frame.getContentPane().add(config.getSenderAgent().getDisplay());
			
			frame.pack();
			frame.setVisible(true);
			System.out.println("maintool");
		}
		else if (e.getSource() == bConfig) {
			PanneauConfig panneauConf = new PanneauConfig(config);
			panneauConf.setSize(450,200);
			panneauConf.setTitle(config.getLangProperty("PANEL_CONF_TITLE"));
			panneauConf.setVisible(true);
			}
		else if (e.getSource() == bList) {
			JDialogChanList listeChan = new JDialogChanList(chat, config);
			listeChan.setSize(500,250);
			listeChan.setTitle(config.getLangProperty("PANEL_CHANLIST_TITLE"));
			listeChan.setVisible(true);
			}
		else if (e.getSource() == bTopic) {
			MadChatHello madAg= (MadChatHello)config.getChatAgent();
			Component comp = madAg.getDisplay().getCurrentChan();
			if(comp instanceof ChatPanel){
				cfgChan=chat.getCurrentConfigChan();
				Chatter c=(Chatter)chat.getCurrentChan().getMe();
				PanneauTopic pt = new PanneauTopic(config, cfgChan,c.isOp());
				pt.pack();
				pt.setTitle(config.getLangProperty("PANEL_TOPIC_TITLE")+ cfgChan.getNomChannel());
				pt.setVisible(true);
			}
		}
		else if (e.getSource()==bIgnore){
			MadChatHello madAg= (MadChatHello)config.getChatAgent();
			Component comp = madAg.getDisplay().getCurrentChan();
			if(comp instanceof ChatPanel){
				((ChatPanel)comp).gestionIgnored();
			}
			
		}
		else if (e.getSource()==bPM){
			MadChatHello madAg= (MadChatHello)config.getChatAgent();
			ChatterList liste = madAg.getDisplay().getChatListInChan("default");
			
			PanneauPM panelPM = new PanneauPM(config, liste);
			panelPM.setTitle(config.getLangProperty("PANEL_PM_TITLE"));
			panelPM.setVisible(true);
		}
	}

	public void configChanged(String msg) {
		if(msg.equals("theme")){
			bKick.setIcon(new ImageIcon(config.getIconsPath()+"kick.gif"));
			bIgnore.setIcon(new ImageIcon(config.getIconsPath()+"ban.gif"));
			bList.setIcon(new ImageIcon(config.getIconsPath()+"liste.gif"));
			bPM.setIcon(new ImageIcon(config.getIconsPath()+"pm.gif"));
			bTopic.setIcon(new ImageIcon(config.getIconsPath()+"topic.gif"));
			//bNew.setIcon(new ImageIcon(config.getIconsPath()+"new.gif"));
			bJoin.setIcon(new ImageIcon(config.getIconsPath()+"join.gif"));
			bAffichShare.setIcon(new ImageIcon(config.getIconsPath()+"server.gif"));
		}
		else if(msg.equals("lang")){
			if(config.isShare()){
				bShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_STOP_TOOLTIP")); 
				}
			else {
				bShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_START_TOOLTIP")); 	
			}
			bKick.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_KICK_TOOLTIP"));
			bIgnore.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_IGNORE_TOOLTIP"));
			bPM.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_PM_TOOLTIP"));
			bTopic.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_TOPIC_TOOLTIP"));
			bList.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_CHANLIST_TOOLTIP"));;
			bJoin.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_JOIN_TOOLTIP"));
			bConfig.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_CONFIG_TOOLTIP"));;
			bAffichShare.setToolTipText(config.getLangProperty("MAIN_TOOLBAR_SHARE_CONFIG_TOOLTIP"));;
		}
		
	}

	
}
