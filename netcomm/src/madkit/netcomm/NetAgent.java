/*
 * NetAgent.java - Created on Oct 18, 2003
 * 
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.

 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2008/06/23 12:12:38 $
 */

package madkit.netcomm;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.StringTokenizer;

import madkit.boot.Madkit;
import madkit.kernel.AgentAddress;
import madkit.kernel.KernelAddress;
import madkit.kernel.KernelMessage;
import madkit.kernel.Message;
import madkit.kernel.NetworkRequest;
import madkit.kernel.StringMessage;
import madkit.kernel.Communicator;

// Added by Saber
import java.lang.ClassLoader;
import java.net.*;
// End of saber contribution
/**
 * The NetAgent replaces the Communicator. For the outside agents
 * the communications behave the save way.
 * The NetAgent can be configured to automatically broadcast the presence of the Kernel using @link NetAgent# setAutoBroadcast()
 * 
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 * 
 * @version $Revision: 1.6 $
 */

public class NetAgent extends MadkitNetworkAgent implements Communicator {

	static final int DEFAULT_PORT=4444;

	/**
	 * 
	 * @uml.property name="socketInfo"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private SocketKernel socketInfo;

	/**
	 * 
	 * @uml.property name="router"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private RouterAgent router;

	/**
	 * 
	 * @uml.property name="tcpAgent"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private TCPServerAgent tcpAgent;

	/**
	 * 
	 * @uml.property name="udpAgent"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private UDPServerAgent udpAgent;

	/**
	 * 
	 * @uml.property name="broadcast"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private BroadCastClient broadcast;

	/**
	 * 
	 * @uml.property name="stats"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private StatsAgent stats;

	
	
	private boolean alive=true;
	private boolean autoBroadcast=false;

	/**
	 * 
	 * @uml.property name="gui"
	 * @uml.associationEnd multiplicity="(0 1)"
	 */
	private NetAgentGUI gui;
  
	private int port;
	
	public NetAgent()
	{
		this(DEFAULT_PORT);
	}
	
	public NetAgent(int p)
	{
		port = p;
		tcpAgent=new TCPServerAgent(port);
		socketInfo=tcpAgent.getSocketKernel();
		router=new RouterAgent(socketInfo);
		
		//LoggerConfigurator.configure();
	}
	
	
	/* (non-Javadoc)
	 * @see madkit.kernel.Agent#live()
	 */
	public void live() 
	{
		if(autoBroadcast){
			pause(200); //buy some time to the agents before broadcasting
			madkitBroadcast();
		}
		
		while(alive){
			Message msg=waitNextMessage();
			exitImmediatlyOnKill();
			if(!alive) return;
			if(msg instanceof KernelMessage){
				//give the message to the router agent to find 
				//the P2PAgent in charge of the connection
				sendMessage(router.getAddress(),new NetworkMessage(NetworkMessage.ROUTE_MESSAGE,(KernelMessage)msg));
			}else if(msg instanceof NetworkMessage){
				handleNetworkMessage((NetworkMessage) msg);				
			}else if(msg instanceof NetConfigMessage){
				handleConfigMessage((NetConfigMessage) msg);
			}else if (msg instanceof StringMessage){
				handlePersonalMessage((StringMessage) msg);
			}else{
				debug("unknown message type");
				debug(msg.toString());
			}
			
		}
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#activate()
	 */
	public void activate() 
	{
		// setDebug(Madkit.debug);
		debug("NetAgent Activated");

		if(hasGUI()) gui.setStatus("Active");
		//if(!isGroup(community,group)){
			
		//}
		requestRole(community,group,"netagent",null);
		
		myKernel = getAgentWithRole("communications","site");
		sendMessage(myKernel, new NetworkRequest(NetworkRequest.BE_COMMUNICATOR));
		
		//launch the agents
		
		launchAgent(tcpAgent,"TCPSocketAgent",false);

		launchAgent(router,"RouterAgent",false);
		
		udpAgent=new UDPServerAgent(port);
		launchAgent(udpAgent,"udpserveragent",false);
		
		broadcast=new BroadCastClient();
		launchAgent(broadcast,"broadcastclient",false);
		
		stats=new StatsAgent(NetConfigMessage.isEnableStat());
		//launchAgent(stats,"statsagent",true);
		
		SocketDynamicConnection smc=new SocketDynamicConnection(socketInfo);
		launchAgent(smc,SocketDynamicConnection.ROLE,false);
	//	
	}

	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#end()
	 */
	public void end() 
	{
		super.end();
		sendMessage(myKernel, new NetworkRequest(NetworkRequest.STOP_COMMUNICATOR));
		//killAgent(broadcast);
		killAgent(tcpAgent);
		killAgent(udpAgent);
		killAgent(router);
		killAgent(stats);
		broadcastMessage(community,group,"p2pagent",new NetworkMessage(NetworkMessage.DIE,null));
		alive= false;
		println("NetAgent Terminated");
		System.gc();
		disposeMyGUI();
	}
	
	/* (non-Javadoc)
	 * @see madkit.kernel.AbstractAgent#initGUI()
	 */
	public void initGUI()
	{
		createGroup(false,community,group,"Kernel's Networking group",new NetworkIdentifier());
		gui=new NetAgentGUI(this,router);
		setGUIObject(gui);
	}

	/**
	 * @return
	 */
	public int getPort() 
	{
		return socketInfo.getPort();
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleNetworkMessage(madkit.netcomm.NetworkMessage)
	 */
	protected void handleNetworkMessage(NetworkMessage message) 
	{
		switch (message.getType()) {
			case NetworkMessage.KERNEL_CONNECTED :
				if(hasGUI())
				{
					debug("Kernel_connected received from "+message.getSender());
					gui.kernelConnected(message);
				}
				break;
			
			case NetworkMessage.KERNEL_DISCONNECTED:
				debug("KERNEL_DISCONNECTED received");
				if(hasGUI())
				{
					gui.kernelDisconnected(message);
				}
				sendMessage(myKernel,new NetworkRequest(NetworkRequest.DECONNECTED_FROM,((KernelAddress)message.getArgument()).getID()));
				break;
			case NetworkMessage.UPDATE_ROUTE:
				if(hasGUI()){					
					gui.updateKernel(message);
				}
			break;
			case NetworkMessage.FOWARD_TO_KERNEL:
				Object o=message.getArgument();
				if(o instanceof NetworkRequest){
					NetworkRequest m=(NetworkRequest) o;
					sendMessage(myKernel,new NetworkRequest(m.getRequestCode(),m.getArgument()));
				}
				break;

			default :
				debug("Unknown NetworkMessageType");
				debug(message.toString());
				break;
		}

		
	}

	/* (non-Javadoc)
	 * @see madkit.netcomm.MadkitNetworkAgent#handleConfigMessage(madkit.netcomm.NetConfigMessage)
	 */
	protected void handleConfigMessage(NetConfigMessage message) 
	{
	//	debug("ConfigurationMessage Received --- Changing config to:");
		
	}
	
	
	protected void handlePersonalMessage(StringMessage m)
	{
			debug("Communicator: personal message: "+m);
			StringTokenizer st = new StringTokenizer(m.getString()," :");
			String performative = "nop";
			String host=null;
			int port=DEFAULT_PORT;
			String portname=null;
			if (st.hasMoreTokens()){
				performative = st.nextToken();
			}

			if (performative.equals("add-host")){
				if (st.hasMoreTokens()){
					host = st.nextToken();
					if (st.hasMoreTokens()){
						portname = st.nextToken();
						try {
							port = Integer.parseInt(portname);
						}catch (NumberFormatException ex){
							System.err.println("Communicator error: not a valid port number: "+port);
						}
					}
					this.connectKernel(host,port);
				}
				else {
					System.err.println("Communicator error: not a valid host to add: "+host);
				}
			} else if (performative.equals("get-port")){
				sendMessage(m.getSender(),new StringMessage("reply port="+this.getPort()));
			}
		}
	
	////////////////////////////////////////////////////////////////////////////////////////////
	//					Control Methods
	////////////////////////////////////////////////////////////////////////////////////////////
	/**Disconnects the kernel handled by the agent p2p
	 * @param p2p
	 */
	void disconnectKernel(AgentAddress p2p)
	{
		sendMessage(p2p,new NetworkMessage(NetworkMessage.DISCONNECT,null));
	}
	
	/**Requests the connection of the kernel at host:port
	 * @param host Distant host
	 * @param port 
	 */
	public void connectKernel(String host, int port)
	{
		debug("Trying to connect to "+host+":"+port);
		try {
			InetAddress ia=InetAddress.getByName(host);
			Socket sock=new Socket(ia,port);
			Vector v=new Vector();
			v.add(sock);
			v.add(null);
			v.add(new SocketKernel(ia.getHostName(),port));
			
			sendMessage(router.getAddress(),new NetworkMessage(NetworkMessage.CONNECT_KERNEL,v));
		} catch (UnknownHostException e) {
			debug("UnknownHostException caught "+e.getMessage());
		} catch (IOException e) {
			debug("IOException caught "+e.getMessage());
		}
	}
	
	
		// Added By Saber 
	
	public void connectAgency(String kernelName,String host, int port)
	{
		println("Trying to connect Agency to host "+host+":"+port);
		
		try 
		{
			InetAddress ia=InetAddress.getByName(host);
			Vector v=new Vector();
			try
			{	
				Socket sock=new Socket(ia,port);
				v.add(sock);
				v.add(null);
				v.add(new SocketKernel(ia.getHostName(),port));
				sendMessage(router.getAddress(),new NetworkMessage(NetworkMessage.CONNECT_KERNEL,v));
			}
			catch(Exception e)
			{
					println("Connexion with distant agency couldn't be establish"+e.getMessage());
			}
		} 
		catch (UnknownHostException e) 
		{
			println("Errrror "+e.toString());
			debug("UnknownHostException caught "+e.getMessage());
		} 
		catch (IOException e) 
		{
			println("Errrror "+e.toString());
			debug("IOException caught "+e.getMessage());
		}
		
	}

	
	
	/**Broadcasts the presence of the kernel.
	 * @return true if the broadcast was successful.
	 */
	public boolean madkitBroadcast()
	{
		try 
		{
			println("Broadcasting New Madkit Kernel");
			BroadCaster.broadcast(myKernel.getKernel().getID(),socketInfo.getHost(),socketInfo.getPort());
			return true;
		} 
		catch (IOException e) 
		{
			println("Exception during broadcast");
			debug("IOException caught "+e.getMessage());
			return false;
		}
		
	}

	/**
	 * @return Returns the autoBroadcast.
	 * 
	 * @uml.property name="autoBroadcast"
	 */
	public boolean isAutoBroadcast() {
		return autoBroadcast;
	}

	/**
	 * If the autoBroadcast is set to true, it broadcasts the presence of the kernel.
	 * @param autoBroadcast The autoBroadcast to set.
	 * 
	 * @uml.property name="autoBroadcast"
	 */
	public void setAutoBroadcast(boolean autoBroadcast) {
		this.autoBroadcast = autoBroadcast;
	}

	/**
	 * 
	 * @uml.property name="stats"
	 */
	void setStats(StatsAgent ag) {
		this.stats = ag;
	}
	
	void sendDebug(boolean b){
		setDebug(b);
		NetConfigMessage.setDebug(b);
		this.broadcastMessage(community,group,"member",new NetConfigMessage());
	}

}
