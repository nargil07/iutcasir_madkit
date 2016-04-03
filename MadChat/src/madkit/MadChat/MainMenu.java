package madkit.MadChat;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class MainMenu extends JMenuBar implements ActionListener,ConfigChangeListener {

	//Where the GUI is created:

	private JMenu menuFile;
	private JMenuItem menuParam;
	private JCheckBoxMenuItem cbMenuTimestamp;
	private JCheckBoxMenuItem cbMenuLog;
	private JMenuItem menuLogExplorer;
	private JMenuItem menuQuit;
	
	private JMenu menuAction;
	private JMenuItem menuPing;
	private JMenuItem menuWhois;
	private JMenuItem menuKick;
	private JMenuItem menuOP;
	private JMenuItem menuDEOP;
	
	private JMenu menuHelp;
	private JMenuItem menuCommand;
	private JMenuItem menuWebsite;
	private JMenuItem menuAbout;
	
	private JMenu menuShare;
	private JMenuItem menuPartage;
	private JCheckBoxMenuItem cbLancementServ;
	


	private JTabbedPane tabChan;
	private Config config;

	public MainMenu(JTabbedPane P,Config cfg) {
		config=cfg;
		tabChan = P;
		config.addConfigChangeListener(this);
		
		// MENU FICHIER
		menuFile = new JMenu(config.getLangProperty("MAIN_MENU_FILE"));
		menuFile.setMnemonic(KeyEvent.VK_F);
		add(menuFile);

		//Fichier - Parametres
		menuParam = new JMenuItem(config.getLangProperty("MAIN_MENU_FILE_CONFIG"), new ImageIcon("icons/menu_ico.gif"));
		menuParam.setMnemonic(KeyEvent.VK_F1);
		menuParam.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1,
				ActionEvent.CTRL_MASK));
		menuParam.addActionListener(this);
		menuParam.getAccessibleContext().setAccessibleDescription("Reglage des parametres");
		menuFile.add(menuParam);

		// Fichier - timestamp
		menuFile.addSeparator();
		cbMenuTimestamp = new JCheckBoxMenuItem(config.getLangProperty("MAIN_MENU_FILE_TIMESTAMP"));
		cbMenuTimestamp.setSelected(config.isTimestamp());
		cbMenuTimestamp.addActionListener(this);
		menuFile.add(cbMenuTimestamp);

		// Fichier - Log
		cbMenuLog = new JCheckBoxMenuItem(config.getLangProperty("MAIN_MENU_FILE_LOGAUTO"));
		cbMenuLog.setSelected(config.isLogAuto());
		cbMenuLog.addActionListener(this);
		menuFile.add(cbMenuLog);
		
//		 Fichier - LogExplorer
		menuLogExplorer = new JMenuItem(config.getLangProperty("MAIN_MENU_FILE_LOGVIEW"));
		menuLogExplorer.addActionListener(this);
		menuFile.add(menuLogExplorer);
		
		menuFile.addSeparator();
		
		// Fichier - Deconnect
		menuQuit = new JMenuItem(config.getLangProperty("MAIN_MENU_FILE_QUIT"), new ImageIcon(
				"icons/menu_ico.gif"));
		menuQuit.setMnemonic(KeyEvent.VK_Q);
		menuQuit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				ActionEvent.CTRL_MASK));
		menuQuit.addActionListener(this);
		menuFile.add(menuQuit);
	

		
/* MENU ACTION */
		//Build second menu in the menu bar.
		menuAction = new JMenu(config.getLangProperty("MAIN_MENU_ACTION"));
		menuAction.setMnemonic(KeyEvent.VK_A);

		//Action - Ping
		menuPing = new JMenuItem(config.getLangProperty("MAIN_MENU_ACTION_PING"), new ImageIcon("icons/menu_ico.gif"));
		menuPing.setMnemonic(KeyEvent.VK_P);
		menuPing.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				ActionEvent.CTRL_MASK));
		menuAction.add(menuPing);
		
		//Action - Whois
		menuWhois = new JMenuItem(config.getLangProperty("MAIN_MENU_ACTION_WHOIS"), new ImageIcon("icons/menu_ico.gif"));
		menuWhois.setMnemonic(KeyEvent.VK_W);
		menuWhois.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W,
				ActionEvent.CTRL_MASK));
		menuAction.add(menuWhois);		
		
		menuAction.addSeparator();
		
		//Action - Op
		menuOP = new JMenuItem(config.getLangProperty("MAIN_MENU_ACTION_OP"), new ImageIcon("icons/menu_ico.gif"));
		menuOP.setMnemonic(KeyEvent.VK_PLUS);
		menuOP.addActionListener(this);
		menuOP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
				ActionEvent.CTRL_MASK));
		menuAction.add(menuOP);
		
		//Action - Deop
		menuDEOP = new JMenuItem(config.getLangProperty("MAIN_MENU_ACTION_DEOP"), new ImageIcon("icons/menu_ico.gif"));
		menuDEOP.setMnemonic(KeyEvent.VK_MINUS);
		menuDEOP.addActionListener(this);
		menuDEOP.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
				ActionEvent.CTRL_MASK));
		menuAction.add(menuDEOP);
		
		menuAction.addSeparator();
		
		//Action - Kick
		menuKick = new JMenuItem(config.getLangProperty("MAIN_MENU_ACTION_KICK"), new ImageIcon("icons/menu_ico.gif"));
		menuKick.setMnemonic(KeyEvent.VK_K);
		menuKick.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K,
				ActionEvent.CTRL_MASK));
		menuAction.add(menuKick);

		add(menuAction);

/* MENU PARTAGE */
		//Build third menu in the menu bar.
		menuShare = new JMenu(config.getLangProperty("MAIN_MENU_SHARE"));
		menuShare.setMnemonic(KeyEvent.VK_P);

		
		//Partage - Lancement serveur
		cbLancementServ = new JCheckBoxMenuItem(config.getLangProperty("MAIN_MENU_SHARE_SHAREAUTO"));
		cbLancementServ.setSelected(config.isShare());
		cbLancementServ.addActionListener(this);
		cbLancementServ.setMnemonic(KeyEvent.VK_L);
		cbLancementServ.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				ActionEvent.CTRL_MASK));
		
				
		menuShare.add(cbLancementServ);
		
		menuShare.addSeparator();
		
		//Partage - Config serveur
		menuPartage = new JMenuItem(config.getLangProperty("MAIN_MENU_SHARE_CONFIG"));
		menuPartage.addActionListener(this);
		menuPartage.setMnemonic(KeyEvent.VK_F);
		menuPartage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				ActionEvent.CTRL_MASK));
		
		
		
		menuShare.add(menuPartage);
		
		
		add(menuShare);
		
		
/* MENU HELP */
		//Build third menu in the menu bar.
		menuHelp = new JMenu(config.getLangProperty("MAIN_MENU_HELP"));
		menuHelp.setMnemonic(KeyEvent.VK_H);

		menuHelp.getAccessibleContext().setAccessibleDescription(
				"This menu does nothing");

		//Action - Commandes
		menuCommand = new JMenuItem(config.getLangProperty("MAIN_MENU_HELP_COMMAND"), new ImageIcon("icons/menu_ico.gif"));
		menuCommand.getAccessibleContext().setAccessibleDescription("Afficher les commandes");
		menuHelp.add(menuCommand);
		
		//Aide - Website
		menuWebsite = new JMenuItem(config.getLangProperty("MAIN_MENU_HELP_WEBSITE"), new ImageIcon("icons/menu_ico.gif"));
		menuWebsite.getAccessibleContext().setAccessibleDescription("Aller sur le site web");
		menuHelp.add(menuWebsite);

		//Aide - Version
		menuAbout = new JMenuItem(config.getLangProperty("MAIN_MENU_HELP_ABOUT"), new ImageIcon("icons/menu_ico.gif"));
		menuAbout.getAccessibleContext().setAccessibleDescription("Afficher la version du projet");
		menuHelp.add(menuAbout);
		
		add(menuHelp);

	}

	
/* ACTION PERFORMED */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == cbMenuLog) {
			//checkbox pour les logs
			config.setLogAuto(cbMenuLog.isSelected());
		}
		else if (e.getSource() == cbMenuTimestamp) {
			//checkbox pour les logs
			config.setTimestamp(cbMenuTimestamp.isSelected());
		}
		else if (e.getSource() == cbLancementServ) {
			
			//checkbox pour le share
			config.setShare(cbLancementServ.isSelected());
		}
		else if (e.getSource() == menuPartage) {
			JFrame frame=new JFrame();
			frame.getContentPane().add(config.getSenderAgent().getDisplay());
			
			frame.pack();
			frame.show();
			System.out.println("maintool");
			
		}
		else if (e.getSource() == menuQuit) {
			//sauvegarder les logs et quitter
			//System.exit(0);
			}
		else if (e.getSource() == menuParam) {
			PanneauConfig panneauConf = new PanneauConfig(config);
			panneauConf.setSize(450,200);
			panneauConf.setTitle(config.getLangProperty("PANEL_CONFIG_TITLE"));
			panneauConf.show();
			}
		else if (e.getSource() == menuLogExplorer) {
			LogExplorer logexpl = new LogExplorer(config);
			logexpl.setSize(450,450);
			logexpl.setTitle(config.getLangProperty("PANEL_LOGEXPLORER_TITLE"));
			logexpl.show();
			}
		else if (e.getSource() == menuOP) {
			MadChatHello madAg= (MadChatHello)config.getChatAgent();
			ChatterList liste = madAg.getDisplay().getCurrentChan().getChatList();
			PanneauOp paneau= new PanneauOp(config, liste);
			paneau.setSize(200,450);
			paneau.setTitle(config.getLangProperty("PANEL_OP_TITLE"));
			paneau.show();
			}
		else if (e.getSource() == menuDEOP) {
			MadChatHello madAg= (MadChatHello)config.getChatAgent();
			ChatterList liste = madAg.getDisplay().getCurrentChan().getChatList();
			PanneauDeop paneau= new PanneauDeop(config, liste);
			paneau.setSize(200,450);
			paneau.setTitle(config.getLangProperty("PANEL_DEOP_TITLE"));
			paneau.show();
			}
		}


	public void configChanged(String msg) {
		System.out.println("===============>config change");
		if(msg.equals("share")){
			System.out.println("==================>Msg=Share");
			cbLancementServ.setSelected(config.isShare());
		}
		else if(msg.equals("lang")){
			menuFile.setText(config.getLangProperty("MAIN_MENU_FILE"));
			menuParam.setText(config.getLangProperty("MAIN_MENU_FILE_CONFIG"));
			cbMenuTimestamp.setText(config.getLangProperty("MAIN_MENU_FILE_TIMESTAMP"));
			cbMenuLog.setText(config.getLangProperty("MAIN_MENU_FILE_LOGAUTO"));
			menuLogExplorer.setText(config.getLangProperty("MAIN_MENU_FILE_LOGVIEW"));
			menuQuit.setText(config.getLangProperty("MAIN_MENU_FILE_QUIT"));
			
			menuAction.setText(config.getLangProperty("MAIN_MENU_ACTION"));
			menuPing.setText(config.getLangProperty("MAIN_MENU_ACTION_PING"));
			menuWhois.setText(config.getLangProperty("MAIN_MENU_ACTION_WHOIS"));
			menuKick.setText(config.getLangProperty("MAIN_MENU_ACTION_KICK"));
			menuOP.setText(config.getLangProperty("MAIN_MENU_ACTION_OP"));
			menuDEOP.setText(config.getLangProperty("MAIN_MENU_ACTION_DEOP"));
			
			menuHelp.setText(config.getLangProperty("MAIN_MENU_HELP"));
			menuCommand.setText(config.getLangProperty("MAIN_MENU_HELP_COMMAND"));
			menuWebsite.setText(config.getLangProperty("MAIN_MENU_HELP_WEBSITE"));
			menuAbout.setText(config.getLangProperty("MAIN_MENU_HELP_ABOUT"));
			
			menuShare.setText(config.getLangProperty("MAIN_MENU_SHARE"));
			menuPartage.setText(config.getLangProperty("MAIN_MENU_SHARE_CONFIG"));
			cbLancementServ.setText(config.getLangProperty("MAIN_MENU_SHARE_SHAREAUTO"));
		}
		
	}
	
	

		
	
	}
