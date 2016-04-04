package madkit.compteur;

import madkit.kernel.*;

public class Compteur extends Agent {

    String myCommunity = "compteur";
    String myGroup = "group_compteur"; // change the group name to suit your needs
    String myRole = "compteur"; // change the role name to suit your needs

    boolean alive = true;
    boolean compter = true;

    public void activate() {
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

    /**
     * Methode appelée quand l'agent est lancé
     *
     *
     */
    public void live() {
        int n = 0;
        //Tant que l'agent est en "vie"
        while (alive) {
            //Nous récuperons le message
            Message m = nextMessage();
            //Si nous récuperons un message non vide
            if (m != null) {
                handleMessage(m); //Nous appelons la methode HandleMessage
            }
            if (this.compter) {
                n++;
                println("Compteur : " + n);
            }

            pause(1000);
        }
    }

    /**
     * Methode qui gère les actions en fonctions des messages reçus
     *
     * @args m Message : Le message
     *
     *
     */
    void handleMessage(Message m) {
        //On verifie que le message soit de type StringMessage
        if (m instanceof StringMessage) {
            //si le message est "STOP" on appèle la methode end et on tue l'agent
            if (((StringMessage) m).getString() == "stop") {
                this.end();
            }else if(((StringMessage) m).getString() == "start"){
                this.start();
            }else if(((StringMessage) m).getString() == "seppuku"){
                this.alive = false;
            }
            println("message: " + ((StringMessage) m).getString() + " : received from " + m.getSender());
        }
    }

    public void end() {
        println("Moi on m'a dit d'arreter de compter.");
        this.compter = false;
    }
    public void start() {
        println("Moi on m'a dit de continuer a compter.");
        this.compter = true;
    }
}

/**
 * **************************************************************
 *
 * File created using the MadKit designer Thanks for using MadKit - 2008
 *
 ****************************************************************
 */
