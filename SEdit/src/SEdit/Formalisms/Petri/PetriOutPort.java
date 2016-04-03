/*
* PetriOutPort.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit.Formalisms.Petri;

import java.util.*;
import java.io.*;

import SEdit.*;
import SEdit.Graphics.*;
import SEdit.Scheme.*;

import madkit.kernel.*;
import madkit.messages.*;



public class PetriOutPort extends PetriPlace {

	protected String messageType;
	public String getMessageType() {return(messageType);}
	public void setMessageType(String s) {messageType = s;}

	/* public void produce(Object e) {
  		String s = STools.prinToString(e);
  		// getAgent().sendOutValue(getLabel(),s);
  		if (messageType.equals("StringMessage")){
  			StringMessage m = new StringMessage(s);
  			System.out.println(">> send StringMessage : " + s);
  			getStructure().getAgent().sendToRecipient(m);
  		} else if (messageType.equals("ActMessage")){
  			ActMessage m = new ActMessage(getLabel());
  			m.setContent(e);
  			System.out.println(">> send ActMessage : " + s);
  			getStructure().getAgent().sendToRecipient(m);
  		} else if (messageType.equals("ACLMessage")){
  			ACLMessage m = new ACLMessage(getLabel(),s);
  			System.out.println(">> send ACLMessage : " + s);
  			getStructure().getAgent().sendToRecipient(m);
  		}

  	} */

  	public void produce(int j){
  		String value = "($token " + j + ")";
  		// getAgent().sendOutValue(getLabel(),value);
  		System.out.println(">> send token : " + value);
  	}

}
