# Warbot - Base - (C) 2002 - Jacques Ferber

groupName='warbot-dumbTeam-'+self.getTeam()

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
	print 'Base', groupName
	self.createGroup(0,groupName,None,None)
	self.requestRole(groupName,'base',None)

def end():
	print 'Base morte'

def doIt():
	# gestion des messages utilisateurs
	displayManagement()
	# Reception des messages et idenification du RL le plus proche
	while not self.isMessageBoxEmpty():
		m = self.readMessage()
		display("message: "+str( m.getAct()))
		if (m.getAct() == 'requestposition'):
			self.println("message received from "+ str(m.getSender())+", "+str(m.getFromX())+", " +str(m.getFromY()))
			self.send(m.getSender(),"baseposition")

	if (self.getResourceLevel() >= 400):
		self.createAgent("redMissileLauncher")
	percepts = self.getPercepts()
	if len(percepts)>0:
		for p in percepts:
			if (p.getPerceptType()=='Food' and self.distanceTo(p) <= 3):
				self.eat(p)
				display("eating")
