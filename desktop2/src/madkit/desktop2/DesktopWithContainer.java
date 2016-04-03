package madkit.desktop2;

import java.awt.*;
import java.util.*;
import javax.swing.*;


public class DesktopWithContainer extends DesktopMDI{
	protected JFrame frame;
	
	public DesktopWithContainer(){
		super();
	}
	
	public void init(AgentInfo ai, LinkedList menuInfos, DesktopAgentGUI desktopAgentGUI){
		super.init(ai, menuInfos, desktopAgentGUI);
		
		frame = new JFrame("MadKit "+madkit.kernel.Kernel.VERSION);
		//System.out.println("loc:"+ai.getLocation()+", size:"+ai.getSize());
                frame.setSize(new Dimension(800, 600));
		/*if (ai.getSize().getWidth() == -1 && ai.getSize().getHeight() == -1){
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
		else*/
                        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			
		if (ai.getLocation().getX() == -1 && ai.getLocation().getY() == -1)
			frame.setLocation(0, 0);
		else
			frame.setLocation(ai.getLocation());
		
		 java.net.URL u = this.getClass().getResource("/images/madkit_logo32.gif");
	     //if (u!=null)	
	      	frame.setIconImage(new ImageIcon(u).getImage());
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(desktop, BorderLayout.CENTER);
		
		frame.getContentPane().add(bar, BorderLayout.SOUTH);
		dBar.setOrientation(startMenu.getOrientation());
		
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(this);
	}

	public void dispose(){
		super.dispose();
		ai.setLocation(frame.getLocation());
		ai.setSize(frame.getSize());
		bar.remove(startMenu);
		frame.dispose();
	}
	public Component getTopComponent(){ return frame; }
	
	public boolean setBarOrientation(String orientation){
		startMenu.setOrientation(orientation);
		frame.remove(bar);
		frame.getContentPane().add(bar, orientation);
		return true;
	}
	public void show(){
		frame.setVisible(true);
	}
}
