/*
 * SelectedPluginMessage.java - Created on Feb 2, 2004
 * 
 * Copyright (C) 2003-2004 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.

 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Last Update: $Date: 2005/02/01 07:05:34 $
 */

package madkit.pluginmanager;

import madkit.kernel.Message;



/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
class SelectedPluginMessage extends Message {
	private PluginInformation _info=null;
	private String _name=null;
	
	/**
	 * 
	 */
	public SelectedPluginMessage(PluginInformation info) {
		_info=info;
	}
	
	public SelectedPluginMessage(String name) {
		_name = name;
	}

	public PluginInformation getPluginInformation(){
		return _info;
	}
	
	public String getPluginName(){
		if (_info != null){
			return _info.getName();
		}
		else return _name;
	}
}
