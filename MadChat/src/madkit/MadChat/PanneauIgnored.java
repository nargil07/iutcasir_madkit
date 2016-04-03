package madkit.MadChat;
import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Robot;
import java.awt.event.*;

import javax.swing.*;

public class PanneauIgnored extends JFrame implements ActionListener{
	Config cfg;
	ChatterList listIgn;
	ChatterList listChat;
	ChatterList listAccept;
	JList jListeChatter;
	JList jListeIgnored;
	JLabel lblChatter;
	JLabel lblIgnored;
	JPanel chatter;
	JPanel ignored;
	JPanel boutons;
	JPanel centre;
	JButton bAdd;
	JButton bRem;
	JScrollPane scrollChatter;
	JScrollPane scrollIgnored;
	
	public PanneauIgnored(Config conf,ChatterList lC,ChatterList lIg) {
		super();
		cfg=conf;
		listChat=lC;
		listIgn=lIg;

		
		
		listAccept=new ChatterList();
		for(int i=0;i<listChat.size();i++){
			Chatter c=(Chatter)listChat.get(i);
			if(!listIgn.contains(c) && c.getNom().equals(cfg.getLogin())==false) listAccept.add(c);
		}
		jListeChatter=new JList(listAccept);
		jListeIgnored=new JList(listIgn);

		scrollChatter=new JScrollPane(jListeChatter,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) ;
		scrollIgnored=new JScrollPane(jListeIgnored,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER) ;
		
		
		lblChatter=new JLabel(conf.getLangProperty("PANEL_IGNORE_LABEL_CONNECTED"));
		lblChatter.setOpaque(true);
		lblIgnored=new JLabel(conf.getLangProperty("PANEL_IGNORE_LABEL_IGNORED"));

		chatter=new JPanel(new BorderLayout());
		ignored=new JPanel(new BorderLayout());
		boutons=new JPanel (new GridLayout(7,1));
		
		
		bAdd=new JButton(">>");
		bAdd.addActionListener(this);
		bRem=new JButton("<<");
		bRem.addActionListener(this);
		
		
		chatter.add(lblChatter,BorderLayout.NORTH);
		chatter.add(scrollChatter,BorderLayout.CENTER);
		
		boutons.add(new JLabel(" "));
		boutons.add(new JLabel(" "));
		boutons.add(bAdd,BorderLayout.NORTH);
		boutons.add(new JLabel(" "));
		boutons.add(bRem,BorderLayout.SOUTH);
		boutons.add(new JLabel(" "));
		boutons.add(new JLabel(" "));
		
		ignored.add(lblIgnored,BorderLayout.NORTH);
		ignored.add(scrollIgnored,BorderLayout.CENTER);
		
				
		
		
		
		this.getContentPane().add(chatter,BorderLayout.WEST);
		this.getContentPane().add(boutons,BorderLayout.CENTER);
		this.getContentPane().add(ignored,BorderLayout.EAST);
		this.pack();
		//this.setSize(500,200);
		MadChatHello madH=(MadChatHello)conf.getChatAgent();
		
		this.setTitle(conf.getLangProperty("PANEL_IGNORE_TITLE")+madH.getDisplay().getCurrentConfigChan().getNomChannel());
		this.show();
		
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource()==bAdd){
			Chatter c=(Chatter)jListeChatter.getSelectedValue();
			if(c!=null){
				listIgn.add(c);
				listAccept.remove(c);
				jListeChatter.setListData(listAccept);
				jListeIgnored.setListData(listIgn);
			}
		}
		else if (e.getSource()==bRem){
			Chatter c=(Chatter)jListeIgnored.getSelectedValue();
			if(c!=null){
				listIgn.remove(c);
				listAccept.add(c);
				jListeChatter.setListData(listAccept);
				jListeIgnored.setListData(listIgn);
			}
		}
	}

	
	
	
	
	

}
