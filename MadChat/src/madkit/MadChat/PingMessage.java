package madkit.MadChat;

import madkit.kernel.*;

/**
 * @author gaara
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PingMessage extends Message{

	private String chanel;
	private long temps;
	private String user;
	
	public PingMessage(long time, String channel, String chatter){
		super();
		this.temps=time;
		this.chanel=channel;
		this.user= chatter;
	}
	
	public long getTime(){
		return temps;
	}
	
	public String getChannel(){
		return chanel;
	}
	
	public String getChatter(){
		return user;
	}
}
