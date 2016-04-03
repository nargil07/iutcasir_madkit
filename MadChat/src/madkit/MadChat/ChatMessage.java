package madkit.MadChat;
import madkit.kernel.Message;


public class ChatMessage extends Message{
	 /**Chat's message content*/
    String content = null;
    /**Chatter name*/
    String chatter = null;
    /**Channel name*/
    String channel = null;

    /**Get the chat's message content*/
    String getContent()
    {
        return content;
    }

    /**Build the String of the chat's message*/
    public String toString()
    {
        if (chatter == null)
            return content;
        else
            return chatter + "> " + content;
    }

    /**Set the chat's message content to s*/
    ChatMessage(String s){
        content=s;
    }
    /**Set the chat's message content to s and the chatter's name to from*/
    ChatMessage(String from, String s){
        content=s;
        chatter = from;
    }
    /**Set the chat's message content to s, chatter's name to from and channel's name to channel*/
    ChatMessage(String from, String name, String s){
        content=s;
        chatter = from;
        channel = name;
    }
    /**Return the chatter's name*/
    String getChatter(){return chatter;}
     /**Return the chatter's name*/
    String getChannel(){return channel;}

}//fin ChatMessage

