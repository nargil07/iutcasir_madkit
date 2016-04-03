package madkit.TreeTools;

import java.io.*;


public class AbstractFileNode extends GenericTreeNode {

    protected File file;
    protected Entry entry;

    public static String getNameFromPath(String s){
        int k = s.lastIndexOf('/');
        if (k != -1){
            return s.substring(k+1);
        }
        else return s;
    }

    public AbstractFileNode(File _file){
	super(_file.getName());
	file = _file;
    }

    public AbstractFileNode(Entry _entry){
	super(_entry.getName());
	entry = _entry;
    }
    
	/**
	 * @return Returns the entry.
	 */
	public Entry getEntry() {
		return entry;
	}
	/**
	 * @param entry The entry to set.
	 */
	public void setEntry(Entry entry) {
		this.entry = entry;
	}
	/**
	 * @return Returns the file.
	 */
	public File getFile() {
		return file;
	}
	/**
	 * @param file The file to set.
	 */
	public void setFile(File file) {
		this.file = file;
	}
}
