
package madkit.MadChat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


public class PanneauTopic extends JFrame implements ActionListener{

	private JTextField topic;

	private JLabel labelTopic;
	private JLabel labelSetTopic;
	private JLabel labelPass;
	private JLabel labelMod;
	
	private JCheckBox cb_topic;
	private JCheckBox cb_pass;
	private JCheckBox cb_mod;
	
	private JTextField password;
	
	private Config config;
	private ConfigChan cfgChan;
	
	private JPanel panel;
	private JPanel panel1;
	private JPanel panel2;
	private JPanel panelTopic;
	private JPanel panelisTopic;
	private JPanel panelPass;
	private JPanel panelMod;
	
	private JButton valid;
	
	public PanneauTopic(Config cfg, ConfigChan cfgchan,boolean op){
		this.config=cfg;
		this.cfgChan=cfgchan;
		
		labelTopic= new JLabel(config.getLangProperty("PANEL_TOPIC_LABEL_TOPIC"));
		labelSetTopic = new JLabel(config.getLangProperty("PANEL_TOPIC_LABEL_ONLY_ADMIN"));
		labelPass = new JLabel(config.getLangProperty("PANEL_TOPIC_LABEL_PASSWORD"));
		labelMod = new JLabel("Seuls les OP ou VOICE peuvent discuter");
		
		password = new JTextField(cfgChan.getPassword());
		password.setPreferredSize(new Dimension(180,18));
		topic= new JTextField(cfgChan.getTopic());
		
		cb_topic= new JCheckBox();
		cb_topic.setSelected(cfgChan.isTopic());
		
		cb_pass = new JCheckBox();
		if(cfgChan.getPassword().equals("")) cb_pass.setSelected(false);
		else cb_pass.setSelected(true);
		
		cb_mod = new JCheckBox();
		cb_mod.setSelected(cfgChan.isModerated());
		
		panel= new JPanel(new BorderLayout());
		panel1= new JPanel(new BorderLayout());
		panel2= new JPanel(new BorderLayout());
		panelTopic= new JPanel(new BorderLayout());
		panelisTopic= new JPanel(new BorderLayout());
		panelPass= new JPanel(new BorderLayout());
		panelMod= new JPanel(new BorderLayout());
		
		valid=new JButton(config.getLangProperty("PANEL_TOPIC_OK"));
		valid.addActionListener(this);
		
		if(!op){
			topic.setEditable(false);
			password.setEditable(false);
			cb_topic.setEnabled(false);
			cb_pass.setEnabled(false);
			cb_mod.setEnabled(false);
		}
		
		panelTopic.add(labelTopic, BorderLayout.WEST);
		panelTopic.add(topic, BorderLayout.CENTER);
		
		panelisTopic.add(cb_topic, BorderLayout.WEST);
		panelisTopic.add(labelSetTopic, BorderLayout.CENTER);
		
		panel1.add(panelTopic, BorderLayout.NORTH);
		panel1.add(panelisTopic, BorderLayout.SOUTH);
		
		panelPass.add(cb_pass, BorderLayout.WEST);
		panelPass.add(labelPass, BorderLayout.CENTER);
		panelPass.add(password, BorderLayout.EAST);
		
		panelMod.add(cb_mod, BorderLayout.WEST);
		panelMod.add(labelMod, BorderLayout.CENTER);
		
		panel2.add(panelPass, BorderLayout.NORTH);
		panel2.add(panelMod, BorderLayout.SOUTH);
		
		panel.add(panel1, BorderLayout.NORTH);
		panel.add(panel2, BorderLayout.CENTER);
		panel.add(valid, BorderLayout.SOUTH);
		
		getContentPane().add(panel);
		pack();
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==valid){
			System.out.println("topic====================>"+topic.getText());
			cfgChan.setTopic(topic.getText());
			if(cb_pass.isSelected())
				cfgChan.setPassword(password.getText());
			else cfgChan.setPassword("");
			
			cfgChan.setModerated(cb_mod.isSelected());
			InfoServMessage ism= new InfoServMessage(cfgChan.getNomChannel(),cfgChan);
			config.getChatAgent().broadcastMessage("MadChat",cfgChan.getNomChannel(),"chatter",ism);
			this.dispose();
		}
		
		
	}
}
