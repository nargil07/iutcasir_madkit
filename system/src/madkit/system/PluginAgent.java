/*
* EditorAgent.java -a NotePad agent, to edit text and send string messages to other agents
* Copyright (C) 1998-2002 Jacques Ferber
*
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
package madkit.system;

import java.io.*;

import java.util.*;

import madkit.kernel.*;
import madkit.utils.common.*;
import madkit.kernel.Utils;
import madkit.messages.*;

/**
 * This class represent the different plugins that are in the kernel..
 * 
 * @author J.Ferber
 *
 */
public class PluginAgent extends Agent {
	
	PluginInformation pluginfo;
	boolean alive = true;
	
	public void init(File plugindir){
		if (pluginfo == null){
			pluginfo = new PluginInformation(plugindir);
		}
		if (!pluginfo.updateInfo()){
			// killAgent(this);
			System.err.println("Error when building the pluginAgent of "+plugindir);
			alive = false;
		}
	}
	
	public String getPluginName(){ return pluginfo.getName();}
	
	public void activate(){
		if (alive){
			createGroup(false,"system",null,null);
			requestRole("system","plugin",null);
		}
		
	}
	
	public void live(){
		if (!alive) return;
		AgentAddress designer = getAgentWithRole("system","designer");
		if (designer != null)
			sendMessage(designer,new ActMessage("newPlugin",pluginfo));
		while (alive){
			Message m = waitNextMessage();
			if (m instanceof StringMessage)
				handleStringMessage((StringMessage) m);
			else if (m instanceof ActMessage){
				handleActMessage((ActMessage) m);
			}
		}
	}
	
	void handleStringMessage(StringMessage msg){
		String content = msg.getString();
		StringTokenizer st = new StringTokenizer(content);
		String action = st.nextToken();
		if ("info".equalsIgnoreCase(action))
			sendMessage(msg.getSender(),new StringMessage(pluginfo.toString()));
//		if ("build".equalsIgnoreCase(action))
//			buildProject();
		else if ("getdoc".equalsIgnoreCase(action)){
			if (pluginfo.getDocPath() != null){
				String res = "displayLink£Plugins£"+pluginfo.getName()+"£"+pluginfo.getDocPath()+"£"+pluginfo.getName();
				sendMessage(msg.getSender(),new StringMessage(res));
			}
		}
	}
	
	void handleActMessage(ActMessage msg){
		String action = msg.getAction();
		if ("info".equalsIgnoreCase(action))
			sendMessage(msg.getSender(),new ActMessage("infos",pluginfo));
//		else if ("build".equalsIgnoreCase(action))
//			buildProject();
	}
	
//	void buildProject(){
//	}

}
