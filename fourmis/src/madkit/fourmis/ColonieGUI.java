package madkit.fourmis;

import madkit.kernel.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ColonieGUI extends JPanel implements ActionListener {

    Colonie ag;

    ColonieGUI(Colonie _ag) {
        ag = _ag;
        setLayout(new BorderLayout());
        JPanel buttonList = new JPanel();
        JButton fourmisButt = new JButton("Nouvelle Fourmis");
        fourmisButt.addActionListener(this);
        buttonList.add(fourmisButt);
        add(buttonList, BorderLayout.NORTH);
        OPanel output = new OPanel();
        add(output, BorderLayout.CENTER);
        ag.setOutputWriter(output.getOut());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Nouvelle Fourmis")) {
            ag.creationFourmis();
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
