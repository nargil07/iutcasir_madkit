/*
* Broker.java - Travel, a simple demo application
* Copyright (C) 1998-2002 Olivier Gutknecht, Jacques Ferber
* @author Antony marchi
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package marketorg;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JPanel;

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.kernel.OPanel;
import madkit.messages.ACLMessage;

/**
 * Contains the bid of a provider. This class is used by the Broker to choose
 * the best offer among the bids of the providers
 */
class BidAnswer {

    AgentAddress bidder;
    int value;

    BidAnswer(AgentAddress b, int v) {
        bidder = b;
        value = v;
    }

    int getValue() {
        return value;
    }

    AgentAddress getBidder() {
        return bidder;
    }
}

public class Broker extends Agent implements ActionListener {

    Vector bidders = new Vector();
    BidAnswer[] answers;
    int contract = 0;
    JButton b;
    int cpt = 0;
    AgentAddress client;
    List<AgentAddress> providerRefuse = new ArrayList<>();
    boolean refuse = false;

    boolean pause = false;

    public void enterPause() {
        pause = true;
        b.setBackground(Color.red);
        b.repaint();

        while (pause) {
            pause(200);
        }
    }

    public void initGUI() {
        JPanel p = new JPanel(new BorderLayout());
        OPanel o = new OPanel();
        p.add("Center", o);
        b = new JButton("Next step");
        b.addActionListener(this);
        p.add("South", b);
        setGUIObject(p);
        setOutputWriter(o.getOut());//new GUIPrintWriter(o.ge.outField));
    }

    public void actionPerformed(ActionEvent e) {
        pause = false;
        b.setBackground(Color.gray);
    }

    public void activate() {
        println("Broker ready");

        createGroup(true, "travel", "travel-providers", null, null);
        createGroup(true, "travel", "travel-clients", null, null);
        requestRole("travel", "travel-providers", "broker", null);
        requestRole("travel", "travel-clients", "broker", null);
    }

    public void handleMessage(ACLMessage m) {
        String product = null;

        if (m.getAct().equalsIgnoreCase("REQUEST")) {

            println("Reception d'une demande client.");
            enterPause();

            product = m.getContent().toString();
            client = m.getSender();

            AgentAddress[] bidders = getAgentsWithRole("travel", "travel-providers", product + "-provider");
            if (bidders.length == 0) {
                println("Pas de point de vente trouvé " + product);
                return;
            }
            println("Found providers of " + product);
            cpt = bidders.length;
            answers = new BidAnswer[cpt];

            println("Envoie des demandes d'offres");

            ACLMessage req = new ACLMessage("REQUEST-FOR-BID", product);
            broadcastMessage("travel", "travel-providers", product + "-provider", req);

            println("En attente d'offre ....");
        } // reception des propositions
        else if (m.getAct().equalsIgnoreCase("BID")) {
            receiveBid(m);
        }
        else if(m.getAct().equalsIgnoreCase("REFUSE")){
            println("Le client a refuser mon bus");
            refuse = true;
            pause = false;
            
        }
    }

    /**
     *
     */
    @Override
    public void live() {
        while (true) {
            Message m = waitNextMessage();

            if (m instanceof ACLMessage) {
                handleMessage((ACLMessage) m);
            } else {
                System.err.println("ERROR: invalid message type: " + m);
            }
        }
    }

    protected void receiveBid(ACLMessage m) {
        AgentAddress b = m.getSender();
        println("Reception d'une offre de " + m.getContent() + " venant de " + m.getSender());
        cpt--;
        answers[cpt] = new BidAnswer(m.getSender(), ((Integer) new Integer(m.getContent().toString())).intValue());
        if (cpt <= 0) {
            bestContract();
        }
    }

    /**
     * Selection du meilleurs contrat.
     */
    void bestContract() {
        AgentAddress best = null;
        int bestoffer = 9999;
        println("Selection de la meilleures offres entre " + answers.length + " proposition");
        enterPause();
        for (int i = 0; i < answers.length; i++) {
            println(":: meilleur: " + bestoffer + ", value: " + answers[i].getValue());
            if (answers[i].getValue() < bestoffer && !verifRefuser(answers[i].getBidder())) {
                best = answers[i].getBidder();
                bestoffer = answers[i].getValue();
            }
        }
        if (best != null && !verifRefuser(best)) {
            println("On choisis le provider :" + best);
            println("  avec " + bestoffer + " F");
            contract++;
            println("Demande de confirmation au Provider");

            enterPause();
            sendMessage(best,
                    new ACLMessage("MAKE-CONTRACT", "contract-" + contract));
            pause(100);

            println("Demande de confirmation au client...");
            sendMessage(client,
                    new ACLMessage("MAKE-CONTRACT", "contract-" + contract));
            
            Message m = waitNextMessage();
            handleMessage((ACLMessage) m);
            if(refuse){
                println("Ajout de l'offre " + best.getName() + " en refusé");
                providerRefuse.add(best);
            }else{
                
            }
            
        }else {
            println("Je n'ai pas d'offres");
        }
    }
    
    private boolean verifRefuser(AgentAddress offer){
        boolean result = false;
        for(AgentAddress agentAddress : providerRefuse){
            println("Nombre d'agent refusé : " + providerRefuse.size());
            println(agentAddress.toString() + " VS " + offer.toString());
            if(agentAddress == offer){
                result = true;
                break;
            }
        }
        return result;
    }
}
