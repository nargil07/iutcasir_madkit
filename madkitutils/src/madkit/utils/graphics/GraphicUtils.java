/*
 * GraphicUtils.java - SEdit, a tool to design and animate graphs in MadKit
 * Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package madkit.utils.graphics;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import madkit.kernel.AbstractMadkitBooter;
import madkit.utils.common.OperatingSystem;

public class GraphicUtils {

	public static void log(String s) {
		//	System.err.println(s);
	}

	public static void debug(Object s) {
		//	System.err.println("DEBUG:"+s);
	}

	public static void debug(Object o, Object s) {
		//	System.err.println("Debug("+o+"):"+s);
	}

	/*
	 * Get the extension of a file.
	 */
	public static String getExtension(File f) {
		String ext = null;
		String s = f.getName();
		int i = s.lastIndexOf('.');

		if (i > 0 && i < s.length() - 1) {
			ext = s.substring(i + 1).toLowerCase();
		}
		return ext;
	}

	/**
	 * get the Frame that surrounds a component. May be used for buildings
	 * dialogs Ol: JF import
	 */
	static public Frame getRealFrameParent(Component c) {
		while (!((c instanceof Frame) || (c instanceof JFrame))) {
			if (c == null)
				return (null);
			else
				c = c.getParent();
		}
		return ((Frame) c);
	}

	static public Container getFrameParent(Container _c) {
		Container c = _c;
		while (!((c instanceof JFrame) || (c instanceof Frame) || (c instanceof JInternalFrame))) {
			if (c == null)
				return (null);
			else
				c = c.getParent();
		}
		return ((Container) c);
	}

	// utilise par StructureFrame
	public static void addMenuItem(ActionListener al, JMenu m, String label,
			String command, int key) {
		addMenuItem(al, m, label, command, key, -1);
	}

	public static void addMenuItem(ActionListener al, JMenu m, String label,
			String command, int key, int ckey) {
		JMenuItem menuItem;
		menuItem = new JMenuItem(label);
		m.add(menuItem);

		menuItem.setActionCommand(command);
		menuItem.addActionListener(al);
		setKeystroke(menuItem, ckey);
	}

	public static JButton addTool(ActionListener al, JToolBar toolBar,
			String name, String descr, String imageName) {
		JButton b;
		if (imageName.equals("")) {
			b = (JButton) toolBar.add(new JButton(name));
			b.setActionCommand(name);
		} else {
			java.net.URL u = AbstractMadkitBooter.getBooter().getClass()
					.getResource(imageName);
			if (u != null)
				b = (JButton) toolBar.add(new JButton(new ImageIcon(u)));
			else
				b = (JButton) toolBar.add(new JButton(descr));
			b.setActionCommand(name);
		}

		b.setToolTipText(descr);
		b.setMargin(new Insets(0, 0, 0, 0));
		b.addActionListener(al);
		return b;
	}

	static void setKeystroke(JMenuItem m, int key) {
		if (key > 0) {
			if (key != KeyEvent.VK_DELETE)
				m.setAccelerator(KeyStroke.getKeyStroke(key, Event.CTRL_MASK,
						false));
			else
				m.setAccelerator(KeyStroke.getKeyStroke(key, 0, false));
		}
	}

	public static void addRadioButtonMenuItem(ActionListener al, JMenu m,
			String label, String command, boolean b, ButtonGroup g) {
		JRadioButtonMenuItem menuItem;
		menuItem = new JRadioButtonMenuItem(label);
		m.add(menuItem);
		menuItem.setActionCommand(command);
		menuItem.addActionListener(al);
		menuItem.setSelected(b);
		g.add(menuItem);
	}

	/**
	 * Focuses on the specified component as soon as the window becomes active.
	 * 
	 * @param win
	 *            The window
	 * @param comp
	 *            The component
	 */
	public static void requestFocus(final Window win, final Component comp) {
		win.addWindowListener(new WindowAdapter() {
			public void windowActivated(WindowEvent evt) {
				comp.requestFocus();
				win.removeWindowListener(this);
			}
		});
	}

	/**
	 * Returns if the specified event is the popup trigger event. This
	 * implements precisely defined behavior, as opposed to
	 * MouseEvent.isPopupTrigger().
	 * 
	 * @param evt
	 *            The event Taken from jEdit 3.2pre8
	 */
	public static boolean isPopupTrigger(MouseEvent evt) {
		if (OperatingSystem.isMacOS())
			return evt.isControlDown();
		else
			return ((evt.getModifiers() & InputEvent.BUTTON3_MASK) != 0);
	}

	/**
	 * Shows the specified popup menu, ensuring it is displayed within the
	 * bounds of the screen.
	 * 
	 * @param popup
	 *            The popup menu
	 * @param comp
	 *            The component to show it for
	 * @param x
	 *            The x co-ordinate
	 * @param y
	 *            The y co-ordinate taken from jEdit 4.0pre1
	 */
	public static void showPopupMenu(JPopupMenu popup, Component comp, int x,
			int y) {
		showPopupMenu(popup, comp, x, y, true);
	} //}}}

	/**
	 * Shows the specified popup menu, ensuring it is displayed within the
	 * bounds of the screen.
	 * 
	 * @param popup
	 *            The popup menu
	 * @param comp
	 *            The component to show it for
	 * @param x
	 *            The x co-ordinate
	 * @param y
	 *            The y co-ordinate
	 * @param point
	 *            If true, then the popup originates from a single point;
	 *            otherwise it will originate from the component itself. This
	 *            affects positioning in the case where the popup does not fit
	 *            onscreen. Taken from jEdit 4.0
	 */
	public static void showPopupMenu(JPopupMenu popup, Component comp, int x,
			int y, boolean point) {
		int offsetX = 0;
		int offsetY = 0;

		int extraOffset = (point ? 1 : 0);

		Component win = comp;
		while (!(win instanceof Window || win == null)) {
			offsetX += win.getX();
			offsetY += win.getY();
			win = win.getParent();
		}

		if (win != null) {
			Dimension size = popup.getPreferredSize();

			Rectangle screenSize = win.getGraphicsConfiguration().getBounds();

			if (x + offsetX + size.width + win.getX() > screenSize.width
					&& x + offsetX + win.getX() >= size.width) {
				if (point)
					x -= (size.width + extraOffset);
				else
					x = (win.getWidth() - size.width - offsetX + extraOffset);
			} else {
				x += extraOffset;
			}

			if (y + offsetY + size.height > win.getHeight()
					&& y + offsetY >= size.height) {
				if (point)
					y = (win.getHeight() - size.height - offsetY + extraOffset);
			} else {
				y += extraOffset;
			}

			popup.show(comp, x, y);
		} else
			popup.show(comp, x + extraOffset, y + extraOffset);

	}

	/**
	 * Tries to launch the application associated to the file..
	 * 
	 * @param url:
	 *            the name of the file to be executed
	 */
	public static void execute(String url) {
		boolean windows = isWindowsPlatform();
		String cmd = null;
		try {
			if (windows) {
				// cmd = 'rundll32 url.dll,FileProtocolHandler http://...'
				cmd = WIN_PATH + " " + WIN_FLAG + " " + url;
				Process p = Runtime.getRuntime().exec(cmd);
			} else {
				// Under Unix, Netscape has to be running for the "-remote"
				// command to work. So, we try sending the command and
				// check for an exit value. If the exit command is 0,
				// it worked, otherwise we need to start the browser.
				// cmd = 'netscape -remote openURL(http://www.javaworld.com)'
				cmd = UNIX_PATH + " " + UNIX_FLAG + "(" + url + ")";
				Process p = Runtime.getRuntime().exec(cmd);
				try {
					// wait for exit code -- if it's 0, command worked,
					// otherwise we need to start the browser up.
					int exitCode = p.waitFor();
					if (exitCode != 0) {
						// Command failed, start up the browser
						// cmd = 'netscape http://www.javaworld.com'
						cmd = UNIX_PATH + " " + url;
						p = Runtime.getRuntime().exec(cmd);
					}
				} catch (InterruptedException x) {
					System.err.println("Error bringing up application, cmd='"
							+ cmd + "'");
					System.err.println("Caught: " + x);
				}
			}
		} catch (IOException x) {
			// couldn't exec browser
			System.err.println("Could not invoke application, command=" + cmd);
			System.err.println("Caught: " + x);
		}
	}

	/**
	 * Try to determine whether this application is running under Windows or
	 * some other platform by examing the "os.name" property.
	 * 
	 * @return true if this application is running under a Windows OS
	 */
	public static boolean isWindowsPlatform() {
		String os = System.getProperty("os.name");
		if (os != null && os.startsWith(WIN_ID))
			return true;
		else
			return false;
	}

	// Used to identify the windows platform.
	private static final String WIN_ID = "Windows";

	// The default system browser under windows.
	private static final String WIN_PATH = "rundll32";

	// The flag to display a url.
	private static final String WIN_FLAG = "url.dll,FileProtocolHandler";

	// The default browser under unix.
	private static final String UNIX_PATH = "konqueror";

	// The flag to display a url.
	private static final String UNIX_FLAG = "-remote openURL";

}
