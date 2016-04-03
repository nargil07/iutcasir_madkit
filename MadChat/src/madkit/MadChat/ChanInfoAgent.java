/*
 * Created on Mar 30, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package madkit.MadChat;

import java.util.Vector;

import javax.swing.ImageIcon;

import madkit.kernel.*;

/**
 * @author gaara
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class ChanInfoAgent extends Agent {
	private MyTableModel model;

	private Config config;

	public ChanInfoAgent(MyTableModel tm, Config cfg) {
		model = tm;
		config = cfg;
	}

	public void activate() {
		String[] chanConnectes = config.getChatAgent().getMyGroups("MadChat");
		String[] chanlist = getExistingGroups("MadChat");
		Vector vectChanTmp=new Vector();
		Vector vectChan=new Vector();
		
		
		for (int i = 0; i < chanlist.length; i++) {
			if (chanlist[i].startsWith("%") || chanlist[i].startsWith("$")) {
				System.out.println(chanlist[i] + " est prive");
			}
			else vectChanTmp.add(chanlist[i]);
		}
		
		for(int i=0;i<vectChanTmp.size();i++){
			boolean bool = false;
			String s = (String)vectChanTmp.get(i);
			for (int j = 0; j < chanConnectes.length && !bool; j++) {
				if (chanConnectes[j].equals(chanlist[i]))
					bool = true;
			}
			//	if(bool) tabchan[i][0]= new
			// ImageIcon(config.getIconsPath()+"aqua_bulle.gif");
			if (!bool) vectChan.add(s);
			
		}

		
		Object[][] tabchan = new Object[vectChan.size()][5];

		
		for (int i = 0; i<vectChan.size(); i++) {
			CommandMessage cm = new CommandMessage(this.getAddress(),
			CommandMessage.REQ_SERV, (String)vectChan.get(i), "");

			sendMessage("MadChat", (String)vectChan.get(i), "chatter", cm);
			Message m = waitNextMessage();
					
			tabchan[i][0] = new Boolean(false);
			tabchan[i][1] =(String)vectChan.get(i);

			if (m instanceof InfoServMessage) {
					InfoServMessage ism = (InfoServMessage) m;
					ConfigChan cfgChan = ism.getConfigChan();
					tabchan[i][2] = cfgChan.getTopic();
					tabchan[i][3] = new Boolean(!(cfgChan.getPassword()
							.equals("")));
					tabchan[i][4] = new Integer(getAgentsWithRole(
								"MadChat", (String)vectChan.get(i), "chatter").length);
			} else {
					tabchan[i][2] = "Topic inconnu";
					tabchan[i][3] = "?";
					tabchan[i][4] = new Integer(getAgentsWithRole(
							"MadChat", (String)vectChan.get(i), "chatter").length);
			}
			

		}
		model.setData(tabchan);
	}

	public void live() {
		println("Hello world...");
		//broadcastMessage(myCommunity, myGroup, myRole, new
		// StringMessage("Hello to all!"));

	}
}