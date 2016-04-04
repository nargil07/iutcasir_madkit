package madkit.compteur;

import madkit.kernel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ControlleurGUI extends JPanel implements ActionListener {

    Controlleur ag;

    ControlleurGUI(Controlleur _ag) {
        ag = _ag;
        setLayout(new BorderLayout());
        JPanel buttonList = new JPanel();
        JButton stopButt = new JButton("Stop");
        stopButt.addActionListener(this);
        JButton startButt = new JButton("Start");
        startButt.addActionListener(this);
        JButton seppukuButt = new JButton("seppuku");
        seppukuButt.addActionListener(this);
        buttonList.add(stopButt);
        buttonList.add(startButt);
        buttonList.add(seppukuButt);
        add(buttonList, BorderLayout.NORTH);
        OPanel output = new OPanel();
        add(output, BorderLayout.CENTER);
        ag.setOutputWriter(output.getOut());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Stop")) {
            ag.stop();
        } else if (e.getActionCommand().equals("Start")) {
            ag.start();
        } else {
            ag.end();
        }
    }

}

/**
 * **************************************************************
 *
 * File created using the MadKit designer Thanks for using MadKit - 2008
 *
 ****************************************************************
 */
