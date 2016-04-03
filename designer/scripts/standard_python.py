# %%NAME%%.py - an agent in Python.
#
# Copyright (C) 2002 Jacques Ferber
#
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# as published by the Free Software Foundation; either version 2
# of the License, or any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.

from madkit.kernel import Agent

    
myCommunity="%%PROJECT%%"
myGroup="myGroup"
myRole="myRole"
alive = 1
	
	
def activate():
    	self.println("Hello I'm an agent !")
	# create a distributed group 1 for distributed, 0 for local group
	r = self.createGroup(1, myCommunity, myGroup, None, None)
	if (r != 1):
		self.println("The group has already been created")
	self.requestRole(myCommunity, myGroup, myRole,None)


def live():
	self.println("Hello world..")
    	n = 3
    	while (n > 0) :
        	self.println('I am OK : '+str(n))
        	self.pause(300)
        	n = n-1
	while(alive):
		# wait for a message
        	m = self.waitNextMessage()
		# and handle it
        	handleMessage(m)

def handleMessage(m):
	#You should describe here the  behavior of your agent
	# upon reception of a String message
	self.println("message: "+m.getString())

def end():
    	self.println('I am dead, arghhh')
            
