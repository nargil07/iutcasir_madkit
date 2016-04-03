package madkit.MadChat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PanneauJoin extends JFrame implements ActionListener{
	Config conf;
	MadChatHello chatAg;
	MadChat chat;
	JPanel p;
	JPanel p1;
	JPanel p2;
	JButton ok;
	JButton annuler;
	JTextField nomChan;
	JTextField pass;
	JLabel lblNom;
	JLabel lblPass;
	public PanneauJoin(Config cfg) {
		super();
		conf=cfg;
		chatAg=(MadChatHello)conf.getChatAgent();
		chat=(MadChat)chatAg.getDisplay();
		p=new JPanel(new BorderLayout());
		p1=new JPanel(new BorderLayout());
		p2=new JPanel(new BorderLayout());
		ok=new JButton(conf.getLangProperty("PANEL_JOIN_OK"));
		ok.addActionListener(this);
		annuler=new JButton(conf.getLangProperty("PANEL_JOIN_CANCEL"));
		annuler.addActionListener(this);
		nomChan=new JTextField();
		pass=new JTextField();
		lblNom=new JLabel(conf.getLangProperty("PANEL_JOIN_LABEL_NAME"));
		lblPass=new JLabel(conf.getLangProperty("PANEL_JOIN_LABEL_PASSWORD"));
		p.add(lblNom,BorderLayout.NORTH);
		p.add(nomChan,BorderLayout.CENTER);
		p1.add(lblPass,BorderLayout.NORTH);
		p1.add(pass,BorderLayout.CENTER);
		p2.add(ok,BorderLayout.EAST);
		p2.add(annuler,BorderLayout.CENTER);
		getContentPane().add(p,BorderLayout.NORTH);
		getContentPane().add(p1,BorderLayout.CENTER);
		getContentPane().add(p2,BorderLayout.SOUTH);
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==ok){
			String ans=nomChan.getText();
			if(ans!=null && !ans.startsWith("%") && !ans.startsWith("$")){
				if(!ans.equals("")){
					if(conf.getChatAgent().isGroup("MadChat",ans)){
						if(chat.isChan(chat.getAllChan(),ans)){}
						else	chat.addChan(ans);
					}
					else{
						conf.getChatAgent().createGroup(true,"MadChat",ans,null,null);
						chat.addChan(ans,pass.getText());
						System.out.println("chan cree !");
					}
				}
			}
			this.dispose();
		}
		else if(e.getSource()==annuler){
			this.dispose();
		}
	}

	
}
