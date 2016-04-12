package madkit.fourmis;

import madkit.kernel.*;

public class Fourmis extends Agent implements MessageInterface{
	String myCommunity="fourmis";
	String myGroup="groupe"; // change the group name to suit your needs
	String myRole="chercheuse"; // change the role name to suit your needs
	
	boolean alive = true;
	
	public void activate(){
		println("Bonjour je suis une fourmis");
		// create a distributed group
		int g = createGroup(true, myCommunity, myGroup, null, null);
		if (g == 1)
			println("I create the group "+myGroup+" in community "+myCommunity);
		else if (g == -1)
			println("The group has already been created");
		else
			println("Cannot create the group");
			
		int r = requestRole(myCommunity, myGroup, myRole,null);
		if (r == 1)
			println("I play "+ myRole + " in " + myGroup + " of " + myCommunity);
		else if (r == -1)
			println("I cannot enter the group: access denied");
		else if (r == -2)
			println("I already play the role "+myRole+" in " + myGroup + " of " + myCommunity);
		else if (r == -3)
			println("The group "+myGroup+" does not exist int the community "+myCommunity);
		else if (r == -4)
			println("The community "+myCommunity+" does not exist");
	}
	
	public void live()
	{
		broadcastMessage(myCommunity, "zone", "colonie", new StringMessage(WHERE));
                
                println(WHERE);
		while(alive){
			Message m = waitNextMessage();
			handleMessage(m);
		}
	}
	
	void handleMessage(Message m){
		// You should describe here the agent's behavior
		// upon reception of a message
		if (m instanceof StringMessage){
			println("message: "+((StringMessage) m).getString() + " : received from "+m.getSender());
		}
	}
	
	public void end()
	{
		println("\t That's it !!! Bye ");
	}
}

/****************************************************************
  
   File created using the MadKit designer
   Thanks for using MadKit - 2008
   
*****************************************************************/
