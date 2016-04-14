package madkit.fourmis;

import madkit.kernel.*;

public class Launcher extends Agent {

    String myCommunity = "fourmis";
    String myGroup = "myGroup"; // change the group name to suit your needs
    String myRole = "myRole"; // change the role name to suit your needs

    boolean alive = true;

    public void activate() {
        Colonie c = new Colonie();
        Zone z1, z2,z3;
        z1 = new Zone();
        z2 = new Zone();
        z3 = new Zone();
        c.addZone(2,z1);
        c.addZone(4,z2);
        z1.addZone(2,c);
        z2.addZone(4,c);
        this.launchAgent(c, "colonie1", true);
        this.launchAgent(z1, "zone1", true);
        this.launchAgent(z2, "zone2", true);
        this.launchAgent(z3, "zone3", true);
    }

    public void live() {
    }

    public void end() {
        println("\t That's it !!! Bye ");
    }
}

/**
 * **************************************************************
 *
 * File created using the MadKit designer Thanks for using MadKit - 2008
 *
 ****************************************************************
 */
