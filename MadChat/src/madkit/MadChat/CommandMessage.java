	package madkit.MadChat;
	import madkit.kernel.AgentAddress;
import madkit.kernel.Message;

public class CommandMessage extends Message{

		 /**Chat's message content*/
	    AgentAddress source = null;
	    /**Chatter name*/
	    int messageType = 0;
	    /**Channel name*/
	    String channel = null;
	    String data;
	    
	    long time;
	    
	    public final static int REQ_LIST = 0;	    
	    public final static int REQ_SHARE_SRV = 1;
	    public final static int REQ_SERV =  2;
	    
	    public final static int MSG_QUIT = 30;
	    public final static int MSG_NICK = 31;
	    
	    public final static int MSG_OP = 32;
	    public final static int MSG_DEOP = 33;
	    
	    public final static int MSG_SLAP = 34;
	    public final static int MSG_KICK = 35;
	    public final static int MSG_PV = 36;
	    public final static int MSG_PING = 37;
	    public final static int MSG_WHOIS = 38;
	    
		public CommandMessage(AgentAddress sender, int messageType, String channel, String data) {
			super();
	
			this.source = sender;
			this.messageType = messageType;
			this.channel = channel;
			this.data = data;
		}
		
		public CommandMessage(AgentAddress sender, int messageType, String channel, long data) {
			super();
	
			this.source = sender;
			this.messageType = messageType;
			this.channel = channel;
			this.time = data;
		}
		
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public String getData() {
			return data;
		}
		public long getTime(){
			return time;
		}
		public void setData(String data) {
			this.data = data;
		}
		public int getMessageType() {
			return messageType;
		}
		public void setMessageType(int messageType) {
			this.messageType = messageType;
		}
		public AgentAddress getSource() {
			return source;
		}
		public void setSource(AgentAddress sender) {
			this.source = sender;
		}

	    
	    
}//fin ChatMessage


