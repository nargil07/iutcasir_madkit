/*
 * LauncherGui.java -TurtleKit - A 'star logo' in MadKit
 * Copyright (C) 2000-2002 Fabien Michel
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package turtlekit.kernel;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
//import java.awt.GridBagConstraints;
//import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import madkit.system.property.PropertyAgent;

import madkit.kernel.OPanel;
import madkit.utils.graphics.GraphicUtils;

/**
 * this class defines the Graphics object where the display is finally made
 * 
 * @author Fabien MICHEL
 * @version 1.1 4/1/2000
 */

class LauncherGui extends JPanel implements ActionListener {
	JTextField p, dD, pause, displayDelay; //zone de saisie

	JButton b1, b2, b3, b4, step, python; //les boutons

	Launcher ll;

	JPanel buttons, allbuttons, cycle;

	//JTextArea textDisplay;
	OPanel textDisplay;

	JButton bProp;

	//Image buffer;
	//Graphics bufferGraphics;

	PropertyAgent prop;

	ImageIcon iStart, iStep, iReset, iStop, iPythonEd, iView, iProps;

	public LauncherGui(Launcher l) {
		ll = l;
		setSize(220, 210);
		//setLocation(200,200);
		iStart = makeIcon("/toolbarButtonGraphics/media/Play24.gif");
		iStop = makeIcon("/toolbarButtonGraphics/media/Pause24.gif");
		iStep = makeIcon("/toolbarButtonGraphics/media/StepForward24.gif");
		iReset = makeIcon("/toolbarButtonGraphics/general/Refresh24.gif");
		iPythonEd = makeIcon("/images/agents/agenteditorPython32.gif");
		iView = makeIcon("/images/toolbars/watch.gif");
		iProps = makeIcon("/toolbarButtonGraphics/general/loupe.gif");

	}

	private void makebutton(JButton b, JPanel p) {
		p.add(b);
		b.addActionListener(this);
	}

	JButton createButton(JPanel p, String action, String descr, ImageIcon i) {
		JButton b;
		if (i != null)
			b = new JButton(i);
		else
			b = new JButton(action);

		b.setToolTipText(descr);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.setActionCommand(action);
		b.addActionListener(this);
		if (p != null)
			p.add(b);
		return b;
	}

	ImageIcon makeIcon(String path) {
		if (path != null) {
			ImageIcon i = null;
			java.net.URL u = this.getClass().getResource(path);
			if (u != null)
				i = new ImageIcon(u);

			if ((i != null) && (i.getImage() != null))
				return i;
		}
		return null;
	}

	void setButtonState(JButton b, String action, ImageIcon icon) {
		b.setActionCommand(action);
		if (icon != null)
			b.setIcon(icon);
	}

	void initialisation() {
		setLayout(new BorderLayout());
		
		// Create the buttons of the top panel
		//b1 = new JButton("Start");
		//b4 = new JButton("Reset");
		//step=new JButton("Step");
		//python=new JButton("Python");
		if (ll.wrap)
			b2 = new JButton("Wrap On");
		else
			b2 = new JButton("Wrap Off");
		b3 = new JButton("Add Viewer");

		allbuttons = new JPanel(new GridLayout(2, 3));
		b1 = createButton(allbuttons, "start", "Run and stop the simulation", iStart);
		step = createButton(allbuttons, "Step", "Step the simulation", iStep);
		b4 = createButton(allbuttons, "Reset", "Reset the simulation", iReset);
		makebutton(b2, allbuttons);
		b3 = createButton(allbuttons, "Add Viewer", "Add a viewer", iView);
		python = createButton(allbuttons, "Python", "Launch a python editor", iPythonEd);

		//Create the slider and its label
		JLabel sliderLabel = new JLabel("Simulation speed", JLabel.CENTER);
		sliderLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		JSlider simulationSpeed = new JSlider(JSlider.HORIZONTAL, 0, 500, 490);
		simulationSpeed.addChangeListener(new SliderListener());
		simulationSpeed.setMajorTickSpacing(250);
		//  simulationSpeed.setMinorTickSpacing(10);
		simulationSpeed.setPaintTicks(true);
		simulationSpeed.setPaintLabels(false);
		simulationSpeed.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

		JPanel contentPane = new JPanel(new BorderLayout());

		contentPane.add(sliderLabel, BorderLayout.WEST);
		contentPane.add(simulationSpeed, BorderLayout.CENTER);

		bProp = createButton(null, "Properties", "Shows the simulation parameters", iProps);
		contentPane.add(bProp, BorderLayout.EAST);

		textDisplay = new OPanel();//JTextArea();
		textDisplay.jscrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		textDisplay.jscrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		ll.setOutputWriter(textDisplay.getOut());

		add("North", allbuttons);
		add("South", contentPane);
		add("Center", textDisplay);
		doLayout();
		allbuttons.doLayout();
		textDisplay.doLayout();
		contentPane.doLayout();
		//buttons.doLayout();
	}

	public Dimension getPreferredSize() {
		return getSize();
	}

	public void actionPerformed(ActionEvent e) {
		Object s = e.getSource();
		if (s == b1) {
			if (b1.getActionCommand().equalsIgnoreCase("Start")) {
				b1.setBackground(Color.green);
				setButtonState(b1, "Stop", iStop);
				//b1.setText("Stop");
				ll.start = true;
				return;
			}
			if (ll.run && ll.start) {
				b1.setBackground(Color.red);
				//b1.setText("Run");
				setButtonState(b1, "Run", iStart);
				ll.setStop();
				return;
			} else if (ll.start) {
				b1.setBackground(Color.green);
				setButtonState(b1, "Stop", iStop);
				//b1.setText("Stop");
				ll.setStop();
			}
		} else if (s == b3 && ll.start)
			ll.addViewer();
		else if (s == b4 && ll.start) {
			textDisplay.clearOutput();
			ll.setReset();
			ll.run = true;
			b1.setBackground(Color.green);
			//b1.setText("Stop");
			setButtonState(b1, "Stop", iStop);
		} else if (s == b2) {
			if (b2.getText().equalsIgnoreCase("Wrap On")) {
				ll.setWrapModeOn(false);
				b2.setText("Wrap Off");
			} else {
				ll.setWrapModeOn(true);
				b2.setText("Wrap On");
			}
		}
		//if (s==p) ll.setCyclePause(Integer.parseInt(p.getText()));
		//if (s==dD) ll.setCycleDisplayEvery(Integer.parseInt(dD.getText()));
		else if (s == step) {
			if (ll.start && ll.run) {
				b1.setBackground(Color.red);
				// b1.setText("Run");
				setButtonState(b1, "Run", iStart);
				ll.setStop();
				ll.stepByStep();
				return;
			}
			if (ll.start) {
				ll.stepByStep();
				return;
			}
		} else if (s == bProp) {
			if (prop == null) {
				prop = new PropertyAgent(ll);
				ll.launchAgent(prop,"Properties of " + ll.simulationName, true);
			} else { // check
				GraphicUtils.getFrameParent((JComponent) prop.getGUIObject()).setVisible(true);
			}
		} else if (s == python) {
			try {
				ll.println("launching python. Please wait...");
				ll.launchPython();
				if (ll.run) {
					b1.setBackground(Color.red);
					//b1.setText("Run");
					setButtonState(b1, "Run", iStart);
					ll.setStop();
					ll.stepByStep();
					return;
				}
			} catch (NoClassDefFoundError ex) {
				ll.println("can't launch python in applet mode");
			} catch (Exception ex) {
				ll.println("can't launch python in applet mode");
			}
		}

	}

	void removePropertyWindows() {
		if (prop != null) {
			ll.killAgent(prop);
		}
	}

	class SliderListener implements ChangeListener {
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			if (!source.getValueIsAdjusting()) {
				ll.setCyclePause(500 - (int) source.getValue());
			}
		}
	}

}

