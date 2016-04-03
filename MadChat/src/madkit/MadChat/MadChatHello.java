package madkit.MadChat;

import java.util.Date;
import java.util.Vector;

import javax.swing.JPanel;

import madkit.MadChat.share.RequestTreeMessage;
import madkit.MadChat.share.SenderAgent;
import madkit.MadChat.share.ShareAgent;
import madkit.MadChat.share.ShareAgentPanel;
import madkit.MadChat.share.SmallSenderAgent;
import madkit.MadChat.share.TreeMessage;
import madkit.TreeTools.DirEntry;
import madkit.kernel.*;

public class MadChatHello extends Agent {
	Config configuration;
	String myCommunity="MadChat";
	String myGroup="default";
	String myRole="chatter";
	MadChat display;
	
	
	boolean alive = true;
	
	  public void initGUI()
	  {
		configuration=new Config(this);
		SenderAgent senderA=new SenderAgent();
		ShareAgent shareA=new ShareAgent();
		configuration.setSenderAgent(senderA);
		configuration.setShareAgent(shareA);
		if(configuration.isShare()){
			
			launchAgent(senderA,"share",false);
		}
		launchAgent(shareA,"share",false);
		display = new MadChat(configuration);
		setGUIObject(display);
		//set
	  }
	
	public void activate(){
		
		
		// create a distributed group
		int g = createGroup(true, myCommunity, myGroup, null, null);
		if (g == 1)
			println("I create the group "+myGroup+" in community "+myCommunity);
		else if (g == -1)
			println("The group has already been created");
		else
			println("Cannot create the group");
		
		display.addChan("default");
			
		/*int r = requestRole(myCommunity, myGroup, myRole,null);
		if (r == 1)
			println("I play "+ myRole + " in " + myGroup + " of " + myCommunity);
		else if (r == -1)
			println("I cannot enter the group: access denied");
		else if (r == -2)
			println("I already play the role "+myRole+" in " + myGroup + " of " + myCommunity);
		else if (r == -3)
			println("The group "+myGroup+" does not exist int the community "+myCommunity);
		else if (r == -4)
			println("The community "+myCommunity+" does not exist");*/
	}
	
	public void live()
	{
		println("Hello world...");
		//broadcastMessage(myCommunity, myGroup, myRole, new StringMessage("Hello to all!"));
		while(alive){
			Message m = waitNextMessage();
			handleMessage(m);
		}
	}
	
	
	
	public MadChat getDisplay() {
		return display;
	}

	void handleMessage(Message m){
		// You should describe here the agent's behavior
		// upon reception of a message
		println("On est ds le HandleMsg");
		if (m instanceof ChatMessage){
			println("instance of chatmsg");
			ChatMessage cm=(ChatMessage)m;
			display.insertChatMsgInChan(cm.getChannel(),cm.getChatter(),cm.getContent());
		}
		else if (m instanceof PingMessage){
			println("instance of pingmsg");
			PingMessage cm=(PingMessage)m;
			Date d = new Date();
			long time = d.getTime() - cm.getTime();
			System.out.println("Time in ms : "+ time);
			//display.insertChatMsgInChan(cm.getChannel(),cm.getChatter(),cm.getContent());
			display.insertInfoMsgInChan(cm.getChannel(), "[- "+cm.getChatter()+" PING reply : "+time+" ms -]","red");
		}
		else if (m instanceof CommandMessage){
			println("instance of commandMessage");
			CommandMessage cm=(CommandMessage)m;
			switch (cm.getMessageType()) {
			case CommandMessage.REQ_LIST:
				println("renvoie la liste");
				ListMessage m1=new ListMessage(display.getChatterListInChan(cm.getChannel()),cm.getChannel());
				this.sendMessage(cm.getSource(),m1);
				break;
			case CommandMessage.MSG_QUIT:
				println(cm.getSource()+" va quitter le salon "+cm.getData());
				display.removeChatterInChan(cm.getChannel(),cm.getSource());
				break;
			case CommandMessage.MSG_NICK:
				println("change de nick "+cm.getData());
				display.setChatterNickInChan(cm.getChannel(),cm.getSource(),cm.getData());
				break;
			case CommandMessage.REQ_SHARE_SRV:
				println("demande mon serveur de fichier");
				ShareServerMessage ssm= new ShareServerMessage(this.getAddress(),cm.getChannel(),null);
				if(configuration.isShareRunning()) ssm.setShareServer(configuration.getSenderAgent().getAddress());
				this.sendMessage(cm.getSource(),ssm);
				break;
			case CommandMessage.REQ_SERV:
				println("demande info chan");
				InfoServMessage ism= new InfoServMessage(cm.getChannel(),display.getConfigChan(cm.getChannel()));
				sendMessage(cm.getSender(),ism);
				break;
			case CommandMessage.MSG_OP:
				println("message : OP "+ cm.getData());
				display.setOP(cm.getChannel(), cm.getSource(), cm.getData(), true);
				break;
			case CommandMessage.MSG_DEOP:
				println("message : DEOP");
				display.setOP(cm.getChannel(), cm.getSource(), cm.getData(), false);
				break;
			case CommandMessage.MSG_SLAP:
				println("message : SLAP");
				display.slapInChan(cm.getChannel(), cm.getSource(), cm.getData());
				break;
			case CommandMessage.MSG_KICK:
				println("message : Kick");
				display.kickInChan(cm.getChannel(), cm.getSource(), cm.getData());
				break;
			case CommandMessage.MSG_PV:
				println("message : PV");
				System.out.println(cm.getChannel());
				if(display.existChan(cm.getChannel())){
					JPanel j = display.getChan(cm.getChannel());
					if(j instanceof PvPanel){
						((PvPanel)j).setHim(display.getChatListInChan("default").getByLogin(cm.getData()));
					}
				}
				else display.addChanPV(configuration, display.getChatListInChan("default").getByLogin(cm.getData()));
				break;
			case CommandMessage.MSG_PING:
				println("message : PING");
				PingMessage pingMess = new PingMessage(cm.getTime(), cm.getChannel(), configuration.getLogin());
				this.sendMessage(cm.getSender(), pingMess);
				break;
			case CommandMessage.MSG_WHOIS:
				println("message : WHOIS");
				break;
			default:
				break;
			}
		}
		else if(m instanceof ShareServerMessage){
			println("======================>shareServerMessage detected");
			ShareServerMessage ssm=(ShareServerMessage) m;
			if(ssm.getShareServer()!=null){
				sendMessage(ssm.getShareServer(), new RequestTreeMessage("request-tree"));
			}
			else{
				display.insertInfoMsgInChan(ssm.getChannel(),"Aucun fichier partag?","#ff0000");
			}
				
		}
		if (m instanceof TreeMessage) {
			AgentAddress serverAddress = ((TreeMessage) m).getSender();
			System.out.println("(client) TreeMessage received");
			String serverName =
				((TreeMessage) m).getSender().getKernel().getHost().toString();
			DirEntry s = ((TreeMessage) m).getDir();
			Vector v = s.getVect();
			println("adresse==============================>"+serverAddress);
			display.addServerPanel(	s,((TreeMessage) m).getSender());
			//checkPlugins(((TreeMessage)m).getPluginsInfo(), serverName,serverAddress);
			//checkUpdatedFile(s, ((TreeMessage) m).getSender());
		}
		else if (m instanceof InfoServMessage){
			println("instance of InfoServMessage");
			InfoServMessage ism=(InfoServMessage)m;
			display.setConfigInChan(ism.getChan(),ism.getConfigChan());
			display.insertInfoMsgInChan(ism.getChan(),"Le topic du channel viens de changer!","#ffffff");
			
		}
		
		else if (m instanceof ListMessage){
			println("instance of lsitMessage");
			ListMessage lm=(ListMessage)m;
			display.setChatterListInChan(lm.getChannel(),lm.getChatterList());
		}
		else if (m instanceof ChatterMessage){
			println("instance of ChatterMessage");
			ChatterMessage cm=(ChatterMessage)m;
			display.addChatterInChan(cm.getChannel(),cm.getChatter());
		}
	}
	
	//envoie un message a une personne autre que l'agent lui-meme
	//si il n'y a personne d'autre que que lui il se l'envoie
	public void sendMessageToAnyone(String community,String group,String role,Message m){
		AgentAddress[] ags=this.getAgentsWithRole(community,group,role);
		if(ags.length>1)
		{	
			int i=0;
			while(ags[i]==this.getAddress() && i<ags.length)
				i++;
			this.sendMessage(ags[i],m);
			System.out.println("envoie ? "+ags[i]+" "+ags[i]);
		}
		else {
			this.sendMessage(community,group,role,m);
			System.out.println("envoie au hasard ");
		}
	}
	public void end()
	{
		configuration.save();
		display.closeAllChannel();
		configuration.getSenderAgent().saveDirectory();
		killAgent(configuration.getSenderAgent());
		killAgent(configuration.getShareAgent());
	}
}
