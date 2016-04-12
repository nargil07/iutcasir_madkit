package madkit.fourmis;

import java.util.ArrayList;
import java.util.List;
import madkit.kernel.*;

public class Colonie extends Agent implements MessageInterface {

    String myCommunity = "fourmis";
    String myGroup = "zone";
    String myRole = "colonie";
    int nbFourmisCreer = 0;
    List<Agent> listAgent = new ArrayList<>();

    boolean alive = true;

    ColonieGUI gui;

    @Override
    public void initGUI() {
        gui = new ColonieGUI(this);
        this.setGUIObject(gui);
    }

    @Override
    public void activate() {
        println("Je suis une colonie ! Et je peux créer des fourmis");

        int g = createGroup(true, myCommunity, myGroup, null, null);
        switch (g) {
            case 1:
                println("I create the group " + myGroup + " in community " + myCommunity);
                break;
            case -1:
                println("The group has already been created");
                break;
            default:
                println("Cannot create the group");
                break;
        }

        int r = requestRole(myCommunity, myGroup, myRole, null);
        switch (r) {
            case 1:
                println("I play " + myRole + " in " + myGroup + " of " + myCommunity);
                break;
            case -1:
                println("I cannot enter the group: access denied");
                break;
            case -2:
                println("I already play the role " + myRole + " in " + myGroup + " of " + myCommunity);
                break;
            case -3:
                println("The group " + myGroup + " does not exist int the community " + myCommunity);
                break;
            case -4:
                println("The community " + myCommunity + " does not exist");
                break;
            default:
                break;
        }
    }

    /**
     *
     */
    @Override
    public void live() {
        while (alive) {
            this.exitImmediatlyOnKill(); //to be sure the agent thread can be really stopped
            
            Message m = waitNextMessage();
            if (m instanceof StringMessage) {
                handleMessage((StringMessage) m);
            }
        }
    }

    void die() {
        sendMessage(getAddress(), new StringMessage("die"));
    }

    void handleMessage(StringMessage m) {

        println("message: " + m.getString() + " : received from " + m.getSender());
    }

    void creationFourmis() {
        println(CREATE);
        Fourmis f = new Fourmis();
        launchAgent(f, "fourmis" + (++nbFourmisCreer), true);
        listAgent.add(f);
    }

    /**
     *
     */
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
