package madkit.fourmis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import madkit.kernel.*;

public class Zone extends Agent implements MessageInterface {

    String myCommunity = "fourmis";
    String myGroup = "zone"; // change the group name to suit your needs
    String myRole = "zone"; // change the role name to suit your needs
    Map<Integer, Agent> zonePossible;
    protected List<AgentAddress> listAgent = new ArrayList<>();
    boolean alive = true;

    ZoneGUI gui;

    public Zone() {
        this.zonePossible = new HashMap<>();
    }

    /**
     *
     */
    @Override
    public void initGUI() {
        gui = new ZoneGUI(this);
        this.setGUIObject(gui);
    }

    @Override
    public void activate() {
        println("Hello I'm an agent !");
        // create a distributed group
        int g = createGroup(true, myCommunity, myGroup, null, null);
        int r = requestRole(myCommunity, myGroup, myRole, null);
    }

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

    public void addZone(Integer temps, Agent a) {
        zonePossible.put(temps, a);
    }

    void die() {
        sendMessage(getAddress(), new StringMessage("die"));
    }

    void handleMessage(StringMessage m) {
        AgentAddress ad = m.getSender();
        switch (m.getString()) {
            case WHERE:
                verifierFourmis(ad);
                break;
            case OU:
                indiquerZone(ad);
                break;
            case JEMENVAIS:
                println(ad.getName() + "pars de chez moi");
                retirerUneFourmis(ad);
            case JESUISLA:
                listAgent.add(ad);
                println("Bienvenu");
                break;
        }
    }


    @Override
    public void end() {
        println("\t That's it !!! Bye ");
    }

    protected void verifierFourmis(AgentAddress aa) {
        for (AgentAddress a : listAgent) {
            if (a.equals(aa)) {
                sendMessage(aa, new StringMessage(ICI));
                println(a.getName() + " Tu es ici");
            }
        }
    }

    protected void indiquerZone(AgentAddress aa) {
        RouteMessage routeMessage = new RouteMessage(zonePossible);
        sendMessage(aa, routeMessage);
    }
    
    protected void retirerUneFourmis(AgentAddress aa){
        for(AgentAddress a : listAgent){
            if(a.equals(aa)){
                listAgent.remove(a);
                break;
            }
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
