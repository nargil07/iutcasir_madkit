
package madkit.compteur;

import madkit.kernel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlleurGUI extends JPanel implements ActionListener {
	
	Controlleur ag;
	
	 ControlleurGUI(Controlleur _ag){
	    ag = _ag;
	    setLayout(new BorderLayout());
	    JPanel buttonList = new JPanel();
		JButton helloButt = new JButton("Stop");
		helloButt.addActionListener(this);
		JButton sendButt = new JButton("Start");
		sendButt.addActionListener(this);
		buttonList.add(helloButt);
		buttonList.add(sendButt);
		add(buttonList,BorderLayout.NORTH);   
		OPanel output = new OPanel();
        add(output,BorderLayout.CENTER);
        ag.setOutputWriter(output.getOut());
	 }

	public void actionPerformed(ActionEvent e) {
         if (e.getActionCommand().equals("Stop")){
			ag.stop();
         } else if (e.getActionCommand().equals("Start")){
			ag.start();
         }
    }
    
}

/****************************************************************
  
   File created using the MadKit designer
   Thanks for using MadKit - 2008
   
*****************************************************************/
