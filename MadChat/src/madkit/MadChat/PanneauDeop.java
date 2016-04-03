/*
 * Created on May 2, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package madkit.MadChat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

/**
 * @author gaara
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PanneauDeop extends JFrame implements ActionListener{
	private Config config;
	
	private JList listeChatter;
	private ChatterList rawList;
	private ChatterList opList;
	
	private JPanel panelChatter;
	private JPanel panelButton;
	
	private JScrollPane scrollChatter;
	
	private JLabel label;
	private JButton valider;
	private JButton cancel;
	
	public PanneauDeop(Config conf,ChatterList lC){
		super();
		config=conf;
		rawList = lC;
		System.out.println("rawList count = "+rawList.size());
		opList=new ChatterList();
		for(int i=0;i<rawList.size();i++){
			Chatter c=(Chatter)rawList.get(i);
			if(c.isOp()) opList.add(c);
		}
		System.out.println("opList count = "+opList.size());
		listeChatter=new JList(opList);
				
		valider = new JButton(config.getLangProperty("PANEL_DEOP_OK"));
		valider.addActionListener(this);
		cancel = new JButton(config.getLangProperty("PANEL_DEOP_CANCEL"));
		cancel.addActionListener(this);
		
		panelChatter = new JPanel(new BorderLayout());
		panelButton = new JPanel(new BorderLayout());
		
		
		if(rawList.getByLogin(config.getLogin()).isOp()){
			panelButton.add(valider, BorderLayout.CENTER);
			label = new JLabel(config.getLangProperty("PANEL_DEOP_LABEL_OK"));
		}
		else label = new JLabel(config.getLangProperty("PANEL_DEOP_LABEL_NOOP"));
		
		
		panelButton.add(cancel, BorderLayout.EAST);
		
		scrollChatter=new JScrollPane(listeChatter,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) ;
		
		
		panelChatter.add(label, BorderLayout.NORTH);
		panelChatter.add(scrollChatter, BorderLayout.CENTER);
		panelChatter.add(panelButton, BorderLayout.SOUTH);
		
		getContentPane().add(panelChatter);
		
	}
	
	
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == valider){
			Object[] liste = listeChatter.getSelectedValues();
			
			MadChatHello ag= (MadChatHello)config.getChatAgent();
			for(int i=0; i<liste.length; i++){
				Chatter c = (Chatter)liste[i];
				System.out.println("Chatter "+i+" a deop : "+c.getNom());
				//rawList.getByLogin(c.getNom()).setOp(false);
				
				CommandMessage m = new CommandMessage(config.getChatAgent().getAddress(),CommandMessage.MSG_DEOP,ag.getDisplay().getCurrentChan().getNomChannel(),c.getNom());
				ag.broadcastMessage("MadChat",ag.getDisplay().getCurrentChan().getNomChannel(),"chatter",m);
			}
			this.dispose();
		}
		
		if(e.getSource() == cancel){
			this.dispose();
		}

	}
}