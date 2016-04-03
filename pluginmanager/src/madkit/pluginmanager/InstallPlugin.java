/*
 * InstallPlugin.java - Created on Feb 3, 2004
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
 * Last Update: $Date: 2004/02/16 14:52:23 $
 */

package madkit.pluginmanager;

import java.io.File;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.1 $
 */
class InstallPlugin {
	public static final int DIRECT_REQUEST=-99;
	
	PluginInformation pluginInformation;
	int dependsOn=0;
	boolean downloading=false;
	File file;
	
	public InstallPlugin(PluginInformation info){
		pluginInformation=info;
	}
}
