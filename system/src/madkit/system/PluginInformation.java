/*
 * Created on 7 févr. 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.system;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import java.util.StringTokenizer;
import java.util.Iterator;

import madkit.kernel.Utils;
import madkit.utils.common.PropertyFile;
import madkit.utils.common.Version;

/**
 * @author Sebastian Rodriguez (adaptation J. Ferber)
 * 
 * PluginInformation contains all the information of a plugin.. The plugin agent
 * reads the content of the <plugin>.properties file, not the build.xml file!!
 */

public class PluginInformation implements Serializable {
	public final static int UP_TO_DATE = 0;

	public final static int NEEDS_UPDATE = 1;

	public final static int NEW = 2;

	private String name;

	private Version version;

	private int size;

	private File directory;

	//		private String fileName;
	private String description;

	private String agentNames;

	// private Hashtable dependencies = new Hashtable();

	private Collection actions = new Vector();

	//		private String serverUrl;
	private String docPath; // DocPath for the manual section
	private String system;

	//author data
	private String authorName;

	private File desktopFile;
	private File buildFile;
	private String docRef;
	private String category;
	
	// depends property
	private String depends;
	private Vector dependList;
	
	public String getDepends(){
		return depends;
	}
	
	public void parseDepends(){
		dependList=new Vector();
		if (depends == null) return;
		StringTokenizer st= new StringTokenizer(depends,", ");
		while (st.hasMoreTokens()) {
			dependList.add(st.nextToken());
	     }
	}
	
	public Vector getDependList(){
		return dependList;
	}
	
	synchronized void addDepend(String[] elts){
		for(int i=0;i<elts.length;i++){
			dependList.add(elts[i]);
		}
		compileDepends();
	}
	
	synchronized public void addDepend(String elt){
		dependList.add(elt);
		compileDepends();
	}
	
	void compileDepends(){
		String st="";
		for (Iterator it = dependList.iterator();it.hasNext();){
			st = st+it.next();
			if (it.hasNext())
				st = st+", ";
		}
		depends = st;
	}
	
	synchronized public void removeDepend(String elt){
		dependList.remove(elt);
		compileDepends();
	}
	
	// requires property
	private String requires;
	private Vector requireList;
	
	public String getRequires(){
		return requires;
	}
	
	public void parseRequires(){
		//System.out.println("parsing requires:"+requires);
		requireList=new Vector();
		if (requires == null) return;
		StringTokenizer st= new StringTokenizer(requires,", ");
		while (st.hasMoreTokens()) {
			requireList.add(st.nextToken());
	     }
	}
	public Vector getRequireList(){
		return requireList;
	}
	
	synchronized public void addRequire(String[] elts){
		for(int i=0;i<elts.length;i++){
			requireList.add(elts[i]);
		}
		compileRequire();
	}
	synchronized public void addRequire(String elt){
		requireList.add(elt);
		requires = requires+", "+elt;
	}
	
	void compileRequire(){
		String st="";
		for (Iterator it = requireList.iterator();it.hasNext();){
			st = st+it.next();
			if (it.hasNext())
				st = st+", ";
		}
		requires = st;
	}
	
	synchronized public void removeRequire(String elt){
		requireList.remove(elt);
		compileRequire();
	}

	//		private String _authorEmail;
	//		private String _authorWeb;
	//		private String _md5;

	/**
	 *  
	 */

	protected PluginInformation(File dir) {
		directory = dir;
	}

	public String getDocPath() {
		return docPath;
	}

	/**
	 * @return Returns the_author Name.
	 */
	public final String getAuthorName() {
		return authorName;
	}
	/**
	 * Sets the author of the plugin
	 */
	public final void setAuthorName(String s) {
		authorName = s;
	}


	/**
	 * @return Returns the system.
	 */
	public String getSystem() {
		return system;
	}
	/**
	 * @param system The system to set.
	 */
	public void setSystem(String s) {
		system = s;
	}
	/**
	 * @return Returns the description of the plugin
	 */
	public final String getDescription() {
		return description;
	}
	
	/**
	 * Sets the description of the plugin
	 */
	public final void setDescription(String s) {
		 description = s;
	}

	/**
	 * @return Returns the directory where the plugin is located.
	 */
	public final File getDirectory() {
		return directory;
	}
	
	/**
	 * @return Returns the file of the local desktop.ini file of the plugin.
	 */
	 public final File getDesktopIniFile() {
		return desktopFile;
	}
	 /**
		 * @return Returns the file of the local desktop.ini file of the plugin.
		 */
	 public final File getBuildFile() {
			return desktopFile;
		}

	/**
	 * @return Returns the _fileName.
	 */
	//		public final String getFileName() {
	//			return fileName;
	//		}
	//		public final String getFileMD5Sum(){
	//			return _md5;
	//		}
	/**
	 * @return Returns the _name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * @return Returns the _serverUrl.
	 */
	//		public final String getServerURL() {
	//			return serverUrl;
	//		}
	/**
	 * @return Returns the _size.
	 */
	public final int getSize() {
		return size;
	}

	/**
	 * @return Returns the version.
	 */
	public final Version getVersion() {
		return version;
	}
	
	/**
	 * Sets the version of the plugin
	 * @param v the version in structured form 
	 */
	public final void setVersion(Version v) {
		 version = v;
	}
	
	/**
	 * Sets the version of the plugin
	 * @param v a String which represents the version 
	 */
	public final void setVersion(String s) {
		 setVersion(Version.valueOf(s));
	}

//	public final Collection getDependencies() {
//		return dependencies.values();
//	}
	/**
	 * @return Returns the docFile.
	 */
	public final String getDocFile() {
		return docRef;
	}
	/**
	 * @return Returns the category.
	 */
	public final String getCategory() {
		return category;
	}
	/**
	 * @return Returns the agentNames (is not all).
	 */
	public final String getAgentNames() {
		return agentNames;
	}

	boolean updateDesktopIniFile(){
		if ((directory != null)&& (directory.isDirectory())){
			name = Utils.getFileNameFromPath(directory.getPath());
			File desktopIniFile = new File(directory,"desktop.ini");
			if (!desktopIniFile.isFile()){
				return(false);
			}
			desktopFile = desktopIniFile;
			return true;
		} else return false;
	}	

	boolean updateInfo() {
		if ((directory != null) && (directory.isDirectory())) {
			name = Utils.getFileNameFromPath(directory.getPath());
			File propFile = new File(directory, name + ".properties");
			if (!propFile.isFile()) {
				System.out.println("Warning: the plugin " + name
						+ " is not a correct plugin");
				return (false);
			}
			File buildF = new File(directory,"build.xml");
			if (buildF.isFile()){
				buildFile = buildF;
			}
			PropertyFile pf = new PropertyFile();
			pf.loadFrom(propFile);
			updateInfo(pf);
			updateDesktopIniFile();
			return true;
		} else {
			System.err.println("Warning: the plugin " + getName()
					+ " does not seem to have been correctly installed");
			return false;
		}
	}

	void updateInfo(PropertyFile pf) {
		name = pf.getProperty("madkit.plugin.name");
		agentNames = pf.getProperty("madkit.plugin.agents");
		authorName = pf.getProperty("madkit.plugin.author");
		version = Version.valueOf(pf.getProperty("madkit.plugin.version"));
		description = pf.getProperty("madkit.plugin.description");
		depends = pf.getProperty("madkit.plugin.depend");
		requires = pf.getProperty("madkit.plugin.requires");
		docRef = pf.getProperty("madkit.plugin.docfile");
		system = pf.getProperty("madkit.plugin.system");
		if ((system == null) || (system.equals("${system.plugin}")))
			system = "false";
		category = pf.getProperty("madkit.plugin.category");
		if ((docRef != null) && (docRef != "${docfile}")) {
			File _docFile = new File(directory, "docs" + File.separator
					+ docRef);
			if (_docFile.isFile()) {
				docPath = _docFile.getPath();
			}
		}
		parseDepends();
		parseRequires();

	}

	public String toString() {
		return getName() + ": " + this.getDescription() + ", "
				+ this.getAuthorName() + ", " + this.getVersion();
	}
}

