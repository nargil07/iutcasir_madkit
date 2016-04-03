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
public interface Mobile {
	public KernelAddress getMyAgency();
	public AgentAddress getMyBirthAddress();
	public void setMirrorAgent(Mirror mirror);
	public Mirror createMirror(KernelAddress dest);
}
