package warbot.CM;

import warbot.kernel.*;

public class CMExplorer extends Brain
{
	String groupName	="warbot-";
	String roleName		="Explorer";
	
	int memeDirection	=30;
	int tempsMax		=8;
	int temps			=tempsMax;	// variable permettant de garder la meme direction pendant tempsMax it�rations

	public CMExplorer(){}

	public void activate()
	{
		groupName		=groupName+getTeam();  // -> warbot-CM
		randomHeading();  // direction al�atoire
		println("Explorateur CM op�rationnel");
		createGroup(false,groupName,null,null);
		requestRole(groupName,roleName,null);
		requestRole(groupName,"mobile",null);
		
	}
	
	public void decompteTemps()
	{
		temps--;	
	}

	public void doIt()
	{		
		String chaineAide		="HELP-E";
		String chaineAtak		="ATAQ";
		double positionEnnemiX 	= 0;
		double positionEnnemiY 	= 0;
		String ennemiX 			= "";
		String ennemiY 			= "";
				
		if (!isMoving())	// si bloqu� : direction al�atoire
			randomHeading();
		
		
		// 1. Si base ennemie trouv�e : broadcast
		Percept[]objetsPercus = getPercepts();  // entit�s dans le p�rim�tre de perception
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entit�s per�ues...
		{
			Percept objetCourant = objetsPercus[i];
			if (!objetCourant.getTeam().equals(getTeam()) && objetCourant.getPerceptType().equals("Home")) // si objet courant = base ennemie
			{
				ennemiX = Double.toString(objetCourant.getX());	
				ennemiY = Double.toString(objetCourant.getY());				
				broadcast(groupName,"Launcher",chaineAtak,ennemiX,ennemiY);
			}
		}		
			
		// 2. Si attaqu� : demande d'aide sans argument
		if (getShot())
		{
			println("explorer attaqu�");
			broadcast(groupName,"Launcher",chaineAide,"0","0");			
		}
			
		// 3. si rocketlauncher ennemi rep�r�
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entit�s per�ues...
		{
			Percept objetCourant = objetsPercus[i];
			if (!objetCourant.getTeam().equals(getTeam()) && objetCourant.getPerceptType().equals("RocketLauncher")) // si RocketLauncher ennemi
			{
				// strat�gie d'�vitement
				positionEnnemiX = objetCourant.getX();	// abscisse de l'ennemi
				positionEnnemiY = objetCourant.getY();	// ordonn�e de l'ennemi
				ennemiX = Double.toString(objetCourant.getX());	
				ennemiY = Double.toString(objetCourant.getY());				
//				println("launcher ennemi rep�r�");
//				println(ennemiX);
//				println(ennemiY);	
				broadcast(groupName,"Launcher",chaineAide,ennemiX,ennemiY);
				for(int j=0;j<objetsPercus.length;j++)  // pour toutes les entit�s per�ues...
				{
					Percept objetCourant2 = objetsPercus[j];
					if ((getHeading()-(towards(objetCourant2.getX(),objetCourant.getY()))>=20) && (objetCourant2.getPerceptType().equals("Obstacle")))
					// si d�tecte un obstacle et que cet obstacle est dans le champ de la direction de l'agent	
					{
						if (temps==tempsMax)	// si on vient de rep�rer l'obstacle
						{
							decompteTemps();
							setHeading(towards(-positionEnnemiX/2,-positionEnnemiY)); // on �vite ET l'ennemi, Et l'obstacle
						}
						else	
						{
							if (temps==0)
								{temps=tempsMax;}
							else	// temps entre 8 et 0
								{decompteTemps();}		
						}
						move();
						return;
					}					
				}	
				setHeading(towards(-positionEnnemiX,-positionEnnemiY)); // direction oppos�e � l'ennemi rep�r�
			}
		}
		
		// 4. d�placement al�atoire (randomHeading au d�but)
		temps=tempsMax;	//remise � 0 du compteur : on est hors de risque...
		move();
		return;			
	}
}