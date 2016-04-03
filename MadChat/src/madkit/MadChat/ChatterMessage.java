package madkit.MadChat;
import java.io.Serializable;

import madkit.kernel.*;
public class ChatterMessage extends Message implements Serializable{

	Chatter chatter= null;
	String channel;
	
	public ChatterMessage(Chatter chatter,String channel) {
		super();
		this.chatter=chatter;
		this.channel=channel;
	}

	public Chatter getChatter() {
		return chatter;
	}

	public void setChatter(Chatter chatter) {
		this.chatter = chatter;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
