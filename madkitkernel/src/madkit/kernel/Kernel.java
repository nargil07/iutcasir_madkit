/*
* Kernel.java - Kernel: the kernel of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Fabien Michel, Jacques Ferber
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.kernel;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;
import java.awt.Point;
import java.awt.Dimension;

import java.lang.reflect.*;

// added by Saber so it will be possible to launch the netAgent while enabling mobility.
//import madkit.netcomm.NetAgent;



/** This class is the heart of the MadKit micro-kernel.  Most of these
  methods will only be useful to "system" agents developpers
  @author Ol Gutknecht
  @author Fabien Michel since MadKit 3.0
  	- org operations are now implemented in the Organization class
  	- there are also changes in messaging routines
  	- hooks are now called only when the considered operation is effectively done
  	- new features have been added:
  	  - migration of agents (for the moment for security reason, this feature has been
            more or less disabled).
  	  - communities
  @version 3.1
 */

final public class Kernel
{
	public final static int SEND_MESSAGE            = 1;
	public final static int SEND_BROADCAST_MESSAGE  = 2;
	public final static int KILL_AGENT              = 3;
	public final static int CREATE_GROUP            = 4;
	public final static int LEAVE_GROUP             = 5;
	public final static int ADD_MEMBER_ROLE         = 6;
	public final static int REMOVE_MEMBER_ROLE      = 7;
	public final static int RESTORE_AGENT	    	= 8;
	public final static int LAUNCH_AGENT            = 0;
	public final static int CONNECTED_TO            = 9;
	public final static int DISCONNECTED_FROM       = 10;
	public final static int NEW_COMMUNITY	    	= 11;
	public final static int DELETE_COMMUNITY        = 12;

	final static int HOOKS_NUMBER = 13;

	public final static int GET_GROUPS    = 20;
	public final static int GET_AGENTS    = 21;
	public final static int DUMP_ORGANIZATION = 22;
	public final static int GET_AGENTINFO = 23;
	/*public final static int BE_COMMUNICATOR = 15;
	public final static int STOP_COMMUNICATOR = 16;*/
	public final static int MIGRATION = 17;
	public final static int CONNECTION = 24;
	public final static int DECONNECTION = 25;
	public final static int DUMP_COMMUNITIES = 27;
	//public final static int GET_AVAILABLE_DESTINATIONS = 26;

	final public static String DEFAULT_COMMUNITY = "public";

	final public static String VERSION = "4.2.0 b1 - China town";

	final public static String BUGREPORT = "Please file bug reports on the madkit forum at http://www.madkit.net";

	static KernelAddress kernelAddress;
	String kernelName;		//remove it ?? No it is now the name of the agency when mobility is activated!

	private boolean debug = false;
	private Writer ostream = new OutputStreamWriter (System.err);

	private SiteAgent siteAgent;
	private KernelAgent kernelAgent;
	GraphicShell gui = null;
	private static Map localAgents = null;
	private Map organizations;
	private static ThreadGroup agentsThread=new ThreadGroup("agents");

	static boolean fastSynchronous=false;
	public static boolean agressiveHeapMode=false;
	public static int defaultAgentsAllocation=2000;
	static boolean interGroupMessage=true;

    static public ThreadGroup getAgentThreadGroup(){ return agentsThread;}

    static int agentsNb=0;

    public static int getAgentsNb()  { return agentsNb;  }  
    
    public static KernelAddress getAddress()  {    return kernelAddress;  }
    private static void setAddress(KernelAddress k)  {    kernelAddress=k;  }
	
    /*	
		Added by Saber, modifs by JF
		net : the NetAgent launched when mobility is activated. 
		JF: it is now described by an interface.
		
	*/
		static Communicator net=null;
		private Keeper KeeperAgent=null;
		
		
public void registerGUI(GraphicShell g)
{
	gui=g;
	//siteAgent.initGUI();
	//disposeGUIOf(siteAgent);
	//redisplayGUIOf(siteAgent);
}
public String getName()    {	return kernelName;    }

public Kernel(String theName, boolean ipnumeric)
{
	kernelAddress = new KernelAddress(ipnumeric);
	kernelName=theName;
	initialization();
}

public Kernel(String theName, boolean ipnumeric, String ipaddress)
{
	kernelAddress = new KernelAddress(ipnumeric,ipaddress);
	kernelName=theName;
	initialization();
}

public Kernel(String theName)
{
	this(theName, false);
}

void initialization()
{
	Organization communities = new Organization();
	organizations = new Hashtable();
	organizations.put("communities",communities);

	if(agressiveHeapMode)
	  	localAgents = new HashMap(defaultAgentsAllocation);
	else
	  	localAgents = new HashMap();
	  

	kernelAgent = new KernelAgent();
	siteAgent = new SiteAgent(organizations, kernelAgent);
	launchAgent(siteAgent,"SITEAGENT"/*+kernelAddress*/,this,false);
	System.err.println("\n\t-----------------------------------------------------");
	System.err.println("\n\t\t\t\tMadKit \n\n\t\t\t  by MadKit Team (c) 1997-2008\n");
	System.err.println("\t\t\tversion: "+VERSION+"\n");
	System.err.println("\t-----------------------------------------------------\n");
	System.err.println(BUGREPORT+"\n\n");
	displayln("MadKit Agent microKernel "+getAddress() + " is up and running");
}

///////////////////////////////////// 	FUNDAMENTAL OPERATIONS : CREATE, REQUESTROLE, LEAVEROLE & LEAVEGROUP

final int createGroup(AgentAddress creator, boolean distributed, String communityName, String groupName, String description, GroupIdentifier theIdentifier)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		organization = siteAgent.createCommunity(communityName);
	int result = organization.createGroup(creator, distributed, groupName, description, theIdentifier);
	if(result == 1)
	{
		kernelAgent.callHooks(CREATE_GROUP, creator, communityName, groupName, null);
		if(distributed)
			siteAgent.updateDistantOrgs(creator,communityName,groupName);
	}
	return result;
}

//saber contribution
final int joinPlace(Agent agent, String place, String pwd)
{
	return(joinPlace(agent,"public",place,pwd));
}

final int joinPlace(Agent agent,String Community ,String place, String pwd)
{
	return KeeperAgent.joinPlace(agent,Community,place,pwd);
}

final int createPlace(AgentAddress agent,String place, String informations)
{
			if(!isGroup("public","Mobility")) // You must belong to a mobilityApplication before
			{
					System.err.println("This application is not yet able to be a mobile one ! You must create a mobile Group first.");
					return -1;
			}
			else if(isGroup("public",place))
			{
					System.err.println("Place : "+place+" already existing!");
					return -2;// Error_Place_AlreadyExisting
			}
			else
			{
				//if(AgencyKeeper.allowCreatePlace(AgentAddress)==true) sinon creation failed
				System.err.println("Creating the placeKeeper");
				PlaceKeeper pK = null; //new placeKeeper(this,place,KeeperAgent);
				try {
					// JF: instanciation of the PlaceKeeper through reflection
					//KeeperAgent = new AgencyKeeper(getAddress(), "agencyKeeperOf : "+KernelName);		
					Class keeperClass = Utils.loadClass("madkit.mobility.PlaceKeeperAgent");
					Constructor keeperCons = keeperClass.getConstructor(
							new Class[]{Kernel.class,String.class});//,Keeper.class});
					Agent ag = (Agent) keeperCons.newInstance(new Object[]{this,place});//,KeeperAgent});			
					pK = (PlaceKeeper) ag;
					launchAgent(ag,place+":placeKeeper",this,true);
				} catch (Exception e){
						System.err.println("Cannot create a placeKeeper: check if the Mobility plugin is available");
						return -1;
				}
				if(KeeperAgent.addPlace(pK,place))
				{
					return 1; // succeeded
				}
				else
				{
					killAgent((Agent) pK);
					return -1;
				}
			}
}

/*
final int createPlace(AbstractAgent agent,String place, String pwd)
{
			if(!isGroup("public","Mobility")) // You must belong to a mobilityApplication before
			{
					System.err.println("This application is not yet able to be a mobile one ! You must create a mobile Group first.");
					return -1;// Error_MobilytGroup_NotFound
			}
			else if(isGroup("public",place))
			{
					System.err.println("Place : "+place+" already existing!");
					return -2;// Error_Place_AlreadyExisting
			}
			else
			{
					placeKeeper kP = new placeKeeper(place,pwd);
					System.err.println("Place : "+place+" created!");
					return 1;//succeed
			}
}
*/

final boolean enableMobility(String KernelName, int port) // this is the first point making the application mobile
{
			if(KeeperAgent==null) // Verify if there is already a mobility group or not
			{	try {
				// JF: instanciation of the KeeperAgent through reflection
				//KeeperAgent = new AgencyKeeper(getAddress(), "agencyKeeperOf : "+KernelName);		
				
					Class keeperClass = Utils.loadClass("madkit.mobility.AgencyKeeper");
					Constructor keeperCons = keeperClass.getConstructor(
							new Class[]{KernelAddress.class,String.class});
					Agent ag = (Agent) keeperCons.newInstance(new Object[]{getAddress(),"agencyKeeperOf : "+KernelName});			
					KeeperAgent = (Keeper) ag;
					launchAgent(ag,"agencyKeeperOf"+getAddress(),this,true);
				} catch (Exception e){
					System.err.println("Cannot create an AgencyKeeper: check if the Mobility plugin is available");
					return false;
				}
			
				kernelName=KernelName;
				kernelAddress.setKernelName(kernelName);
				kernelAddress.enableMobility();
				boolean status = launchCommunicator(port);
				
				if (status)
					System.err.println("Mobiliy activated : "+getAddress().getInformation());
				else
					System.err.println("Cannot activate mobility...");
				return status;
			}
			else
			{
						System.err.println("A mobility group is already existing!");
						return true;
			}
}

// instanciation of a communicator through reflection... 
boolean launchCommunicator(int port)
{
	if(net==null)
	{
		try
		{
			Agent ag = null;
			Class netClass = Utils.loadClass("madkit.netcomm.NetAgent");
			if (port == 0)
			{
				ag = (Agent) netClass.newInstance();
			} 
			else 
			{
				Constructor construc = netClass.getConstructor(new Class[]{int.class});
				ag = (Agent) construc.newInstance(new Object[]{new Integer(port)});
			}
			net = (Communicator) ag;
			launchAgent(ag,"SiteAgent",this,true);
			return true;
		}
		catch(Exception e)
		{
			System.err.println("Cannot create a communicator agent"+e.toString());
		}
		return false;
	} else 
		return true;
}

final boolean supportMobility()
{
	return kernelAddress.supportMobility();
}

final boolean connectAgencyToAgency(String host,int port)
{
					if(supportMobility())
					{	
						
						if(net==null)
						{
//							net = new NetAgent();
//							launchAgent(net,"NetAgent",this,true);
								launchCommunicator(port);
						}	
						net.connectAgency(kernelName,host,port);
						return true;
					}
					 System.err.println("Mobility is not supported yet!! ");
						return false;
}			

	public KernelAddress getAgencyNamed(String name)
	{
		return KeeperAgent.getAgencyNamed(name);
	}


// Is
public synchronized void launchMirror(Mirror mirror, AgentAddress address,String name, Object creator, boolean startGUI)
{
	Point position = new Point(100,100);
	Dimension dim = new Dimension(120,120);
	Agent agent = (Agent) mirror;

	if (debug)
		displayln("Agent launch: "+ name +", created by " + creator.toString());
	if (agent.getAgentInformation() == null)
	{
		agent.setAgentInformation(new AgentInformation(name, creator));
		agent.setCurrentKernel(this);
		agent.getAddress().update(address);
//		System.out.println("///On a modifié l'adresse de notre mirroir elle est "+agent.getAddress()+" La mienne est "+address);
		agent.setCurrentKernel(this);
		localAgents.put(agent.getAddress(),agent);
		///*organization.*/requestRole(agent.getAddress(), "public", "local","member",null);	//temporaire
		if (startGUI && (gui != null))
			gui.setupGUI(agent,position,dim);
		Thread thread = null;
		//System.err.println("launch "+ Thread.currentThread() +"  "+Thread.currentThread().getPriority());
		if (agent instanceof Agent)
		{
			thread = new Thread(agentsThread, (Agent) agent, agent.getName() + "_thread");
			//thread.setPriority(5);//Thread.MIN_PRIORITY);
			//System.err.println("launch "+ thread +"  "+thread .getPriority());
		}

		if (thread != null)
			thread.start();
		else
		{
			Controller c = agent.getController();
			if (c != null)
				c.activate();
			else
				agent.activate();
    }
		agentsNb++;
		kernelAgent.callHooks(LAUNCH_AGENT, agent.getAgentInformation());
	}
	else
		if (debug)
			displayln("ASSERT: agent already registred");
}

final synchronized void receiveAgent(AbstractAgent agent)
{
			Thread thread = null;
			// if (agent instanceof Agent) modified by Saber  to 	if (agent instanceof mobileAgent)
			// The reason is quite simple we can't receive an agent if he isn't a mobile one
			// The examples like ball and super ping Pong will not run FROM NOW on :(
			//
			// Saber modification
      if (agent instanceof Mobile)
			{
				thread = new Thread(/*tg,*/(Agent) agent, agent.getName() + "_thread");
				// We will verify if we are receiving one of our mobileAgents we must give him his BirthAddress unless a new one

				//(AbstractAgent)localAgents.get(agent)

				if(((Mobile)agent).getMyAgency().equals(kernelAddress))
				{
					System.out.println("One of my Mobile Agents is coming back. Welcome!");
					Mirror mirror = (Mirror)localAgents.get(((Mobile)agent).getMyBirthAddress());
					killAgent((Agent) mirror);
//					System.out.println("On a tué l'agent mirroir mais on garde sa trace.");
					((Mobile)agent).setMirrorAgent(mirror);
					agent.getAddress().update(((Mobile)agent).getMyBirthAddress());// A refaire de façon à ce que seul le kernel del'aggent puisse faire cette opération
				}
				else
	 			{
					agent.getAddress().update(kernelAddress);
				}
				agent.setCurrentKernel(this);
				localAgents.put(agent.getAddress(),agent);
				if (gui != null)
							gui.setupGUI(agent);
				if (thread != null)
					thread.start();
				else
					agent.activate();

		 }
}


final int requestRole(AgentAddress requester, String communityName, String groupName, String roleName, Object memberCard)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null)
	{
		int result = organization.requestRole(requester, groupName, roleName, memberCard);
		if(result == 1)
		{
			if(organization.isDistributed(groupName))
				siteAgent.updateDistantOrgs(ADD_MEMBER_ROLE, requester, communityName, groupName, roleName, memberCard);
			kernelAgent.callHooks(ADD_MEMBER_ROLE, requester, communityName, groupName, roleName);
		}
		return result;
	}
	else
		return -4;
}

final boolean leaveGroup(AgentAddress requester, String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null && organization.isPlayingRole(requester,groupName,"member"))
	{
		if(organization.isDistributed(groupName))
			siteAgent.updateDistantOrgs(LEAVE_GROUP, requester,communityName, groupName, null, null);
		kernelAgent.callHooks(LEAVE_GROUP, requester, communityName, groupName, null);
		if(organization.leaveGroup(requester,groupName))
			siteAgent.removeCommunity(communityName);
		return true;
	}
	return false;
}

final boolean leaveRole(AgentAddress requester, String communityName, String groupName, String roleName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null && organization.isPlayingRole(requester,groupName,roleName))
	{
		if(organization.isDistributed(groupName))
			siteAgent.updateDistantOrgs(REMOVE_MEMBER_ROLE, requester,communityName, groupName, roleName, null);
		if(organization.leaveRole(requester,groupName,roleName))
			siteAgent.removeCommunity(communityName);
		kernelAgent.callHooks(REMOVE_MEMBER_ROLE, requester, communityName, groupName, roleName);
		return true;
	}
	return false;
}

final boolean isBelongingToGroup(AgentAddress who, String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null && organization.isPlayingRole(who,groupName,"member"))
	    return true;
	return false;
}

//////////////////////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////// OVERLOOKING

/////////////////////////////////////////////////////////////////////////////////////////
final boolean addOverlooker(AgentAddress requester, Overlooker o, Object accessCard)
{
	Organization organization = getOrganizationFor(o.community);
	if(organization == null)
		return false;
	return organization.addOverlooker(requester, o, accessCard);
}

final void removeOverlooker(Overlooker o)
{
	Organization organization = getOrganizationFor(o.community);
	if(organization != null && organization.removeOverlooker(o))
		siteAgent.removeCommunity(o.community);
}
///////////////////////////////////////////////////////////////


////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////   AGENT LAUNCHING & KILLING

////////////////////////////////////////////////////////////////////////////////////

final public synchronized void launchAgent(AbstractAgent agent, String name, Object creator, boolean startGUI){
	launchAgent(agent,name,creator,startGUI,new Point(-1,-1),new Dimension(-1,-1));
}
final public synchronized void launchAgent(AbstractAgent agent, String name, 
											Object creator, boolean startGUI, Point position, Dimension dim)
{
	if (debug)
		displayln("Agent launch: "+ name +", created by " + creator.toString());
	if (agent.getAgentInformation() == null)
	{
		agent.setAgentInformation(new AgentInformation(name, creator));
		agent.setCurrentKernel(this);
		localAgents.put(agent.getAddress(),agent);
		///*organization.*/requestRole(agent.getAddress(), "public", "local","member",null);	//temporaire
		if (startGUI && (gui != null))
			gui.setupGUI(agent,position,dim);
		Thread thread = null;
		//System.err.println("launch "+ Thread.currentThread() +"  "+Thread.currentThread().getPriority());
		if (agent instanceof Agent)
		{
			thread = new Thread(/*agentsThread,*/ (Agent) agent, agent.getName() + "_thread");
			//thread.setPriority(5);//Thread.MIN_PRIORITY);
			//System.err.println("launch "+ thread +"  "+thread .getPriority());
		}

		if (thread != null)
			thread.start();
		else
		{
			Controller c = agent.getController();
			if (c != null)
				c.activate();
			else
				agent.activate();
        	}
		agentsNb++;
		kernelAgent.callHooks(LAUNCH_AGENT, agent.getAgentInformation());
	}
	else
		if (debug)
			displayln("ASSERT: agent already registred");
}

/** Kill a given agent (from another agent). */
final void killAgent(AbstractAgent target, AbstractAgent killer)
{
	// Basically a wrapper verifying rights for the caller to terminate its mate */
	if (target!=null && target.getAgentInformation()!=null && (killer.getAddress().equals(target.getAgentInformation().getOwner()) || target == killer))
		killAgent(target);
}

/** Kill a given agent (also manage groups update) */
final synchronized public void killAgent(AbstractAgent a)
{
	if(a.getCurrentKernel() == null)
		return;
	if (a instanceof Agent && Thread.currentThread() != ((Agent)a).getAgentThread())
		((Agent)a).getAgentThread().stop();
	Controller c = a.getController();
	if (c != null)
		c.end();
	else
		a.end();
	removeAgentFromOrganizations(a.getAddress());
	localAgents.remove(a.getAddress());
	if (gui != null)
		gui.disposeGUI(a);
	a.setCurrentKernel(null);
	kernelAgent.callHooks(KILL_AGENT, a.getAgentInformation());
	agentsNb--;
        //debugString();
}
//final synchronized public void killMirror(Mirror mirror)
//{
//	if(mirror.getCurrentKernel() == null)
//		return;
//	if (Thread.currentThread() != mirror.getAgentThread())
//		mirror.getAgentThread().stop();
//	Controller c = mirror.getController();
//	if (c != null)
//		c.end();
//	else
//		mirror.end();
//
//	removeAgentFromOrganizations(mirror.getAddress());
//	localAgents.remove(mirror.getAddress());
//	if (gui != null)
//		gui.disposeGUI(mirror);
//	mirror.setCurrentKernel(null);
//	kernelAgent.callHooks(KILL_AGENT, mirror.getAgentInformation());
//	agentsNb--;
//}

final synchronized void restoreAgent(AbstractAgent agent, String name, Object creator, boolean startGUI)
{
	if (debug)
		displayln("Agent restoration: "+ name +", restored by " + creator.toString());
	if (agent.getAgentInformation() != null)
	{
		agent.setAgentInformation(new AgentInformation(name, creator));
		agent.setCurrentKernel(this);
		localAgents.put(agent.getAddress(),agent);

		/*organization.*/
		//requestRole(agent.getAddress(),"public", "local","member",null);		//temporaire

		if (startGUI && (gui != null))
			gui.setupGUI(agent);
		Thread thread = null;
		if (agent instanceof Agent)
			thread = new Thread((Agent) agent, agent.getName() + "_thread");
		if (thread != null)
			thread.start();
		else
			agent.activate();
		kernelAgent.callHooks(RESTORE_AGENT, agent.getAgentInformation());
	}
	else
		if (debug)
	    		displayln("ASSERT: restoration impossible: agent has not been previously launched");
}

final synchronized private void killAgents()	// BUGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG
{
	AgentAddress myAgent = kernelAgent.getAddress();
	for(Iterator i=localAgents.keySet().iterator(); i.hasNext(); )
	{
		AgentAddress next = (AgentAddress) i.next();
		if(! next.equals(myAgent))
			removeAgentFromOrganizations(next);
		i.remove();
	}
}

final synchronized void removeReferenceOf(AgentAddress agent)
{
	if (gui != null)
		try
		{
			gui.disposeGUI((AbstractAgent)localAgents.get(agent));
		}
		catch(Exception e){}
	localAgents.remove(agent);
}



///////////////////////////////////////////////////////////////////////////////////////////

////////////////////////////////////////////	MESSAGING

/** Send a message. If the the siteAgent has a communicator it will delegate the message to it
   @param sender Agent requesting the send
   @param receiver Destination agent
   @param m the Message itself
   @see Message*/
final void sendMessage(Message m)
{
	try
	{
		if (m.getReceiver().isLocal())
		{
			sendLocalMessage((Message)m.clone());
			if (!(m instanceof  PrivateMessage))
				kernelAgent.callHooks(SEND_MESSAGE, m);
		}
		else
			if(siteAgent.sendDistantMessage(m) && !(m instanceof PrivateMessage))
				kernelAgent.callHooks(SEND_MESSAGE, m);
	}
	catch(MessageException mexc)
	{
		if (debug)
			siteAgent.debug("Unable to send message "+m+" : "+mexc);
	}
}

/** Sends a local message
   @param m the message itself
   @see Message*/
final void sendLocalMessage(Message m) throws MessageException
{
	AbstractAgent a = (AbstractAgent) localAgents.get(m.getReceiver());
	if (a != null)
		a.receiveMessage(m);
	else
		throw new MessageException("Unknown agent");
}

/** Sends a broadcast message. If a specialized system agent can handle distributed message, the kernel will delegate the message to it
   @param groupName Group
   @param roleName Role
   @param m the Message itself
   @see Message*/
final void sendBroadcastMessage(String communityName, String groupName, String roleName, Message m)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization != null)
	{
		AgentAddress[] receivers = organization.getRolePlayers(groupName, roleName);
		if (!(m instanceof PrivateMessage))
		{
			Vector h = new Vector();
			h.addElement(groupName);
			h.addElement(roleName);
			h.addElement(m);
			kernelAgent.callHooks(SEND_BROADCAST_MESSAGE, h);
		}
		for(int i = 0; i< receivers.length;i++)
		{
			Message m2 = (Message) m.clone();
			m2.setReceiver(receivers[i]);
			try
			{
				if (receivers[i].isLocal())
					sendLocalMessage(m2);
				else
					siteAgent.sendDistantMessage(m2);
			}
			catch(MessageException mexc)
			{
				if (debug)
					siteAgent.debug("Unknown agent");
			}
		}
	}
}

////////////////////////////////////////////////// internal methods

final static synchronized AbstractAgent getReference(Object agent)
{
	return (AbstractAgent) localAgents.get(agent);
}

final static synchronized AgentAddress[] getLocalAgents()
{
	return (AgentAddress[]) localAgents.keySet().toArray(new AgentAddress[0]);
}



///////////////////////////////////////////////// ORGANIZATIONS MANAGEMENT
final synchronized Organization getOrganizationFor(String communityName)
{
	return (Organization) organizations.get(communityName);
}

final synchronized boolean isGroup(String community, String groupName)
{
	Organization organization = getOrganizationFor(community);
	if(organization != null)
		return organization.isGroup(groupName);
	return false;
}

final synchronized public String[] getCurrentGroupsOf(AgentAddress theAgent, String communityName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getCurrentGroupsOf(theAgent);
}

final synchronized public String[] getExistingGroups(String communityName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getGroups();
}

final synchronized public AgentAddress[] getRolePlayers(String communityName, String groupName, String roleName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new AgentAddress[0];
	return organization.getRolePlayers(groupName, roleName);
}

final synchronized AgentAddress getRolePlayer(String communityName, String groupName, String roleName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return null;
	return organization.getRolePlayer(groupName, roleName);
}

final public synchronized String[] getGroupRolesOf(AgentAddress agent, String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getGroupRolesOf(agent, groupName);
}

final synchronized public String[] getExistingRoles(String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new String[0];
	return organization.getRolesIn(groupName);
}

final synchronized boolean isCommunity(String communityName)
{
	Organization organization = getOrganizationFor("communities");
	return  (organization.isPlayingRole(siteAgent.getAddress(),communityName,"member") || organization.getRolePlayer(communityName,"site") != null);
}

final synchronized String[] getCommunities()
{
	return siteAgent.getCommunities();
}

final synchronized boolean connectedWithCommunity(String communityName)
{
	return getOrganizationFor("communities").isPlayingRole(siteAgent.getAddress(), communityName, "site");
}

final synchronized void removeAgentFromOrganizations(AgentAddress theAgent)	// must be optimized
{
	Map groupNames = new HashMap();
	for(Iterator i = organizations.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		Organization org = (Organization) e.getValue();
		groupNames.put(e.getKey(),org.getCurrentGroupsOf(theAgent));
	}
	for(Iterator i = groupNames.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		String[] groups = (String[]) e.getValue();
		for(int j=0;j<groups.length;j++)
			leaveGroup(theAgent,(String) e.getKey(),groups[j]);
	}
}

synchronized Map getDumpCommunities(){
    Map res = new HashMap();
    for(Iterator i = organizations.entrySet().iterator();i.hasNext();){
        Map.Entry e = (Map.Entry) i.next();
        res.put(e.getKey(), ((Organization) e.getValue()).getLocalOrganization());
    }
    return res;
}

/////////   redundant & facilities
/**@return the addresses of all the agents who are members of $groupName$*/
final synchronized public AgentAddress[] getMembersWithin(String communityName, String groupName)
{
	Organization organization = getOrganizationFor(communityName);
	if(organization == null)
		return new AgentAddress[0];
	return organization.getRolePlayers(groupName,"member");
}
/////////////////////////////////////////////////////////////////////////////////////



///////////////////////////////////////////////   Others
/** Request a kernel stop. All agents are (hopefully) cleanly killed */
public void stopKernel()
{
      displayln("Disconnecting MadKit Kernel: " + getName());
      displayln("- killing local agents " + getName());
      killAgents();
      displayln("MadKit Kernel closed.");
      System.exit(0);
}

/** A generic display method adapting its output to the kernel environment (console, GUI, applet...)
	@param s string to be displayed, add a newline at the end of the string */
public void displayln(String s)
{
    display(s+'\n');
}

/** A generic display method adapting its output to the kernel environment (console, GUI, applet...)
	@param s string to be displayed */
public void display(String s)
{
//	try
//	{
//		ostream.write("<" + getName() + "> : " + s);
//		ostream.flush();
              System.err.print("<" + getName() + "> : " + s);
//	}
////	catch (IOException e)      {
//          System.err.println(e.toString());
//        }
}

/** Reassigns the "standard" agent text output stream (used by println method).  */
public void setOutputStream(Writer o)  {    ostream = o;  }

final void disposeGUIOf(AbstractAgent theAgent)
{
	if (gui != null)
		gui.disposeGUIImmediatly(theAgent);
}

final void redisplayGUIOf(AbstractAgent theAgent)
{
	if (gui != null)
		gui.setupGUI(theAgent);
}

/////////////////////////////////////////////////////////////////////////////////////////////////:

//////////////////////////////////////////////	DEPRECATED METHODS

/**@deprecated As of MadKit 3.0. replaced by {@link #getCurrentGroupsOf(AgentAddress, String)}*/
public Vector getCurrentGroups(AgentAddress theAgent)
{
	Vector v = new Vector();
	String[] groups = getCurrentGroupsOf(theAgent, Kernel.DEFAULT_COMMUNITY);
	for(int i = 0;i<groups.length;i++)
		v.addElement(groups[i]);
	return v;
}

/**@deprecated As of MadKit 3.0. replaced by {@link #getMembersWithin(String, String)}*/
synchronized public Enumeration getGroupMembers(String theGroup)
{
	Collection c = new HashSet();
	AgentAddress[] addresses = getMembersWithin(theGroup, Kernel.DEFAULT_COMMUNITY);
	for(int i=0;i< addresses.length;i++)
		c.add(addresses[i]);
	return Collections.enumeration(c);
}

/**@deprecated As of MadKit 3.0. replaced by {@link #getGroupRolesOf(AgentAddress, String, String)}please use getGroupRolesOf instead*/
synchronized public Vector getMemberRoles(String theGroup, AgentAddress theAgent)
{
	Vector v = new Vector();
	String[] roles = getGroupRolesOf(theAgent, Kernel.DEFAULT_COMMUNITY, theGroup);
	for(int i = 0;i<roles.length;i++)
		v.addElement(roles[i]);
	return v;
}

void synchronizeKernel(Map orgs, boolean priority)
{
	for(Iterator i = orgs.entrySet().iterator();i.hasNext();)
	{
		Map.Entry e = (Map.Entry) i.next();
		if(siteAgent.connectedWith((String) e.getKey()))
		{
			if (! organizations.containsKey(e.getKey()))
				organizations.put(e.getKey(),new Organization());
			( (Organization) organizations.get(e.getKey()) ).importOrg((Organization)(e.getValue()),priority);
		}
	}
	siteAgent.refreshCommunities();
}

public static void debugString()
{
		System.err.println("--------------------------------------kernel status");
		ThreadGroup tg = Thread.currentThread().getThreadGroup();
		Thread[] temp=new Thread[tg.activeCount()];
		tg.enumerate(temp);
		for(int i = 0; i< temp.length; i++)
		{
			if (temp[i] != null)
				System.err.println(""+i+":  "+temp[i]+" is demon "+temp[i].isDaemon());
		}
		System.err.println("used memory: "+(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory()));
		System.err.println("locals agents are "+localAgents.size());
}
}
