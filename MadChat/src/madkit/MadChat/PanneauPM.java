/*
 * Created on Apr 28, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package madkit.MadChat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
/**
 * @author gaara
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PanneauPM extends JFrame implements ActionListener{
	private Config config;
	MadChatHello chatAg;
	MadChat chat;
	
	private JList listeChatter;
	private ChatterList rawList;
	
	private JPanel panelChatter;
	private JPanel panelButton;
	
	private JScrollPane scrollChatter;
	
	private JLabel label;
	private JButton valider;
	private JButton cancel;
	
	public PanneauPM(Config conf,ChatterList lC){
		super();
		config=conf;
		rawList = new ChatterList();

		chatAg=(MadChatHello)config.getChatAgent();
		chat=(MadChat)chatAg.getDisplay();
		
		for(int i=0;i<lC.size();i++){
			Chatter c=(Chatter)lC.get(i);
			if(c.getNom().equals(config.getLogin())==false && !chat.existChan("$"+c.getNom())) rawList.add(c);
		}		
		
		
		System.out.println("rawList count = "+rawList.size());
		
		listeChatter=new JList(rawList);

		valider = new JButton(config.getLangProperty("PANEL_PM_OK"));
		valider.addActionListener(this);
		cancel = new JButton(config.getLangProperty("PANEL_PM_CANCEL"));
		cancel.addActionListener(this);
		
		panelChatter = new JPanel(new BorderLayout());
		panelButton = new JPanel(new BorderLayout());
		
		panelButton.add(valider, BorderLayout.CENTER);
		label = new JLabel(config.getLangProperty("PANEL_PM_LABEL_PM"));

		panelButton.add(cancel, BorderLayout.EAST);
		
		scrollChatter=new JScrollPane(listeChatter,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) ;
		
		panelChatter.add(label, BorderLayout.NORTH);
		panelChatter.add(scrollChatter, BorderLayout.CENTER);
		panelChatter.add(panelButton, BorderLayout.SOUTH);
		
		getContentPane().add(panelChatter);
		setSize(200,450);
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == valider){
			Object[] liste = listeChatter.getSelectedValues();
			MadChatHello ag=(MadChatHello)config.getChatAgent();
			
			for(int i=0; i<liste.length; i++){
				Chatter c = (Chatter)liste[i];
				System.out.println("Chatter "+i+" a pm : "+c.getNom());
				//rawList.getByLogin(c.getNom()).setOp(true);
				
				CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_PV,"$"+config.getLogin(),config.getLogin());
				ag.sendMessage(c.getAdresse(), m);
				
//				on cr?e la fenetre de chat pv pour l'utilisateur i				
				//config.getChatAgent().createGroup(true,"MadChat","%"+c.getNom(),null,null);
				chat.addChanPV(config,c);
				System.out.println("chan cree !");
			}
			
			
			this.dispose();
		}
		
		if(e.getSource() == cancel){
			this.dispose();
		}

	}
}