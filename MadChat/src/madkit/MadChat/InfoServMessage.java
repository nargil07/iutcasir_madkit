
package madkit.MadChat;

import madkit.kernel.*;


public class InfoServMessage extends Message{
 
	ConfigChan config;
	String chan;
	
	
	/**
	 * @param topic
	 * @param channel
	 * @param cb_mod
	 * @param password
	 */
	
	public InfoServMessage(String channel,ConfigChan c) {
		super();
		this.config=c;
		this.chan=channel;
	}
	
	
	/**
	 * @return Returns the chan.
	 */
	public String getChan() {
		return chan;
	}
	/**
	 * @param chan The chan to set.
	 */
	public void setChan(String chan) {
		this.chan = chan;
	}
	/**
	 * @return Returns the config.
	 */
	public ConfigChan getConfigChan() {
		return config;
	}
	/**
	 * @param config The config to set.
	 */
	public void setConfigChan(ConfigChan config) {
		this.config = config;
	}
}
