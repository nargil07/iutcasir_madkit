package madkit.designer;

import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.ComboBoxModel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;

import java.awt.event.*;

import madkit.kernel.Utils;


/**
* This code was generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* *************************************
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED
* for this machine, so Jigloo or this code cannot be used legally
* for any corporate or commercial purpose.
* *************************************
*/
abstract class NewEntityDialog extends javax.swing.JDialog implements ActionListener {
	protected JLabel topText;
	protected JButton cancelButton;
	protected JButton createButton;
	protected JPanel buttonPanel;
	protected JComboBox typeComboBox;
	protected JLabel typeLabel;
	protected JTextField nameTextField;
	protected JLabel nameLabel;
	protected JPanel centralPanel;
	
	static String[] USERTYPES= new String[]{"madkit", "turtlekit", "warbot"};
	static String[] GET_USERTYPES= new String[]{"default", "turtlekit", "warbot"};

	PluginDesignerGUI gui;
	
	String getNameTextField(){
		return nameTextField.getText();
	}
	
	public NewEntityDialog(JFrame frame, String title) {
		super(frame);
		setTitle(title);
	}
	public NewEntityDialog(PluginDesignerGUI _gui, String title) {
		super(Utils.getRealFrameParent(_gui));
		gui = _gui;
		setTitle(title);
	}
	
	protected void initGUI() {
		try {
			this.getContentPane().setLayout(new BorderLayout());
			
			topText = new JLabel();
			this.getContentPane().add(topText,BorderLayout.NORTH);
			// topText.setText("Create a new plugin");
			
			buttonPanel = new JPanel();
			this.getContentPane().add(buttonPanel,BorderLayout.SOUTH);
				
			createButton = new JButton();
			buttonPanel.add(createButton);
			createButton.setText("Create");
			createButton.addActionListener(this);

			cancelButton = new JButton();
			buttonPanel.add(cancelButton);
			cancelButton.setText("Cancel");
			cancelButton.addActionListener(this);
		
			centralPanel = new JPanel();
			BorderLayout jPanel1Layout = new BorderLayout();
			this.getContentPane().add(centralPanel, BorderLayout.CENTER);
			centralPanel.setLayout(null);
			centralPanel.setPreferredSize(new java.awt.Dimension(355, 152));
			nameLabel = new JLabel();
			centralPanel.add(nameLabel);
			//nameLabel.setText("plugin name :");
			nameLabel.setBounds(17, 26, 91, 24);
			nameTextField = new JTextField();
			centralPanel.add(nameTextField);
			nameTextField.setBounds(111, 30, 231, 24);
			typeLabel = new JLabel();
			centralPanel.add(typeLabel);
			// typeLabel.setText("plugin type :");
			typeLabel.setBounds(20, 72, 90, 24);
			
			initDialog();
			ComboBoxModel typeComboBoxModel = new DefaultComboBoxModel(this.getUserTypes());
			typeComboBox = new JComboBox();
			centralPanel.add(typeComboBox);
			typeComboBox.setModel(typeComboBoxModel);
			typeComboBox.setBounds(112, 73, 231, 24);
			typeComboBox.setSelectedIndex(0);
			
			this.setSize(363, 202);
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}
	
	void initDialog(){}
	
	String[] getUserTypes(){
		return new String[0];
	}
	
	String[] types=null;
	
	void setTypes(String[] lst){
		types = lst;
	}
	
	String getSelectedType(){
		return(types[typeComboBox.getSelectedIndex()]);
	}
	
	public void actionPerformed(ActionEvent e){
		String c=e.getActionCommand();
		command(c);
	}
	
	abstract protected void create();
		
	void command(String c){
		if (c.equalsIgnoreCase("Create"))
			create();
		else
			cancel();
	}
	
	void cancel(){
		this.dispose();
	}

}

class NewPluginDialog extends NewEntityDialog {
	public NewPluginDialog(JFrame frame, String title) {
		super(frame, title);
		initGUI();
	}
	public NewPluginDialog(PluginDesignerGUI _gui, String title) {
		super(_gui, title);
		initGUI();
	}
	

	void initDialog(){	
		topText.setText("Create a new plugin");
		nameLabel.setText("plugin name :");
		typeLabel.setText("plugin type :");
		setTypes(USERTYPES);
	}
	
	String[] getUserTypes(){
		// System.out.println("defining user types");
		return GET_USERTYPES;
	}
	
	protected void  create(){
		String name = getNameTextField();
		String type = getSelectedType();
		if (name.equals("")){
			System.out.println("Cannot create a plugin with no name");
		} else {
			gui.createPlugin(name,type);
		}
		this.dispose();
	}
}

abstract class NewScriptAgentDialog extends NewEntityDialog {
	// protected String[] userTypes=null;
	
	public NewScriptAgentDialog(JFrame frame, String title) {
		super(frame, title);
	}
	public NewScriptAgentDialog(PluginDesignerGUI _gui, String title) {
		super(_gui, title);
	}

	String[] getUserTypes(){return types;}
	
	protected void create(){
		String name = getNameTextField();
		String type = getSelectedType();
		if (name.equals("")){
			System.out.println("Cannot create a script agent with no name");
		} else {
			createAgent(name, type);
		}
		this.dispose();
	}
	
	abstract protected void createAgent(String name, String type);
}

class NewJavaAgentDialog extends NewScriptAgentDialog {
	static String[] javatypes = new String[] { 
			"Standard Java agent", 
			"Standard Java agent with GUI", 
		//	"Scheduled Java Agent",
			};
	
	public NewJavaAgentDialog(JFrame frame, String title) {
		super(frame, title);
		setTypes(javatypes);
		initGUI();
	}
	public NewJavaAgentDialog(PluginDesignerGUI _gui, String title) {
		super(_gui, title);
		setTypes(javatypes);
		initGUI();
	}

	void initDialog(){	
		topText.setText("Create a new Java Agent");
		nameLabel.setText("Agent name :");
		typeLabel.setText("Agent type :");
	}

	protected void createAgent(String name,String type){
			gui.createJavaAgent(name,type);
	}
}

/**
 * 
 * NewPythonAgentDialog
 *
 */

class NewPythonAgentDialog extends NewScriptAgentDialog {
	static String[] pythontypes = new String[] { 
			"Standard Python Agent", 
//			"Python Agent with GUI", 
//			"Scheduled Python Agent", 
//			"Scheduled Python Agent with GUI" 
			};
			
	public NewPythonAgentDialog(JFrame frame, String title) {
		super(frame, title);
		setTypes(pythontypes);
		initGUI();
	}
	public NewPythonAgentDialog(PluginDesignerGUI _gui, String title) {
		super(_gui, title);
		setTypes(pythontypes);
		initGUI();
	}
	
	void initDialog(){	
		topText.setText("Create a new Python Agent");
		nameLabel.setText("Agent name :");
		typeLabel.setText("Agent type :");
	}

	protected void createAgent(String name,String type){
		gui.createPythonAgent(name,type);
	}
}

class NewJessAgentDialog extends NewScriptAgentDialog {
	static String[] jesstypes = new String[] { 
			"Default Jess Agent"
	};
	public NewJessAgentDialog(JFrame frame, String title) {
		super(frame, title);
		setTypes(jesstypes);
		initGUI();
	}
	public NewJessAgentDialog(PluginDesignerGUI _gui, String title) {
		super(_gui, title);
		setTypes(jesstypes);
		initGUI();
	}
	
	void initDialog(){	
		topText.setText("Create a new Jess Agent");
		nameLabel.setText("Agent name :");
		typeLabel.setText("Agent type :");
	}

	protected void createAgent(String name,String type){
		gui.createJessAgent(name,type); 
	}
}

class NewBshAgentDialog extends NewScriptAgentDialog {
	static String[] bshtypes = new String[] { 
			"Default BeanShell Agent" //, 
	//		"BeanShell Agent with GUI",
	//		"Scheduled beanshell Agent", 
	//		"Scheduled beanshell Agent with GUI" 
			};
	public NewBshAgentDialog(JFrame frame, String title) {
		super(frame, title);
		setTypes(bshtypes);
		initGUI();
	}
	public NewBshAgentDialog(PluginDesignerGUI _gui, String title) {
		super(_gui, title);
		setTypes(bshtypes);
		initGUI();
	}
	
	void initDialog(){	
		topText.setText("Create a new BeanShell agent");
		nameLabel.setText("Agent name :");
		typeLabel.setText("Agent type :");
	}
	protected void createAgent(String name,String type){
		gui.createBshAgent(name,type); 
	}
}

class NewSchemeAgentDialog extends NewScriptAgentDialog {
	static String[] schemetypes = new String[] { 
			"Default Scheme Agent" //, 
		//	"Scheme Agent with GUI", 
		//	"Scheduled Scheme Agent", 
		//	"Scheduled Scheme Agent with GUI" 
			};
	public NewSchemeAgentDialog(JFrame frame, String title) {
		super(frame, title);
		setTypes(schemetypes);
		initGUI();
	}
	public NewSchemeAgentDialog(PluginDesignerGUI _gui, String title) {
		super(_gui, title);
		setTypes(schemetypes);
		initGUI();
	}
	
	void initDialog(){	
		topText.setText("Create a new Scheme agent");
		nameLabel.setText("Agent name :");
		typeLabel.setText("Agent type :");
	}

	protected void createAgent(String name,String type){
		gui.createSchemeAgent(name,type); 
	}
}
