package madkit.fourmis;

import java.util.HashMap;
import java.util.Map;
import madkit.kernel.*;

public class Zone extends Agent {

    String myCommunity = "fourmis";
    String myGroup = "zone"; // change the group name to suit your needs
    String myRole = "zone"; // change the role name to suit your needs
    Map<Integer,Agent> zonePossible;
    boolean alive = true;

    ZoneGUI gui;

    public Zone() {
        this.zonePossible = new HashMap<>();
    }

    public void initGUI() {
        gui = new ZoneGUI(this);
        this.setGUIObject(gui);
    }

    public void activate() {
        println("Hello I'm an agent !");
        // create a distributed group
        int g = createGroup(true, myCommunity, myGroup, null, null);
        int r = requestRole(myCommunity, myGroup, myRole, null);
    }

    public void live() {
        while (alive) {
            this.exitImmediatlyOnKill(); //to be sure the agent thread can be really stopped
            Message m = waitNextMessage();
            if (m instanceof StringMessage) {
                handleMessage((StringMessage) m);
            }
        }
    }
    
    public void addZone(Integer temps, Agent a){
        zonePossible.put(temps,a);
    }

    void die() {
        sendMessage(getAddress(), new StringMessage("die"));
    }

    void handleMessage(StringMessage m) {
        
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
