package madkit.MadChat;
import java.io.*;
import javax.swing.*;
import javax.swing.text.*;

public class HTMLEditor extends JEditorPane{

	private MyHTMLEditorKit kit;
	
	public HTMLEditor() {
		super();
		setDocument( new DefaultStyledDocument( new StyleContext() ) );
		kit = new MyHTMLEditorKit();
		setEditorKit(kit);
		
	}
	
	public void insertHTML(String html) {
		//assumes editor is already set to "text/html" type
		
		Document doc = getDocument();
		StringReader reader = new StringReader(html);
		try {
			System.out.println(doc.getLength());
			kit.read(reader, doc, doc.getLength());

		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		this.setCaretPosition(doc.getLength());
	}
}
