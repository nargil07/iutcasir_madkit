/*
 * DySocketConfigurator.java - Created on Dec 15, 2003
 * 
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashSet;
import java.util.Vector;

import madkit.kernel.AgentAddress;
import madkit.kernel.InvalidAddressException;
import madkit.kernel.Kernel;
import madkit.kernel.KernelAddress;
import madkit.kernel.KernelMessage;
import madkit.netcomm.handlers.SocketHandler;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr *  * @version $Revision: 1.5 $
 */

final class DySocketConfigurator implements Runnable {

		/**
		 * 
		 * @uml.property name="agent"
		 * @uml.associationEnd multiplicity="(0 1)"
		 */
		private SocketDynamicConnection agent;

		private Socket _socket;
		private HashSet _dkernels;
		private Thread myThread=null;
		private int _localPort;
		private KernelAddress _localKernel;
		private AgentAddress _kernel;

		/**
		 * 
		 * @uml.property name="_socketKernel"
		 * @uml.associationEnd multiplicity="(0 1)"
		 */
		private SocketKernel _socketKernel;

		/**Output stream*/
		protected DataOutputStream out = null;
		/**Input stream*/
		protected DataInputStream in = null;
	
		public DySocketConfigurator(SocketDynamicConnection ag,int port, AgentAddress localkernel, SocketKernel socketKernel,Socket socket,HashSet dkernels){
			agent=ag;
			_socket = socket;
			_dkernels=dkernels;
			_localPort=port;
			_localKernel=localkernel.getKernel();
			_socketKernel=socketKernel;
			_kernel=localkernel;
		}
	
		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			Thread currentThread = Thread.currentThread();
			while(myThread == currentThread){
				try {
					Thread.sleep(200);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					System.err.println("InterruptedException "+e1.getMessage());
				}
				try {
					debug("Entering run");
					addConnectionHandler(_socket,_dkernels);
					debug("###Finish adding handler - closing socket");
					closeSocket(_socket);
					stop();
				} catch (IOException e) {
					debug("IOException caught : "+ e.toString());
					closeSocket(_socket);
				} catch (InvalidAddressException e) {
					debug("InvalidAddressException caught : "+ e.toString());
					closeSocket(_socket);
				}
			}
		}
	/*----------------------------------------------------------------------------------------*/
		/**
		 * @param sock
		 */
		private void closeSocket(Socket sock) {
			try {
				sock.close();
				stop();
			} catch (IOException e) {
				debug("IOException caught : "+ e.getMessage());
			}
		
		}
		/**
		 * @param string
		 */
		private void debug(String string) {
			agent.debug("[DyConfigurator]  "+ string);
		}

	/**Opens the Input/Ouput Streams. 
	 * @param socket communicatio socket.
	 */
	protected void openInputOutputStreams(Socket socket) throws IOException {
	
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	}
	/**sends the local port and retrieves the distant SocketDynamicConnectionAgent's port
	 * @return the distant SocketDynamicConnectionAgent's port
	 */
	private int exchangeServerSocketPort() throws IOException {
		debug("entering exchangeServerSocketPort");
		debug("Writing :"+_localPort);
		out.writeInt(_localPort);
		out.flush();
		debug("Reading distant port ");
		int r = in.readInt();
		debug("Got from distant port :"+ r);
		return r;
	}

	/**Exchanges the kernels information. 
		 * @param socket
		 * @return The distant kernelAddress or null if the is the local kernel
		 * @throws IOException
		 * @throws InvalidAddressException
		 */
		protected KernelAddress exchangeKernelInformation(Socket socket) throws IOException, InvalidAddressException {
		
			String kernel=_localKernel.getID();
			out.writeInt(kernel.length());
			out.writeChars(kernel);
			out.flush();
			out.writeInt(UDPServerAgent.getPort());
			out.flush();
		
			int size2=in.readInt();	
	
			KernelAddress ka;
			kernel="";
			for (int i=0;i<size2;i++){
				kernel=kernel+in.readChar();
			}
	
			int datagramSocketPort=in.readInt();
	
			ka=new KernelAddress(socket.getInetAddress().getHostName()+":"+kernel.toString());
			debug("requesting kernel: "+ka);
		
			if(kernel.equals(_localKernel.getID())){
				debug("trying to add my own kernel");
				return null;
			}
			return ka;
		}

	/**Adds a new connection handler.
		 * 
		 * @param socket
		 * @param dkernels contains the kernels known by the localkernel. if null the agent will go into passive mode. 
		 * @throws Exception
		 */
		protected void  addConnectionHandler(Socket socket, HashSet dkernels) throws IOException, InvalidAddressException {
			debug("Entering addConnectionHandler");
			openInputOutputStreams(socket);			
			ack();		
			debug("ACK: OK1");
			int remotePort=exchangeServerSocketPort();	
			KernelAddress ka=exchangeKernelInformation(socket);
			if(ka==null){
				closeSocket(_socket);
				return;
			}		
			SocketHandler sh=new SocketHandler(_localKernel,ka,socket.getInetAddress(),remotePort,dkernels!=null);	
			if(agent==null){
				debug("SocketAgent null!!!!!");
			}		
			if(!agent.addConnectionHandler(ka.getID(),sh)){
				closeSocket(_socket);
				stop();
				return;
			}		
			ack();
			debug("ACK: OK2");
			debug("ACK: Information Received");
			if(dkernels!=null){
				sh.sendObject(buildSynch('c',dkernels));
			}
			agent.sendMessage(agent.getAgentWithRole("system","kernel"),new KernelMessage(KernelMessage.INVOKE,Kernel.DUMP_COMMUNITIES));
		}

   private void ack() throws IOException {
		int ack=-88;  // -88 = '?'
		debug("sending: "+((char)ack));
		out.writeInt(ack);
		out.flush();
		int r=0;
		debug("waiting for "+((char) ack));
		while((r = in.readInt())!=ack){
			debug("connection read: "+r);
		}
		debug("Got the ack "+((char) r));
	}

	/* En el protocol el orden en el vector es
	 * 1. El kernelAddress del kernel local
	 * 2. el SocketKernel local
	 * 3. el AgentAddress del Kernel
	 * 4. un hashset con los kernels distantes.
	 * */
	private Vector buildSynch(char s,HashSet dKernels)
	{
		Vector v = new Vector();

		if (s == 'c') v.addElement("TRANSMIT_SOCKET_INFO");
		else if (s == 'r') v.addElement("ACK_SOCKET_INFO");

		v.addElement(_localKernel);
		
		
		v.addElement(_socketKernel);//need to change the socketKernel object to keep backwards comp
		v.addElement(_kernel);
		v.addElement(dKernels);
		return v;
	}
		
		////Runnable
		
		/**
		 * Starts the Processor
		 */
		public void start(){
			if (myThread == null) {
				myThread = new Thread(this,"DySocketConfigurator");            
				myThread.start();
            
			}
		}  

		/**
		 * Stops the Processor
		 */
		public void stop(){
			myThread=null;
		}

		/**Gets the Processor state.
		 * @return true if the processor is not running.
		 */
		public boolean isRunning() {
			return myThread!=null;
		}

}
