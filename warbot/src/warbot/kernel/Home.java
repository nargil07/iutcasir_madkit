/*
* Home.java -Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
*
* This program is free software; you can redistribute it and/or
* modify it under the terms of the GNU General Public License
* as published by the Free Software Foundation; either version 2
* of the License, or any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/
package warbot.kernel;

import java.awt.Point;


public class Home extends BasicBody
{
	/**
	 * Resources are used to create new agents...
	 */
	private int resourcelevel=0;
	final public static int RESOURCEUNIT=400; // with 2 hamburger you may create a new agent..
	final protected static int CREATE=6;	//Creation of agent
	String createWhat=null;

    public Home(WarbotEnvironment theWorld, Brain b, String team)
    {
        super(theWorld,b,"command center",team,40,10000,300);
        setSpeed(5);
    }

    public Home(){
	    super();
        setSpeed(5);
		setDetectingRange(300);
		this.setEnergy(10000);
	}

	public Percept makePercept(double dx, double dy, double d){
	   Percept p = super.makePercept(dx,dy,d);
	   p.setPerceptType("Home");
	   return p;
	}

    public String getEntityInterfaceType()
    {
        return "CommandCenter";
    }

    void getMissileShot(int value)
    {
        energy-=value;
        getShot=true;
        //System.err.println("my energy is "+energy);
        if(energy<0)
            delete();
    }
    
    protected void tryEat()
    {
    	/*System.err.println(this.toString());
    	System.err.println(eatWhat.toString()); */
    	//System.err.println(":: distance  "+distanceFrom(eatWhat));
    	if (distanceFrom(eatWhat) < 3)
      	{
      		increaseResourceLevel(eatWhat.getEnergy());
      		//eating=true;
            eatWhat.delete();
      	}
      	eatWhat=null;
      }
    
    protected void increaseResourceLevel(int v)
      {
    	resourcelevel += v;
      }
    
    public int getResourceLevel(){return resourcelevel;}

    boolean showResourceLevel(){return true;}
    
    public void createAgent(String type){
    	createWhat = type;
        action = CREATE;
    }
    
    private void tryCreateAgent(){
    	double lx = this.getX();
    	double ly = this.getY();
    	if (createWhat != null){
    		try {
    			if (getResourceLevel()>= RESOURCEUNIT){
	    	        getStructure().getAgent().doCommand(new SEdit.NewNodeCommand(createWhat,new Point(0,0)));
	    	        BasicBody r = (BasicBody)((WarbotStructure)getStructure()).getLastEntity();
	    	        r.setHeading(Math.random()*360);
	    	        r.setXY( (radius+r.getRadius()+1)*r.getCosAlpha()+x,(radius+r.getRadius()+1)*r.getSinAlpha()+y);
	    	        resourcelevel -= RESOURCEUNIT;
    			} else {
    				this.setUserMessage("Resources are too low");
    			}
    		} catch(ClassCastException ex){
    			System.err.println(":: "+this+" cannot create " + createWhat + " this is not an agent!");
    		} catch(Exception e){
    			System.err.println(":: "+this+ " error in creating a "+createWhat);
    			System.err.println(e.getMessage());
    			e.printStackTrace();
    		}
    		createWhat = null;
    	}
    }

    void doAction()
    {
    	switch(action)
    	{
    		case EAT:tryEat();break;
    		case CREATE: tryCreateAgent(); break;
    	}
    }

	
}
