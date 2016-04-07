/*
* Client.java - Travel, a simple demo application
* Copyright (C) 1998-2002 Olivier Gutknecht, Jacques Ferber
* antony marchi
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

import madkit.kernel.Agent;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;
import madkit.messages.ACLMessage;

public class Client extends Agent {

    AgentAddress broker = null;

    /**
     * This method allows the user to set the "broker" agent with the G-Box
     * property sheet
     */
    public void setBroker(AgentAddress add) {
        broker = add;
    }

    /**
     * This method allows the user to get the "broker" agent address in the
     * G-Box property sheet
     */
    public AgentAddress getBroker() {
        return broker;
    }

    String produit;

    public void activate() {
        createGroup(true, "travel", "travel-clients", null, null);
        requestRole("travel", "travel-clients", "client", null);
        double rand = Math.random();
        if (rand > 0.66) {
            produit = "plane";
        } else if (rand > 0.33) {
            produit = "train";
        } else {
            produit = "bus";
        }

        println("Looking for a " + produit + " ticket");

        while (broker == null) {
            broker = getAgentWithRole("travel", "travel-clients", "broker");
            pause(500);
        }

        println("Found a broker:" + broker);
    }

    public void live() {
        println("Je veux des billets pour :" + produit);
        sendMessage(broker, new ACLMessage("REQUEST", produit));

        println("J'attends les offres...");
        while (true) {
            Message m = waitNextMessage();

            if (m instanceof ACLMessage) {
                handleMessage((ACLMessage) m);
            } else {
                System.err.println("ERROR: invalid message type: " + m);
            }
        }
    }

    protected void handleMessage(ACLMessage m) {
        try {
            println("Receiving broker answer");
            if (m.getAct().equalsIgnoreCase("MAKE-CONTRACT")) {
                String contractId = m.getContent().toString();

                createGroup(true, "travel", contractId, null, null);
                requestRole("travel", contractId, "client", null);

                println("Preparation du contrat: " + contractId);

                AgentAddress provider = null;
                while (provider == null) {
                    provider = getAgentWithRole("travel", contractId, "service");
                    pause(100);
                }
                println("Accepte ou non le ticket");
                if (Math.random() > 0.5 || !"bus".equals(produit)) {
                    println("J'accepte le ticket");
                    sendMessage(provider, new ACLMessage("VALIDATE"));
                }else{
                    //Envoie un message au provider comme quoi il le refuse
                    sendMessage(provider, new ACLMessage("REFUSE"));
                    //Repond au broker comme quoi il le refuse
                    sendMessage(m.getSender(), new ACLMessage("REFUSE"));
                    println("Je refuse le ticket");
                    pause(2000);
                    println("Donnez m'en un autre");
                    //En demande un autre
                    sendMessage(broker, new ACLMessage("REQUEST", produit));
                }
            } else if (m.getAct().equalsIgnoreCase("ACCEPT-CONTRACT")) {
                println("Contract OK.");
            }
        } catch (Exception msge) {
            System.err.println(msge.toString());
        }
    }
}
