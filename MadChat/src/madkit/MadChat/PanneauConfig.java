/*******
 * Creation et affichage du panneau de configuration dans une JFrame
 */
package madkit.MadChat;


import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class PanneauConfig extends JFrame implements ActionListener{
	
	//Panneau de config
	JTabbedPane tabbedChan;
	JPanel panelUser;
	GridBagLayout gridUser;
	GridBagLayout gridAffichage;
	GridBagLayout gridSysteme;
	JPanel panelAffichage;
	JPanel panelPartage;
	JPanel panelSysteme;
	
	//Users
	JLabel nick;
	JLabel mail;
	JTextField nickEntry;
	JTextField mailEntry;
	JLabel Image;
	
	//Affichage
	JLabel lang;
	JComboBox langEntry;
	JLabel theme;
	JComboBox themeEntry;
	JCheckBox timestamp;
	JLabel background;
	JTextField backgroundEntry;
	JButton fileBrowser;
	String backgroundPath;
	String oldBackground;
	
	
	//Partage
	JCheckBox checkServ;
	JButton bConfigServer;
	
	
	//Systeme
	JCheckBox logAuto;
	JLabel log;
	JTextField logPath;
	
	//Validation
	JButton valid;
	private Config config;
	
	
	public PanneauConfig(Config cfg){
		config=cfg;
		
		oldBackground=config.getBackground();
		System.out.println("old background = "+oldBackground);
		
		gridUser=new GridBagLayout();
		gridAffichage=new GridBagLayout();
		gridSysteme=new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		//c.fill = GridBagConstraints.BOTH;

        
		tabbedChan =new JTabbedPane();
		panelUser = new JPanel();
		panelAffichage = new JPanel();
		panelPartage = new JPanel(new BorderLayout());
		panelSysteme = new JPanel();
		
		//Users
		nick = new JLabel(config.getLangProperty("PANEL_CONF_LABEL_NICKNAME"));
		nickEntry = new JTextField(config.getLogin(),10);
		mail = new JLabel("Modifier le mail actuel : ");
		mailEntry = new JTextField(10);
		
		//Affichage
		lang = new JLabel(config.getLangProperty("PANEL_CONF_LABEL_LANG"));
		langEntry = new JComboBox();
		File f = new File(config.getConfigPath()+"Lang");
		String [] listeLang = f.list();
		int index=0;
		for(int i=0; i< listeLang.length;i++)
		{
			String s= listeLang[i].substring(0,listeLang[i].length()-5);
			if(s.equals(config.getLang())) index=i;	
			langEntry.addItem(s);
		}
		langEntry.setSelectedIndex(index);
		theme = new JLabel(config.getLangProperty("PANEL_CONF_LABEL_THEME"));
		themeEntry = new JComboBox();
		f = new File(config.getThemePath());
		String [] listeThemes = f.list();
		index=0;
		for(int i=0; i< listeThemes.length;i++)
		{
			if(listeThemes[i].equals(config.getTheme())) index=i;
			themeEntry.addItem(listeThemes[i]);
		}
		themeEntry.setSelectedIndex(index);
		themeEntry.addActionListener(this);
		background= new JLabel(config.getLangProperty("PANEL_CONF_LABEL_BACKGROUND"));
		backgroundEntry = new JTextField(config.getBackground(), 20);
		fileBrowser=new JButton(config.getLangProperty("PANEL_CONF_BROWSE"));
		
		timestamp = new JCheckBox(config.getLangProperty("PANEL_CONF_LABEL_TIMESTAMP"),config.isTimestamp());
		
//		Partage
		checkServ =new JCheckBox(config.getLangProperty("PANEL_CONF_LABEL_LAUNCH_SHARE"),config.isShare());
		checkServ.addActionListener(this);
		bConfigServer = new JButton(config.getLangProperty("PANEL_CONF_LABEL_CONFIG_SHARE"));
		bConfigServer.addActionListener(this);
		panelPartage.add(checkServ,BorderLayout.NORTH);
		panelPartage.add(bConfigServer,BorderLayout.SOUTH);
		

//		Systeme
		logAuto= new JCheckBox(config.getLangProperty("PANEL_CONF_LABEL_SAVE_LOG"),config.isLogAuto());
		log = new JLabel(config.getLangProperty("PANEL_CONF_LABEL_LOG_PATH"));
		logPath = new JTextField(config.getLogPath());
		logPath.setPreferredSize(new Dimension(180,18));
		
		
//Agencement des variables User
		panelUser.setLayout(gridUser);
		gridUser.setConstraints(nick, c);
		panelUser.add(nick);
		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		gridUser.setConstraints(nickEntry, c);
		panelUser.add(nickEntry);
		//c.weightx = 0.0;		   //reset to the default
		
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridUser.setConstraints(mail, c);
		panelUser.add(mail);
		gridUser.setConstraints(mailEntry, c);
		panelUser.add(mailEntry);
		
//Agencement des variables Affichage
		c.fill=GridBagConstraints.NORTH;
        c.weightx = 1.0;
        
        panelAffichage.setLayout(gridAffichage);
        
		gridAffichage.setConstraints(lang, c);
		panelAffichage.add(lang);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridAffichage.setConstraints(langEntry, c);
		panelAffichage.add(langEntry);
        
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridAffichage.setConstraints(theme, c);
		panelAffichage.add(theme);
		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		gridAffichage.setConstraints(themeEntry, c);
		panelAffichage.add(themeEntry);
		
		c.gridwidth = GridBagConstraints.RELATIVE;
		gridAffichage.setConstraints(background, c);
		panelAffichage.add(background);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		gridAffichage.setConstraints(backgroundEntry, c);
		panelAffichage.add(backgroundEntry);
		
		gridAffichage.setConstraints(fileBrowser, c);
		panelAffichage.add(fileBrowser);
		fileBrowser.addActionListener(this);
		//c.weightx = 0.0;		   //reset to the default
		//c.gridwidth = GridBagConstraints.RELATIVE;
		
		gridAffichage.setConstraints(timestamp, c);
		panelAffichage.add(timestamp);

//Agencement des variables Systeme
		c.fill=GridBagConstraints.NORTH;
        c.weightx = 1.0;
		panelSysteme.setLayout(gridSysteme);
		gridSysteme.setConstraints(logAuto, c);
		panelSysteme.add(logAuto);
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		gridSysteme.setConstraints(log, c);
		panelSysteme.add(log);
		gridSysteme.setConstraints(logPath, c);
		panelSysteme.add(logPath);
		
//Ajout des onglets du menu
		tabbedChan.add(panelUser,config.getLangProperty("PANEL_CONF_TAB_USER"));
		tabbedChan.add(panelAffichage,config.getLangProperty("PANEL_CONF_TAB_DISPLAY"));
		tabbedChan.add(panelPartage,config.getLangProperty("PANEL_CONF_TAB_SHARE"));
		tabbedChan.add(panelSysteme,config.getLangProperty("PANEL_CONF_TAB_SYSTEM"));
		getContentPane().add(tabbedChan,BorderLayout.NORTH);
		
		//Bouton valider
		valid=new JButton(config.getLangProperty("PANEL_CONF_OK"));
		getContentPane().add(valid,BorderLayout.SOUTH);
		valid.addActionListener(this);
		pack();
	}
	
	public boolean isBackgroundChanged(){
		return !config.getBackground().equals(oldBackground); 
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == valid) {
			//on sauvegarde les nouveaux parametres dans le fichier de config
			System.out.println(nickEntry.getText());
			if(!nickEntry.getText().startsWith("@")&& !nickEntry.getText().startsWith("+")){
				MadChatHello madAg= (MadChatHello)config.getChatAgent();
				ChatterList liste = madAg.getDisplay().getChatListInChan("default");
				
				if (!nickEntry.getText().equals("")){
					if(liste.getByLogin(nickEntry.getText()) == null)
						config.setLogin(nickEntry.getText());
					else if(!nickEntry.getText().equals(config.getLogin())) madAg.getDisplay().getCurrentChan().insertInfoMessage("Config : Ce nick est deja en cours d'utilisation, veuillez en choisir un autre.","yellow");
				}
				else madAg.getDisplay().getCurrentChan().insertInfoMessage("Config : Le nick \""+nickEntry.getText()+"\" n'est pas valide.","yellow");
				
				config.setTimestamp(timestamp.isSelected());
				config.setLogAuto(logAuto.isSelected());
				config.setTheme(themeEntry.getSelectedItem().toString());
				config.setBackground(backgroundEntry.getText());
				config.setShare(checkServ.isSelected());
				config.setLang(langEntry.getSelectedItem().toString());
				config.save();
			}
			dispose();
		}
		else if(e.getSource()==bConfigServer){
			JFrame frame=new JFrame();
			frame.getContentPane().add(config.getSenderAgent().getDisplay());
			
			frame.pack();
			frame.show();
			
		}
		else if (e.getSource() == fileBrowser) {
			FileChooserGUI fc= new FileChooserGUI(config);
			fc.setSize(300,200);
			//fc.show();
			backgroundEntry.setText(fc.getBackgroundPath());
		}
		else if (e.getSource() == themeEntry) {
			backgroundEntry.setText(config.getThemePath()+(String)themeEntry.getSelectedItem()+config.getFileSeparator()+"background.jpg");
		}
	}
}
