
package madkit.%%PROJECT%%;

import madkit.kernel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class %%NAME%%GUI extends JPanel implements ActionListener {
	
	%%NAME%% ag;
	
	 %%NAME%%GUI(%%NAME%% _ag){
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
         } else if (e.getActionCommand().equals("Send")){
			ag.sendHelloToAll();
         } else if (e.getActionCommand().equals("Sepuku")){
			ag.die();
         }
    }
    
}

/****************************************************************
  
   File created using the MadKit designer
   Thanks for using MadKit - 2008
   
*****************************************************************/
