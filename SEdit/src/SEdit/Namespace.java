/*
* Namespace.java - SEdit, a tool to design and animate graphs in MadKit
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

import java.util.Hashtable;

 class Namespace extends Hashtable
{
    Structure structure;
    
    public Namespace(Structure s)
    {
	super();
	structure=s;
    }
    
    public String lookupID(String s)
    {
	if (containsKey(s))
	    return (String)get(s);
	else
	    return s;
    }
    public String putID(String s)
    {
	if (structure.existID(s))
	    {
		if (s.charAt(0)=='A')
		    {
			String id = structure.newArrowID();
			put(s,id);
			return id;
		    }
		else
		    {
			String id = structure.newNodeID();
			put(s,id);
			return id;
		    }
	    }
	else
	    return s;
    }
    
    
    
}
