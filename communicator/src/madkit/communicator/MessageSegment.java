/*
* MessageSegment.java - Communicator: the connection module of MadKit
* Copyright (C) 1998-2002 Olivier Gutknecht, Pierre Bommel, Fabien Michel, Thomas Cahuzac
*
* This library is free software; you can redistribute it and/or
* modify it under the terms of the GNU Lesser General Public
* License as published by the Free Software Foundation; either
* version 2.1 of the License, or (at your option) any later version.

* This library is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.

* You should have received a copy of the GNU Lesser General Public
* License along with this library; if not, write to the Free Software
* Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
*/
package madkit.communicator;

class MessageSegment extends Object
{
    private byte[] segment;
    private byte part;
    private boolean lastSegment;
    
    public MessageSegment() {}
    public MessageSegment(byte[] seg, byte p, boolean last)
    {
	segment = seg;
	part = p;
	lastSegment = last;
    }
    
    public byte[] getSegment()
    {
   	return segment;
    }
    
    public byte getPartNumber()
    {
   	return part;
    }
    
    public boolean lastSegment()
    {
   	return lastSegment;
    }
}
