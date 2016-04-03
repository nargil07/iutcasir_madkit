package madkit.MadChat;

import madkit.kernel.*;


public class ListMessage extends Message{

	Chatter[] chatterList= null;
	String channel;
	
	public ListMessage(Chatter[] liste,String channel) {
		super();
		chatterList=liste;
		this.channel=channel;
	}

	public Chatter[] getChatterList() {
		return chatterList;
	}

	public void setChatterList(Chatter[] chatterList) {
		this.chatterList = chatterList;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
	
		
}
