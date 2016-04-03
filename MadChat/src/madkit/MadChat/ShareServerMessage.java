package madkit.MadChat;
import madkit.kernel.AgentAddress;
import madkit.kernel.Message;

public class ShareServerMessage extends Message{

		 /**Chat's message content*/
	    AgentAddress source = null;
	    /**Chatter name*/
	    
	    /**Channel name*/
	    String channel = null;
	    AgentAddress shareServer=null;
	    
	    
	    
		public ShareServerMessage(AgentAddress source, String channel, AgentAddress shareServer) {
			super();
			this.source = source;
			this.channel = channel;
			this.shareServer = shareServer;
		}
		public String getChannel() {
			return channel;
		}
		public void setChannel(String channel) {
			this.channel = channel;
		}
		public AgentAddress getShareServer() {
			return shareServer;
		}
		public void setShareServer(AgentAddress shareServer) {
			this.shareServer = shareServer;
		}
		public AgentAddress getSource() {
			return source;
		}
		public void setSource(AgentAddress source) {
			this.source = source;
		}
	    

	    
	    
	    
	    
}//fin ChatMessage


