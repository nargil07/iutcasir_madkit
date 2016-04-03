package madkit.MadChat.share;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import madkit.MadChat.ChatPanel;
import madkit.MadChat.MadChat;
import madkit.TreeTools.DirEntry;
import madkit.kernel.AgentAddress;

public class ServerPanel extends JPanel implements TreeSelectionListener {

    DirEntry entry;
    AgentAddress address;
    AbstractShareAgent agent;
    ShareRemoteTree tree;
    MadChat agentPanel;
    JButton downloadButton;
    JButton closeButton;
    JButton refreshButton;
    JButton viewButton;
    TreePath selection;

    JPanel optionPanel;
    JPanel downloadPanel;
    JPanel refreshPanel;
    JPanel viewPanel;
    JPanel closePanel;
    JPanel infoPanel;


    public ServerPanel(MadChat _agentPanel, DirEntry _entry, AbstractShareAgent _agent ,AgentAddress _address){
	agentPanel = _agentPanel;
	entry = _entry;
	address = _address;
	agent = _agent;
	setLayout(new BorderLayout());
	optionPanel = new JPanel();
	downloadPanel = new JPanel();
	refreshPanel = new JPanel();
	viewPanel = new JPanel();
	closePanel = new JPanel();
	infoPanel = new JPanel();
	optionPanel.setLayout(new GridLayout(5,1));
	downloadPanel.setLayout(new FlowLayout());
	refreshPanel.setLayout(new FlowLayout());
	viewPanel.setLayout(new FlowLayout());
	closePanel.setLayout(new FlowLayout());
	infoPanel.setLayout(new FlowLayout());
	downloadButton = new JButton("Download");
	downloadButton.setToolTipText("Download selected file or directory");
	downloadButton.setEnabled(false);
	downloadButton.addActionListener(new ServerPanel_download_actionListenerAdapter(this));
	closeButton = new JButton("Close");
	closeButton.setToolTipText("Close current server panel");
	closeButton.addActionListener(new ServerPanel_close_actionListenerAdapter(this));
	refreshButton = new JButton("Refresh");
	refreshButton.setToolTipText("Refresh current server panel");
	refreshButton.addActionListener(new ServerPanel_refresh_actionListenerAdapter(this));
	viewButton = new JButton("View Incoming");
	viewButton.setToolTipText("Open your incoming directory with an explorer");
	viewButton.addActionListener(new ServerPanel_view_actionListenerAdapter(this));
	Calendar date = Calendar.getInstance();
	JLabel infoDate = new JLabel(" Updated : "+date.HOUR_OF_DAY+":"+date.MINUTE+":"+date.SECOND);
	downloadPanel.add(downloadButton);
	refreshPanel.add(refreshButton);
	viewPanel.add(viewButton);
	closePanel.add(closeButton);
	infoPanel.add(infoDate);
	optionPanel.add(downloadPanel);
	optionPanel.add(refreshPanel);
	optionPanel.add(viewPanel);
	optionPanel.add(closePanel);
	optionPanel.add(infoDate);
	tree = new ShareRemoteTree(entry, agent, address);
	tree.getTree().addTreeSelectionListener(this);
	add(tree, BorderLayout.CENTER);
	add(optionPanel, BorderLayout.EAST);
    }

    public void close_action(ActionEvent e){
	agentPanel.removeServerPanel();
    }

    public void download_action(ActionEvent e){
    	System.out.println("Class ShareAgentPanel : download " + selection.toString());
    	tree.getPath(selection.toString());
    	tree.send();
    }

    public void refresh_action(ActionEvent e){
	agentPanel.removeServerPanel();
	agentPanel.addServerPanel(entry,address);
    }

    public void view_action(ActionEvent e){
	final JFrame frame = new JFrame("Explorer");
        frame.addWindowListener(new WindowAdapter() {
		public void windowClosing(WindowEvent e) {frame.dispose();}
	    });

        frame.setContentPane(new ExplorerPanel( agent.madkitDirectory+File.separator+agent.groupName+File.separator+"Incoming"));
        frame.pack();
        frame.setVisible(true);
    }

    public void valueChanged(TreeSelectionEvent e){
	selection = tree.getTree().getSelectionPath();
	if (selection == null) downloadButton.setEnabled(false);
	else downloadButton.setEnabled(true);
    }

}
