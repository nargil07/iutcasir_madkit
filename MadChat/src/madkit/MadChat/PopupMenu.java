package madkit.MadChat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PopupMenu extends MouseAdapter {

	private JPopupMenu p;

	PopupMenu(JPopupMenu popup) {
		p = popup;
	}

	public void mouseReleased(MouseEvent e) {
		if (e.isPopupTrigger()) {
			p.show((Component) e.getSource(), e.getX(), e.getY());
		}
	}
}