package madkit.fourmis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import madkit.kernel.*;

public class Fourmis extends Agent implements MessageInterface {

    String myCommunity = "fourmis";
    String myGroup = "groupe"; // change the group name to suit your needs
    String myRole = "chercheuse"; // change the role name to suit your needs

    boolean alive = true;

    public void activate() {
        println("Bonjour je suis une fourmis");
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

    public void live() {

        while (alive) {
            broadcastMessage(myCommunity, "zone", "colonie", new StringMessage(WHERE));
            broadcastMessage(myCommunity, "zone", "zone", new StringMessage(WHERE));
            println(WHERE);
            Message m = waitNextMessage();
            handleMessage(m);
        }
    }

    synchronized void handleMessage(Message m) {
        if (m != null) {
            AgentAddress ad = m.getSender();
            if (m instanceof StringMessage) {

                switch (((StringMessage) m).getString()) {
                    case ICI:
                        println(OU);
                        sendMessage(ad, new StringMessage(OU));
                        enAttenteDesChemins();
                        break;
                }
            } else if (m instanceof RouteMessage) {
                Agent agentSelected;
                HashMap<Integer, Agent> routes = (HashMap<Integer, Agent>) (Map<Integer, Agent>) ((RouteMessage) m).getContent();
                Object[] listesClefs = routes.keySet().toArray();
                Integer distance = (Integer) listesClefs[new Random().nextInt(listesClefs.length)];
                agentSelected = routes.get(distance);
                println("Je vais voir la " + agentSelected.getName());
                sendMessage(ad, new StringMessage(JEMENVAIS));
                try {
                    wait(distance * 1000);
                    sendMessage(agentSelected.getAddress(), new StringMessage(JESUISLA));
                } catch (InterruptedException ex) {
                    Logger.getLogger(Fourmis.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        }
        
        try {
            wait(new Random().nextInt(5) * 1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Fourmis.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    void enAttenteDesChemins() {
        Message m = waitNextMessage(5000);
        handleMessage(m);
    }

    @Override
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
