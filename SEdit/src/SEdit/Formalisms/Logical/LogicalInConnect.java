/*
* LogicalInConnect.java - SEdit, a tool to design and animate graphs in MadKit
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
package SEdit.Formalisms.Logical;



import java.util.*;

import java.lang.Math;

import java.io.*;



import SEdit.Graphics.*;

import SEdit.*;





/** A connector for logical gates

 */



public class LogicalInConnect extends SInConnect implements SLogicalElement

{

    boolean value=false;



    public boolean getValue(){return(value);}

    public void setValue(boolean b){value=b;}

    

    public void receive(boolean b) 

    {

	setValue(b);

	((SLogicalComponent) getComponent()).activate();

    }

}

















