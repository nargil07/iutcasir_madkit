/*
 * Created on Apr 5, 2006
 *
 */
package madkit.MadChat;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.*;

// Cette classe cr?e une frame qui permet le visionnage des logs
public class LogExplorer extends JFrame implements MouseListener, ActionListener{
	private JPanel panel;
	private JPanel buttonPanel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane2;
	private JButton quit;
	private JButton delete;
	private JButton deleteAll;
	private JList listLog;
	private HTMLEditor viewLog;
	private Config config;
	private Vector listeLog;

	
	public LogExplorer(Config cfg){
		this.config=cfg;
		String [] temp;
		
		File f = new File(config.getLogPath());
		temp = f.list();
		listeLog= new Vector();
		for(int i = 0;i<temp.length; i++){
			listeLog.add(temp[i]);
		}
		
		panel = new JPanel(new BorderLayout());
		buttonPanel= new JPanel(new BorderLayout());
		
		quit= new JButton("Close");
		quit.addActionListener(this);
		delete = new JButton("Delete selected");
		delete.addActionListener(this);
		deleteAll= new JButton("Delete all");
		deleteAll.addActionListener(this);
		
		listLog= new JList(listeLog);
		listLog.addMouseListener(this);
		listLog.setFixedCellWidth(100);
		
		viewLog = new HTMLEditor();
		viewLog.setEditable(false);
		if(config.getOsName().startsWith("Windows"))
			viewLog.insertHTML("<body background=\"file:///"+config.getBackground()+"\">"); // le background par defaut
		else viewLog.insertHTML("<body background=\"file://"+config.getBackground()+"\">"); // le background par defaut		
		
		viewLog.insertHTML("Click on a log in the list to display it.");
		
		buttonPanel.add(delete, BorderLayout.WEST);
		buttonPanel.add(deleteAll, BorderLayout.CENTER);
		buttonPanel.add(quit, BorderLayout.EAST);
		
		scrollPane = new JScrollPane(viewLog,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setWheelScrollingEnabled(true);
		
		scrollPane2 = new JScrollPane(listLog,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane2.setWheelScrollingEnabled(true);
		
		panel.add(scrollPane2, BorderLayout.WEST);
		panel.add(scrollPane, BorderLayout.CENTER);	
		panel.add(buttonPanel, BorderLayout.SOUTH);
		
		getContentPane().add(panel);
		pack();
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==listLog){
			JList list = (JList)e.getSource();
        if (e.getClickCount() == 1) {      // Double-click
        	viewLog.setText("");
            String log =(String)list.getSelectedValue();
            File f= new File(config.getLogPath()+log);
            String ligne;
            try {
				BufferedReader br = new BufferedReader(new FileReader(f));
				while((ligne=br.readLine()) != null){
					viewLog.insertHTML(ligne);
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	}
		}
	}

	public void mouseEntered(MouseEvent e) {
		

	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== quit){
			this.dispose();
		}
		else if(e.getSource()== delete){
			System.out.println("deleting log : "+(String)listLog.getSelectedValue());
			File f = new File(config.getLogPath()+(String)listLog.getSelectedValue());
			f.delete();
			listeLog.remove(listLog.getSelectedValue());
			listLog.setListData(listeLog);
			viewLog.setText("");
			if(config.getOsName().startsWith("Windows"))
				viewLog.insertHTML("<body background=\"file:///"+config.getBackground()+"\">"); // le background par defaut
			else viewLog.insertHTML("<body background=\"file://"+config.getBackground()+"\">"); // le background par defaut		
			
			viewLog.insertHTML("Click on a log in the list to display it.");
		}
		else if(e.getSource()== deleteAll){
			for(int i=0; i<listeLog.size(); i++){
				System.out.println("deleting log : "+(String)listeLog.get(i));
				File f = new File(config.getLogPath()+(String)listeLog.get(i));
				f.delete();
			}
			listeLog.removeAllElements();
			listLog.setListData(listeLog);
			viewLog.setText("");

			if(config.getOsName().startsWith("Windows"))
				viewLog.insertHTML("<body background=\"file:///"+config.getBackground()+"\">"); // le background par defaut
			else viewLog.insertHTML("<body background=\"file://"+config.getBackground()+"\">"); // le background par defaut		
			viewLog.insertHTML("Click on a log in the list to display it.");
		}
	}
}
