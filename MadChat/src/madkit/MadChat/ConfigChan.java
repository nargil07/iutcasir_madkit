/*
 * Created on Mar 30, 2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package madkit.MadChat;

import java.io.Serializable;

public class ConfigChan implements Serializable{

	String nomChannel; 
	String topic ;
	boolean istopic;
	String password; 
	boolean moderated;
	
	
	public ConfigChan(String nomChannel, String topic, String password,boolean moderated, boolean istopic) {
		this.nomChannel = nomChannel;
		this.topic = topic;
		this.istopic = istopic;
		this.password = password;
		this.moderated = moderated;
	}
	
	
	public boolean isTopic(){
		return istopic;
	}
	public void setIsTopic(boolean bool){
		this.istopic=bool;
	}
	/**
	 * retourne true si le channel est mod?r? (on ne peut disctuer que si @ ou +)
	 */
	public boolean isModerated() {
		return moderated;
	}
	/**
	 * @param moderated The moderated to set.
	 */
	public void setModerated(boolean moderated) {
		this.moderated = moderated;
	}
	/**
	 * @return Returns the nomChannel.
	 */
	public String getNomChannel() {
		return nomChannel;
	}
	/**
	 * @param nomChannel The nomChannel to set.
	 */
	public void setNomChannel(String nomChannel) {
		this.nomChannel = nomChannel;
	}
	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}
	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}
	/**
	 * @return Returns the topic.
	 */
	public String getTopic() {
		return topic;
	}
	/**
	 * @param topic The topic to set.
	 */
	public void setTopic(String topic) {
		this.topic = topic;
	}
}
