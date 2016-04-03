/*
 * NetConfigMessage.java - Created on Oct 21, 2003
 * 
 * Copyright (C) 2003 Sebastian Rodriguez
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
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
 * Last Update: $Date: 2005/02/08 08:23:46 $
 */

package madkit.netcomm;

import madkit.kernel.Message;

/**
 * @author Sebastian Rodriguez - sebastian.rodriguez@utbm.fr
 *
 * @version $Revision: 1.2 $
 */
public final class NetConfigMessage extends Message {
	// public static int DEBUG_MESSAGE=1;
	// public static int STAT_MESSAGE=2;
	//general config
	private static boolean debug;
	
	//broadcasting
	private static boolean acceptBroadCast;
	
	//Stats Configuration
	/**is Stat enabled?*/
	private static boolean enableStat;
	private static long samplePeriod=StatsAgent.DEFAULT_SAMPLE_PERIOD;

	private static int httpPort=8081;
	
	//put all values to default
	static{
		defaultValues();
	}
	
	/**
	 * Creates a new Configuration Message
	 */
	public NetConfigMessage() {
		super();
	}
	
	/**Resets all configuration values to its defaults values.
	 * enableStat: false
	 * samplePeriod: StatsAgent.DEFAULT_SAMPLE_PERIOD
	 * debug: false
	 * acceptBroadcast: true
	 */
	public static void defaultValues(){
		enableStat=true;
		samplePeriod=StatsAgent.DEFAULT_SAMPLE_PERIOD;
		debug=false;
		acceptBroadCast=true;
	}

	/**
	 * @return Returns the acceptBroadCast.
	 * 
	 * @uml.property name="acceptBroadCast"
	 */
	public static final boolean isAcceptBroadCast() {
		return acceptBroadCast;
	}

	/**
	 * @param acceptBroadCast The acceptBroadCast to set.
	 * 
	 * @uml.property name="acceptBroadCast"
	 */
	public static final void setAcceptBroadCast(boolean acceptBroadCast) {
		NetConfigMessage.acceptBroadCast = acceptBroadCast;
	}

	/**
	 * @return Returns the debug.
	 * 
	 * @uml.property name="debug"
	 */
	public static final boolean isDebug() {
		return debug;
	}

	/**
	 * @param debug The debug to set.
	 * 
	 * @uml.property name="debug"
	 */
	public static final void setDebug(boolean debug) {
		NetConfigMessage.debug = debug;
	}

	/**
	 * @return Returns the enableStat.
	 * 
	 * @uml.property name="enableStat"
	 */
	public static final boolean isEnableStat() {
		return enableStat;
	}

	/**
	 * @param enableStat The enableStat to set.
	 * 
	 * @uml.property name="enableStat"
	 */
	public static final void setEnableStat(boolean enableStat) {
		NetConfigMessage.enableStat = enableStat;
	}

	/**
	 * @return Returns the samplePeriod.
	 * 
	 * @uml.property name="samplePeriod"
	 */
	public static final long getSamplePeriod() {
		return samplePeriod;
	}

	/**
	 * @param samplePeriod The samplePeriod to set.
	 * 
	 * @uml.property name="samplePeriod"
	 */
	public static final void setSamplePeriod(long samplePeriod) {
		NetConfigMessage.samplePeriod = samplePeriod;
	}

}
