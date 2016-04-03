package madkit.%%NAME%%;

import madkit.kernel.*;

public class %%NAME%%Hello extends Agent {
	String myCommunity="%%NAME%%";
	String myGroup="myGroup";
	String myRole="myRole";
	
	boolean alive = true;
	
	public void activate(){
		println("Hello I'm an agent !");
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
		println("Hello world...");
		broadcastMessage(myCommunity, myGroup, myRole, new StringMessage("Hello to all!"));
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
