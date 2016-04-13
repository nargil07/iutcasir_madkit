
package madkit.fourmis;

import madkit.kernel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ZoneGUI extends JPanel implements ActionListener {
	
	Zone ag;
	
	 ZoneGUI(Zone _ag){
	    ag = _ag;
	    setLayout(new BorderLayout());
	    JPanel buttonList = new JPanel();
		JButton helloButt = new JButton("Hello");
		helloButt.addActionListener(this);
		JButton sendButt = new JButton("Send");
		sendButt.addActionListener(this);
		JButton killButt = new JButton("Sepuku");
		killButt.addActionListener(this);
		buttonList.add(helloButt);
		buttonList.add(sendButt);
		buttonList.add(killButt);
		add(buttonList,BorderLayout.NORTH);   
		OPanel output = new OPanel();
        add(output,BorderLayout.CENTER);
        ag.setOutputWriter(output.getOut());
	 }

	public void actionPerformed(ActionEvent e) {
         if (e.getActionCommand().equals("Hello")){
			ag.println("Hello");
         }else if (e.getActionCommand().equals("Sepuku")){
			ag.die();
         }
    }
    
}

/****************************************************************
  
   File created using the MadKit designer
   Thanks for using MadKit - 2008
   
*****************************************************************/
