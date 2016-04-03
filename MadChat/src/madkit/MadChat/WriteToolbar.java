package madkit.MadChat;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.net.*;
import java.io.*;

public class WriteToolbar extends JToolBar implements ActionListener,ConfigChangeListener{
	Config configuration;
	JTextField tWriteArea;
	JButton bColor;
	JButton bBold;
	JButton bItalic;
	JComboBox bPolice;
	JComboBox bSmiley;
	JComboBox bTailleFont;
	JCheckBox bSaveLog;
	JLabel bHorloge;
	JColorChooser colorChooser;
	Color currentColor;
	String currentFont;
	String tailleFont;
	String path;
	
	String separator;
	
	
	public WriteToolbar(JTextField wArea,Config cfg){
		super();
		configuration=cfg;
		separator=configuration.getFileSeparator();
		
		currentFont="Default";
		tailleFont="4";
		tWriteArea=wArea;		
		currentColor=Color.decode(configuration.getDefaultColor());
		
		configuration.addConfigChangeListener(this);
		
		//currentColor = Color.BLACK;
		colorChooser = new JColorChooser();
		
		System.out.println("C le path des Smiley :"+path);
		bHorloge =new JLabel();
		bHorloge.setOpaque(false);
		
		bColor= new JButton();
		bColor.addActionListener(this);
		bColor.setPreferredSize(new Dimension(18,18));
		bColor.setMaximumSize(new Dimension(18,18));
		bColor.setBackground(currentColor);	
		bColor.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_COLOR_TOOLTIP"));
		
		bBold= new JButton("B");
		bBold.addActionListener(this);
		bBold.setPreferredSize(new Dimension(20,20));
		bBold.setMaximumSize(new Dimension(20,20));
		bBold.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_BOLD_TOOLTIP"));
		
		bItalic= new JButton("I");
		bItalic.addActionListener(this);
		bItalic.setPreferredSize(new Dimension(20,20));
		bItalic.setMaximumSize(new Dimension(20,20));
		bItalic.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_ITALIC_TOOLTIP"));
		
		bSmiley =new JComboBox();
		SmileyTab smileyTab=configuration.getSmileyTab();
		String tab[]=smileyTab.getKeys();
		for(int i=0;i<tab.length;i++){
			bSmiley.addItem(new ImageIcon(smileyTab.getPathBySmiley(tab[i])));
			System.out.println("Ajout smiley : ["+tab[i]+"]="+smileyTab.getPathBySmiley(tab[i]));
	    }
	
		
		
		bSmiley.addActionListener(this);
		bSmiley.setPreferredSize(new Dimension(40,20));
		bSmiley.setMaximumSize(new Dimension(40,20));
		bSmiley.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_SMILEY_TOOLTIP"));
		
		bPolice= new JComboBox();
		bPolice.addItem("Default");
		bPolice.addItem("Arial Black");
		//bPolice.addItem("Helvetica");
		bPolice.addItem("impact");
		//bPolice.addItem("verdana");
		bPolice.setPreferredSize(new Dimension(100,20));
		bPolice.setMaximumSize(new Dimension(100,20));
		bPolice.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_FONT_TOOLTIP"));
		
		bTailleFont= new JComboBox();
		bTailleFont.addItem("2");
		bTailleFont.addItem("3");
		bTailleFont.addItem("4");
		bTailleFont.addItem("5");
		bTailleFont.addItem("6");
		bTailleFont.addItem("7");
		bTailleFont.addItem("8");
		bTailleFont.setSelectedIndex(2);
		bTailleFont.setPreferredSize(new Dimension(40,20));
		bTailleFont.setMaximumSize(new Dimension(40,20));
		bTailleFont.addActionListener(this);
		bTailleFont.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_FONTSIZE_TOOLTIP"));
		
		bSaveLog =new JCheckBox((String)configuration.getLangProperty("WRITE_TOOLBAR_LOGCHAN"));
		bSaveLog.setBorderPaintedFlat(true); 
		bSaveLog.addActionListener(this);
		bSaveLog.setPreferredSize(new Dimension(170,20));
		//bSaveLog.setMaximumSize(new Dimension(170,20));
		bSaveLog.setOpaque(false);
		bSaveLog.setSelected(configuration.isLogAuto());
		bSmiley.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_LOGCHAN_TOOLTIP"));
		
		addSeparator();
		add(bColor);
		addSeparator();
		add(bBold);
		addSeparator();
		add(bItalic);
		addSeparator();
		add(bPolice);
		addSeparator();
		add(bTailleFont);
		addSeparator();
		add(bSmiley);
		addSeparator();
		add(bHorloge);
		addSeparator(new Dimension(60,18));
		add(bSaveLog);
		
		ClockThread clock= new ClockThread(bHorloge);
		clock.start();
		
	}
	
	

	public void actionPerformed(ActionEvent e) {
		String buf,fin;
		String debut="";
		int position=0;
		
		buf=tWriteArea.getText();
		
		if(e.getSource()== bBold){
			
			debut=buf.substring(0,tWriteArea.getSelectionStart());
			fin=buf.substring(tWriteArea.getSelectionEnd());
			
			if(tWriteArea.getSelectedText()==null){
				buf=debut+"[g][/g]"+fin;
				
				}
				else{buf=debut+"[g]"+tWriteArea.getSelectedText()+"[/g]"+fin;}
			position=debut.length()+3;
			tWriteArea.setText(buf);
		}
		else if(e.getSource()== bItalic){
			
			
			debut=buf.substring(0,tWriteArea.getSelectionStart());
			fin=buf.substring(tWriteArea.getSelectionEnd());
			
			if(tWriteArea.getSelectedText()==null){
				buf=debut+"[i][/i]"+fin;
				}
				else{buf=debut+"[i]"+tWriteArea.getSelectedText()+"[/i]"+fin;}
			tWriteArea.setText(buf);
			position=debut.length()+3;
		}
		else if(e.getSource()== bSmiley){
			if(bSmiley.getItemCount()>0){
				debut=buf.substring(0,tWriteArea.getSelectionStart());
				fin=buf.substring(tWriteArea.getSelectionEnd());
				String image=((ImageIcon)bSmiley.getSelectedItem()).toString();
				SmileyTab smileyTab=configuration.getSmileyTab();
				String smiley=smileyTab.getSmileyByPath(image);
				
				buf=debut+smiley+fin;
				
				System.out.println("buf=====>"+buf);
				tWriteArea.setText(buf);
				position=debut.length()+smiley.length();
			}	
			
		}
		else if(e.getSource()==bColor){
			//JFrame colorPanel=new JFrame();
			//colorPanel.setSize(new Dimension(450,250));
			
			//JColorChooser colorChooser= new JColorChooser(Color.YELLOW);
			//bColor.setBackground(colorChooser.getColor());
			//JDialog dialColor = colorChooser.createDialog(this, "Couleur Ok?", true, colorChooser, this, null); 
			//colorPanel.add(colorChooser);
			//dialColor.setVisible(true);
			
	        JDialog dialog = JColorChooser.createDialog(this,
	                                        "Pick a Color",
	                                        true,  //modal
	                                        colorChooser,
	                                        this,  //OK button handler
	                                        null); //no CANCEL button handler
	       
	         colorChooser.setColor(currentColor);
	         
	         dialog.setVisible(true);
	         position=buf.length();

		}

		else if(e.getActionCommand().equals("OK")) {
			bColor.setBackground(colorChooser.getColor());
			currentColor = colorChooser.getColor();
			System.out.println(Integer.toHexString(currentColor.getRGB()));
			position=buf.length();
			
		}
		
		else if(e.getSource()==bTailleFont){
			tailleFont=(String)bTailleFont.getSelectedItem();
			position=buf.length();
		}
		else if(e.getSource()==bPolice){
			currentFont=(String)bTailleFont.getSelectedItem();
			position=buf.length();

	
		}
		
		tWriteArea.grabFocus();
		tWriteArea.setCaretPosition(position);
	}

	public Color getCurrentColor() {
		return currentColor;
	}
	
	public String getPolice() {
		return currentFont;
	}
	
	public String getTaillePolice() {
		return tailleFont;
	}

	

	public boolean isSaveLog() {
		return bSaveLog.isSelected();
	}



	public void configChanged(String msg) {
		// TODO Auto-generated method stub
		System.out.println("config changed "+msg);
		if(msg.equals("theme")){
			bSmiley.removeAllItems();
			
			SmileyTab smileyTab=configuration.getSmileyTab();
			String tab[]=smileyTab.getKeys();
			for(int i=0;i<tab.length;i++){
				bSmiley.addItem(new ImageIcon(smileyTab.getPathBySmiley(tab[i])));
				System.out.println("Ajout smiley : ["+tab[i]+"]="+smileyTab.getPathBySmiley(tab[i]));
		    }
		}
		else if(msg.equals("lang")){
			System.out.println("LOG_CHAN="+configuration.getLangProperty("LOG_CHAN"));
			
			bColor.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_COLOR_TOOLTIP"));
			bBold.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_BOLD_TOOLTIP"));
			bItalic.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_ITALIC_TOOLTIP"));
			bPolice.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_FONT_TOOLTIP"));
			bSmiley.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_SMILEY_TOOLTIP"));
			bTailleFont.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_FONTSIZE_TOOLTIP"));
			bSaveLog.setText(configuration.getLangProperty("WRITE_TOOLBAR_LOGCHAN"));
			bSaveLog.setToolTipText(configuration.getLangProperty("WRITE_TOOLBAR_LOGCHAN_TOOLTIP"));
			
		}
	}
}
	
		

	

