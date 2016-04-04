package madkit.compteur;

import madkit.kernel.*;

public class Controlleur extends Agent {

    ControlleurGUI gui;

    public void initGUI() {
        gui = new ControlleurGUI(this);
        this.setGUIObject(gui);
    }

    String myCommunity = "compteur";
    String myGroup = "group_compteur"; // change the group name to suit your needs
    String myRole = "stoppeur"; // change the role name to suit your needs

    boolean alive = true;

    public void activate() {
        println("Bonjour, je suis l'agent qui controlle les compteur");
        // create a distributed group
        int g = createGroup(true, myCommunity, myGroup, null, null);
        if (g == 1) {
            println("I create the group " + myGroup + " in community " + myCommunity);
        } else if (g == -1) {
            println("The group has already been created");
        } else {
            println("Cannot create the group");
        }

        int r = requestRole(myCommunity, myGroup, myRole, null);
        if (r == 1) {
            println("I play " + myRole + " in " + myGroup + " of " + myCommunity);
        } else if (r == -1) {
            println("I cannot enter the group: access denied");
        } else if (r == -2) {
            println("I already play the role " + myRole + " in " + myGroup + " of " + myCommunity);
        } else if (r == -3) {
            println("The group " + myGroup + " does not exist int the community " + myCommunity);
        } else if (r == -4) {
            println("The community " + myCommunity + " does not exist");
        }
    }

    void handleMessage(Message m) {
        // You should describe here the agent's behavior
        // upon reception of a message
        if (m instanceof StringMessage) {
            println("message: " + ((StringMessage) m).getString() + " : received from " + m.getSender());
        }
    }

    public void live() {
        while (alive) {
        }
    }

    public void end() {
        broadcastMessage(myCommunity, myGroup, "compteur", new StringMessage("seppuku"));
        println("\t BANZAIIIIIIIII ");
    }

    public void stop() {
        broadcastMessage(myCommunity, myGroup, "compteur", new StringMessage("stop"));
    }

    public void start() {
        broadcastMessage(myCommunity, myGroup, "compteur", new StringMessage("start"));
    }
}

/**
 * **************************************************************
 *
 * File created using the MadKit designer Thanks for using MadKit - 2008
 *
 ****************************************************************
 */
