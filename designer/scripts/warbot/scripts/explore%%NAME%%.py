# explore1.py - A Brain for simple Warbot Explorers, in Python.
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


groupName='warbot-'+self.getTeam()
homeBaseX=0
homeBaseY=0
wait = 0
atHome=0

displayCount=0
prompt=""

def display(s):
	global displayCount
	global prompt
	self.showUserMessage(1)
	self.setUserMessage(s)
	prompt=s
	displayCount=20

def displayManagement():
	global displayCount
	if (displayCount <= 0):
		self.showUserMessage(0)
	else: 
		displayCount=displayCount-1
		self.setUserMessage(prompt)

def activate():
	print 'hello', groupName
	self.randomHeading()
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,"explorer",None)
	self.requestRole(groupName,"mobile",None)
	self.showUserMessage(1)

def end():
	print 'bye bye'

def doIt():
	global homeBaseX
	global homeBaseY
	global atHome
	action='nothing'
	atHome=0
	displayManagement();
	if not self.isMoving():
		self.randomHeading()
	#print 'heading..', self.getHeading()
	percepts = self.getPercepts()
	#lst = [x.getPerceptType() for x in percepts]
	#print 'percepts=',lst, 'nb:', len(percepts)
	dist = self.getCoveredDistance()
	#self.setUserMessage('head:'+str(self.getHeading())+' dist:'+str(dist))
	while not self.isMessageBoxEmpty():
		m = self.readMessage()
		if (m.getAct() == 'baseposition'):
			self.println("message received from "+ str(m.getSender())+", "+str(m.getFromX())+", " +str(m.getFromY()))
			homeBaseX = m.getFromX()
			homeBaseY = m.getFromY()
			
	if len(percepts)>0:
		food = 0
		ennemyHome = 0
		for p in percepts: #find the closest Hamburger... and get it
			if (not ennemyHome and p.getPerceptType()=='Home' and p.getTeam()!=self.getTeam()):
				self.broadcast(groupName,"launcher","homeposition")
				display('Enemy home!!')
				ennemyHome = 1
			if (p.getPerceptType()=='Home' and p.getTeam() == self.getTeam()):
				if (self.distanceTo(p)<=3):
					display("at home")
					atHome=1
					homeBaseX=0		# reinit base position
					homeBaseY=0
			if (not food and not atHome and not self.isBagFull() and p.getPerceptType()=='Food'):
				pmin=p
				food = 1
			if (p.getPerceptType()=='RocketLauncher' and p.getTeam()!=self.getTeam()):
				display('Under attack..')
				self.broadcast(groupName,"launcher","help")
			if (self.isBagFull()):
				goHome()
				return
		if food: 
			bag = self.getBagPercepts()
			display("Food: "+str(len(bag)))
			if self.distanceTo(pmin)<=3: 	#if close enough
				self.take(pmin)				#take it
				return
			else:							#else go towards it
				self.setHeading(self.towards(pmin.getX(),pmin.getY()))
				self.move()
				return
	self.move()	#move if do not eat
		
def goHome():
	global wait
	bag = self.getBagPercepts()
	if (atHome):
		display("dropping")
		for i in range(len(bag)):
			self.drop(i)
	else:
		display( "carrying "+str(len(bag)))
		if (homeBaseX ==0 and homeBaseY == 0):
			if (wait <= 0):
				self.broadcast(groupName,"base","requestposition")
				wait=5
			else:
				wait = wait-1
                self.setHeading(self.towards(homeBaseX,homeBaseY))
                self.move()

