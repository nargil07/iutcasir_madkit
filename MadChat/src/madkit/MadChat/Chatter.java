package madkit.MadChat;

import java.io.Serializable;

import madkit.kernel.AgentAddress;

public class Chatter implements Serializable{
	private String nom;
	private boolean op;
	private boolean voice;
	private AgentAddress adresse;
	
	public Chatter(String nom, boolean op, AgentAddress adresse) {
		super();
		// TODO Auto-generated constructor stub
		this.nom = nom;
		this.op = op;
		this.voice = false; // on initialise le voice a true pour permettre de parler
		this.adresse = adresse;
	}

	public AgentAddress getAdresse() {
		return adresse;
	}

	public void setAdresse(AgentAddress adresse) {
		this.adresse = adresse;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isOp() {
		return op;
	}
	
	public boolean isVoice() {
		return voice;
	}

	public void setOp(boolean op) {
		this.op = op;
	}
	
	public void setVoice(boolean voice){
		this.voice = voice;
	}

	public String toString() {
		if(op) return "@"+nom;
		else if(voice) return "+"+nom;
		return nom;
	}
	
	
}
