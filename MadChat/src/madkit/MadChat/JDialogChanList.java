
package madkit.MadChat;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.Border;

import madkit.kernel.*;

public class JDialogChanList extends JDialog implements ActionListener{

	private JPanel panelChan;
	private JLabel label;
	private JComboBox comboChan;
	private JButton valid;
	private JButton cancel;
	private JButton refresh;
	private Config config;
	private JTable table;
	private MadChat chat;
	private ConfigChan cfgChan;
	private MyTableModel tm;
	
	public JDialogChanList(MadChat madchat, Config cfg){
		config=cfg;
		chat=madchat;
		//cfgChan=cfgchannel;
		String [] nomCol= {config.getLangProperty("PANEL_CHANLIST_TABLE_JOIN"),config.getLangProperty("PANEL_CHANLIST_TABLE_NAME"),config.getLangProperty("PANEL_CHANLIST_TABLE_TOPIC"),config.getLangProperty("PANEL_CHANLIST_TABLE_PASSWORD"),config.getLangProperty("PANEL_CHANLIST_TABLE_CONNECTED")};
		String [][] data= {{"","","","",""}};
		tm = new MyTableModel(nomCol, data);
		//DefaultTableModel tm= new DefaultTableModel(initTable(),nomCol);
		
		panelChan = new JPanel(new BorderLayout());
		
		//comboChan= new JComboBox();

		
		valid= new JButton(config.getLangProperty("PANEL_CHANLIST_OK"));
		valid.addActionListener(this);
		cancel = new JButton(config.getLangProperty("PANEL_CHANLIST_CANCEL"));
		cancel.addActionListener(this);
		refresh= new JButton(config.getLangProperty("PANEL_CHANLIST_REFRESH"));
		refresh.addActionListener(this);
		label = new JLabel(config.getLangProperty("PANEL_CHANLIST_LABEL_CHANLIST"));

		//Creation de la table des channels
		table = new JTable(tm);
		table.setRowSelectionAllowed(true);
		JScrollPane scrollPane = new JScrollPane(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setPreferredScrollableViewportSize(new Dimension(100, 40));
		table.setPreferredSize(new Dimension(500,200));

		
		//dimension des colonne
		table.getColumnModel().getColumn(0).setPreferredWidth(10);
		table.getColumnModel().getColumn(1).setPreferredWidth(25);
		table.getColumnModel().getColumn(2).setPreferredWidth(135);
		table.getColumnModel().getColumn(3).setPreferredWidth(15);
		table.getColumnModel().getColumn(4).setPreferredWidth(10);
		
		panelChan.add(label, BorderLayout.NORTH);
		panelChan.add(scrollPane, BorderLayout.CENTER);
		
		JPanel panelButton=new JPanel(new FlowLayout());
		panelButton.add(refresh);
		panelButton.add(valid);
		panelButton.add(cancel);
		
		panelChan.add(panelButton, BorderLayout.SOUTH);
		getContentPane().add(panelChan);
		this.setSize(500,250);
		ChanInfoAgent cia=new ChanInfoAgent(tm, config);
		config.getChatAgent().launchAgent(cia,"UpdateChan",false);
		show();
	}
	
	public Object[][] initTable(){
		Agent a= config.getChatAgent();
		String [] chanlist = a.getExistingGroups("MadChat");
		Object [][] tabchan= new Object[chanlist.length][5]; 
	
		for(int i =0; i< chanlist.length;i++){
			if(chanlist[i].startsWith("%") || chanlist[i].startsWith("$")){
				System.out.println(chanlist[i]+" est prive");
				}
			else {
				tabchan[i][0] = new Boolean(false);
				tabchan[i][1] = chanlist[i];
				tabchan[i][2] = cfgChan.getTopic();
				tabchan[i][3] = cfgChan.getPassword();
				tabchan[i][4] = new Integer(a.getAgentsWithRole("MadChat",chanlist[i],"chatter").length);
			}
		
		}
		
		return tabchan;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==valid){
			for (int i = 0; i < tm.getRowCount(); i++) {
				Object o = tm.getValueAt(i,0);
				if(o instanceof Boolean){
					Boolean b = (Boolean) tm.getValueAt(i, 0);
					if (b.booleanValue() == true) 
					{
			   			chat.addChan((String)tm.getValueAt(i,1));
			   			System.out.println("chan cree !");
					}
				}
				
			}
							
			this.dispose();
		}
		else if(e.getSource()==cancel){
			this.dispose();
		}
		else if(e.getSource()==refresh){
			ChanInfoAgent cia=new ChanInfoAgent(tm, config);
			config.getChatAgent().launchAgent(cia,"UpdateChan",false);
		}

	}
}