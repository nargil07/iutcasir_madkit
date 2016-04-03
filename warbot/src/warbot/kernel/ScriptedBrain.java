/*
* ScriptedBrain - Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.kernel;

import java.lang.reflect.*;
import java.io.PrintWriter;
import madkit.kernel.*;
import madkit.messages.ControlMessage;
import madkit.system.EditorAgent;
import madkit.utils.agents.LanguageController;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
abstract public class ScriptedBrain extends Brain {

	PrintWriter out;
	 PrintWriter err;

	 PrintWriter getOut(){return out;}
	 PrintWriter getErr(){return err;}
	/**
	 * 
	 */
	 
	public void initGUI() {
		OPanel o = new OPanel();
		setGUIObject(o);

		out = new PrintWriter(o.getOut());
		err = new PrintWriter(o.getOut());
	  }
	
	public ScriptedBrain() {
		super();
	}
	
	public void println(String s){
	  if (out != null)
			out.println(s);
		  else
			super.println(s);
	}
	
//	/ IMPORTANT ACTIONS
	public void doIt(){
		   this.getController().doIt();
	}
	
	public void edit(){
				Controller cont = this.getController();
				if (!(cont instanceof LanguageController)){
					System.out.println("Scripted brain not editable...");
					return;
				}
				String s = ((LanguageController)cont).getBehaviorFile();
				// String s=this.getFile().getAbsolutePath();
				System.out.println("Editing : " + s);
				Agent ed=null;
				AgentAddress edagent=getAgentWithRole("system","editor");
				System.out.println("Editor: "+edagent);
				if (edagent != null){
					System.out.println("Sending message to "+edagent);
					sendMessage(edagent,new ControlMessage("edit",s));
				} else try {
						Class cl = Class.forName("jsynedit.JSynEditAgent");
						System.out.println("classe: "+cl);
						Class[] argTypes = new Class[]{String.class};
						Constructor cons = cl.getConstructor(argTypes);
						System.out.println("cons: "+cons);
						ed = (Agent) cons.newInstance(argTypes);
						System.out.println("ed: "+ed);
						// ed = new jsynedit.JSynEditAgent(s);
						launchAgent(ed,"Edit : " + s,true);
					} catch(Exception e){
						// System.err.println("Warning: JSynEdit has not been implemented");
						System.out.println("JSynEdit : " + s);
						ed = new EditorAgent(s);
						launchAgent(ed,"Edit : " + s,true);
					}
	
	}

}
