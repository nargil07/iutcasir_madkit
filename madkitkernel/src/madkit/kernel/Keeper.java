/*
 * Created on 27 févr. 2005
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package madkit.kernel;

/**
 * @author Jaco
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public interface Keeper {
	public KernelAddress getAgencyNamed(String name);
	public int joinPlace(Agent agent,String Community ,String place, String pwd);
	public boolean addPlace(PlaceKeeper pK, String placeName);
}