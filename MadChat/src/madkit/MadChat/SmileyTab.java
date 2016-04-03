package madkit.MadChat;
import java.util.*;

import javax.swing.ImageIcon;

public class SmileyTab {
	Hashtable emoticon;
		
	public SmileyTab(){
		emoticon=new Hashtable();
	}
	
	public void insertSmiley(String emoticone,String path){
		System.out.println("Ajout smiley dns la table: ["+emoticone+"]="+path);
		emoticon.put(emoticone, path);
	}
	public String getPathBySmiley(String s){
		return (String)emoticon.get(s);
	}
	
	public String getSmileyByPath(String s){
		//System.out.println("======>getSmileyByPatj ==>"+s);
		Set keys = emoticon.keySet();
		Iterator ik = keys.iterator();
		
		while (ik.hasNext()){
			String cle=(String)ik.next();
			//System.out.println("=>"+cle+" "+emotIcon.get(cle));
			if(emoticon.get(cle).equals(s)){	
				return cle;
			}
		}
		//System.out.println("<=======getSmileyByPatj");
		return null;
	}
	
	public String[] getKeys(){
		Set keys = emoticon.keySet();
		Iterator ik = keys.iterator();
		String res[]=new String[keys.size()];
		int i=0;
		while (ik.hasNext()){
			res[i]=(String)ik.next();
			i++;
		}
		return res;
	}
	
	public void clear(){
		emoticon.clear();
	}
		
}
