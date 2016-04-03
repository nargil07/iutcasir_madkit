/*
* SOutConnect.java - SEdit, a tool to design and animate graphs in MadKit
* Copyright (C) 1998-2002 Jacques Ferber, Olivier Gutknecht
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
package SEdit;

import java.util.Vector;


/** la classe abstraite qui correspond � la notion de connecteur
    dans une structure qui relie un ensemble d'arcs � un noeud
    Bas� sur SInPin, mais se connecte � des SComponent
    
    **/

public class SOutConnect extends SConnect 
{
    protected  boolean isConnectable(SArrow s, boolean isTarget)
    {
	return !isTarget;
    }

    protected void addInArrow(SArrow a)
    {
    }
    
    protected  void addOutArrow(SArrow a)
    {
	arrows.addElement(a);
    }  
    
    public Vector getOutArrows() { return arrows;}
    public Vector getInArrows() { return null;}
}
