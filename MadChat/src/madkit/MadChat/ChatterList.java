package madkit.MadChat;

import java.util.*;

import madkit.kernel.AgentAddress;

public class ChatterList extends Vector {

	public ChatterList(int arg0, int arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ChatterList(int arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ChatterList() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void removeByLogin(String login){
		for(int i=0;i<size();i++){
			Chatter c=(Chatter)get(i);
			if(c.getNom().equals(login)) remove(c);
		}
			
	}
	
	public Chatter getByLogin(String login){
		
			for(int i=0;i<size();i++){
				Chatter c=(Chatter)get(i);
				if(c.getNom().equals(login)) return c;
			}	
			return null;
	}
	
	public Chatter getByAgentAddress(AgentAddress aa){
		
		for(int i=0;i<size();i++){
			Chatter c=(Chatter)get(i);
			if(c.getAdresse().equals(aa)) return c;
		}	
		return null;
}
	public void RemoveByAgentAddress(AgentAddress aa){
		
		for(int i=0;i<size();i++){
			Chatter c=(Chatter)get(i);
			if(c.getAdresse().equals(aa)) remove(c);
		}	
		
	}
	public boolean isInList(String login){
		for(int i=0;i<size();i++){
			Chatter c=(Chatter)get(i);
			if(c.getNom().equals(login)) return true;
		}	
		return false;
	}
}
