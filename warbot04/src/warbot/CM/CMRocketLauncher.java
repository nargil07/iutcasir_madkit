package warbot.CM;

import warbot.kernel.*;

public class CMRocketLauncher extends Brain
{
	String groupName = "warbot-";
	String roleName  = "Launcher";

	int tempsMax		= 10;
	int temps			= tempsMax;	// variable permettant de garder la meme direction pendant tempsMax it�rations

	public CMRocketLauncher(){}

	public void activate()
	{
		groupName=groupName+getTeam();  // -> warbot-CM
		randomHeading();  				// direction al�atoire
		println("RocketLauncher CM op�rationnel");
		createGroup(false,groupName,null,null);
		requestRole(groupName,roleName,null);
		requestRole(groupName,"mobile",null);
	}
	
	public void desobstination()
	{
		//
		if (!isMoving())	// blocage
		{
			if(temps != tempsMax )
			{
				temps = tempsMax;	
			}
			randomHeading();
			temps --;
			move();
			return;
		}
		if(temps < tempsMax && temps > 0)	// direction al�atoire a d�j� �t� prise	
		{
			temps --;
			move();
			return;
		}
		if(temps == 0)	// temps de d�sobstination �coul�
		{
			temps = tempsMax;
		}		
	}

	public void gestionTir(double directionTir,int tailleTabAmis,double[][] tabAmis)	// pour �viter de tirer sur ses amis
	{
		println("gestion du tir");
		println("direction du tir :"+Double.toString(directionTir));
		for(int i=0;i<tailleTabAmis;i++)
		{
			println("entr�e dans le tableau");
			println("direction de l'ami : "+Double.toString(towards(tabAmis[0][i],tabAmis[1][i])));
			if((directionTir-towards(tabAmis[0][i],tabAmis[1][i])>0 && directionTir-towards(tabAmis[0][i],tabAmis[1][i])<20) || (towards(tabAmis[0][i],tabAmis[1][i])-directionTir>0 && towards(tabAmis[0][i],tabAmis[1][i])-directionTir<20) || (directionTir-towards(tabAmis[0][i],tabAmis[1][i])>340) || (towards(tabAmis[0][i],tabAmis[1][i])-directionTir>340))
			{
				println("desobstination");
				desobstination();
				return;
			}
		}
		println("pas desobstination");
		launchRocket(directionTir);	
	}

	public void doIt()
	{		
		// variables pour gestion des messages
		double[][] tabAtaq			= new double[4][100];	// tableau des message d'attaque : premi�re colonne contient l'X, deuxi�me colonne contient l'Y de la base
		double[][] tabHelpBL		= new double[4][100];	// tableau des message d'aide venant des bases concernant des launchers : premi�re colonne X de l'ennemi si non vide, deuxi�me colonne Y de l'ennemi si non vide, troisi�me colonne X envoyeur, quatri�me colonne Y envoyeur
		double[][] tabHelpBE		= new double[4][100];	// tableau des message d'aide venant des bases concernant des explorers : premi�re colonne X de l'ennemi si non vide, deuxi�me colonne Y de l'ennemi si non vide, troisi�me colonne X envoyeur, quatri�me colonne Y envoyeur
		double[][] tabHelpE			= new double[4][100];	// tableau des message d'aide venant des explorers : premi�re colonne X de l'ennemi si non vide, deuxi�me colonne Y de l'ennemi si non vide, troisi�me colonne X envoyeur, quatri�me colonne Y envoyeur
		double[][] tabHelpL			= new double[4][100];	// tableau des message d'aide venant des launchers : premi�re colonne X de l'ennemi si non vide, deuxi�me colonne Y de l'ennemi si non vide, troisi�me colonne X envoyeur, quatri�me colonne Y envoyeur
		int comptAtaq 				= 0;					// compteur du tableau tabAtaq
		int comptHelpBL				= 0;					// compteur du tableau tabHelpBL
		int comptHelpBE				= 0;					// compteur du tableau tabHelpBE
		int comptHelpE 				= 0;					// compteur du tableau tabHelpE
		int comptHelpL 				= 0;					// compteur du tableau tabHelpL
		int tailleAtaq				= 0;					// taille finale des diff�rents tableaux
		int tailleHelpBL			= 0;					// taille finale des diff�rents tableaux
		int tailleHelpBE			= 0;					// taille finale des diff�rents tableaux
		int tailleHelpE				= 0;					// taille finale des diff�rents tableaux
		int tailleHelpL				= 0;					// taille finale des diff�rents tableaux
		// variable pour gestion des objets per�us
		double[][] tabMyTeam		= new double[2][50];	// tableau contenant les coordonn�es relatives des membres de notre �quipe
		double[] tabLauncher		= new double[4];		// tableau contenant les coordonn�es relatives, l'�nergie et la distance du launcher ennemi ayant le moins d'�nergie tout en �tant le plus proche
		double[] tabExplorer		= new double[4];		// tableau contenant les coordonn�es relatives, l'�nergie et la distance de l'explorer ennemi ayant le moins d'�nergie tout en �tant le plus proche
		double[] tabBase			= new double[4];		// tableau contenant les coordonn�es relatives, l'�nergie et la distance de la base ennemie ayant le moins d'�nergie tout en �tant la plus proche
		int comptMyTeam				= 0;					// compteur du tableau tabMyTeam
		int tailleMyTeam			= 0;					// taille finale du tableau tabMyTeam
		// initialisation de l'�nergie et de la distance dans les tableaux des ennemis per�us
		tabLauncher[2]				= 0;
		tabLauncher[3]				= 0;
		tabExplorer[2]				= 0;
		tabExplorer[3]				= 0;
		tabBase[2]					= 0;
		tabBase[3]					= 0;
		// variables de gestion des distances
		int distanceEnnemi			= 80;
		int distanceAmis 			= 80;				// distance � respecter pour �viter obstination rejoindre amis (cas Y)
		int distanceMinAmis 		= 30;				// distance au del� de laquelle il ne faut pas s'approcher des amis (cas Y)
		// variables pour l'envoi de messages
		String actMessageAide 		= "HELP-L";			// cas o� risque d'attaque ennemie
		String actMessageAttaque	= "ATAQ";			// cas de d�tection de la base ennemie
		String argMessageX 			= "";				// pour envoyer position relative en X de l'ennemi
		String argMessageY 			= "";				// pour envoyer position relative en Y de l'ennemi
		WarbotMessage messCourant	= null;
		// variables diverses
		double directionX			= 0;
		double directionY			= 0;
		int seuilEnergieBase		= 6000;				// �nergie seuil pour "se d�fendre" vs "se sacrifier"
		
		// r�cup�ration et classement des messages
		while((messCourant = readMessage())!= null)
		{
			// message d'attaque de base ennemie : les coordonn�es sont toujours pr�sentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "ATAQ")
			{
				tabAtaq[0][comptAtaq] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabAtaq[1][comptAtaq] = Double.valueOf(messCourant.getArg2()).doubleValue();
				tabAtaq[2][comptAtaq] = messCourant.getFromX();
				tabAtaq[3][comptAtaq] = messCourant.getFromY();
				comptAtaq++;
			}
			// message d'aide d'une base concernant les launchers : les coordonn�es de l'ennemi sont toujours pr�sentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-BL")
			{
				tabHelpBL[0][comptHelpBL] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabHelpBL[1][comptHelpBL] = Double.valueOf(messCourant.getArg2()).doubleValue();	
				tabHelpBL[2][comptHelpBL] = messCourant.getFromX();
				tabHelpBL[3][comptHelpBL] = messCourant.getFromY();
				comptHelpBL++;
			}
			// message d'aide d'une base concernant les explorers: les coordonn�es de l'ennemi sont toujours pr�sentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-BE")
			{
				tabHelpBE[0][comptHelpBE] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabHelpBE[1][comptHelpBE] = Double.valueOf(messCourant.getArg2()).doubleValue();	
				tabHelpBE[2][comptHelpBE] = messCourant.getFromX();
				tabHelpBE[3][comptHelpBE] = messCourant.getFromY();
				comptHelpBE++;
			}
			// message d'aide d'un launcher : les coordonn�es de l'ennemi sont toujours pr�sentes en argument du message
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-L")
			{
				// un launcher ne prend pas en compte ses propres messages
				if(messCourant.getFromX() != 0 && messCourant.getFromY() != 0)
				{
					tabHelpL[0][comptHelpL] = Double.valueOf(messCourant.getArg1()).doubleValue();
					tabHelpL[1][comptHelpL] = Double.valueOf(messCourant.getArg2()).doubleValue();	
					tabHelpL[2][comptHelpL] = messCourant.getFromX();
					tabHelpL[3][comptHelpL] = messCourant.getFromY();
					comptHelpL++;
				}
			}
			// message d'aide d'un explorer : pas de coordonn�es de l'ennemi en argument si message suite � tir
			if(messCourant.getAct() != null && messCourant.getAct() == "HELP-E")
			{
				tabHelpE[0][comptHelpE] = Double.valueOf(messCourant.getArg1()).doubleValue();
				tabHelpE[1][comptHelpE] = Double.valueOf(messCourant.getArg2()).doubleValue();	
				tabHelpE[2][comptHelpE] = messCourant.getFromX();
				tabHelpE[3][comptHelpE] = messCourant.getFromY();
				comptHelpE++;
			}
		}
		tailleAtaq 	= comptAtaq;
		tailleHelpBL = comptHelpBL;
		tailleHelpBE = comptHelpBE;
		tailleHelpE = comptHelpE;
		tailleHelpL = comptHelpL;
		comptAtaq	= 0;
		comptHelpBL	= 0;
		comptHelpBE	= 0;
		comptHelpE 	= 0;
		comptHelpL	= 0;
		// fin r�cup�ration et classement des messages
		
		// r�cup�ration et classement des objets per�us
		Percept[] objetsPercus = getPercepts();  		// entit�s dans le p�rim�tre de perception
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entit�s per�ues...
		{
			Percept objetCourant = objetsPercus[i];
			if(objetCourant.getTeam().equals(getTeam()))	// objet de mon �quipe
			{
				tabMyTeam[0][comptMyTeam] = objetCourant.getX();
				tabMyTeam[1][comptMyTeam] = objetCourant.getY();
				comptMyTeam++;	
			}
			else	// on ne garde que les ennemis d'�nergie minimum les plus pr�s de chaque type	
			{
				// ennemi de type base
				if(objetCourant.getPerceptType().equals("Home"))			// objet "ennemi" de type base
				{
					if(objetCourant.getEnergy()<tabBase[2] || tabBase[2] == 0)
					{
						tabBase[0] = objetCourant.getX();
						tabBase[1] = objetCourant.getY();
						tabBase[2] = objetCourant.getEnergy();
						tabBase[3] = objetCourant.getDistance();
					}
					else
						if(objetCourant.getEnergy()==tabBase[2] && objetCourant.getDistance() < tabBase[3])
						{
							tabBase[0] = objetCourant.getX();
							tabBase[1] = objetCourant.getY();
							tabBase[2] = objetCourant.getEnergy();
							tabBase[3] = objetCourant.getDistance();	
						}
				}
				// ennemi de type explorer
				if(objetCourant.getPerceptType().equals("Explorer"))		// objet "ennemi" de type explorer
				{
					if(objetCourant.getEnergy()<tabExplorer[2] || tabExplorer[2] == 0)
					{
						tabExplorer[0] = objetCourant.getX();
						tabExplorer[1] = objetCourant.getY();
						tabExplorer[2] = objetCourant.getEnergy();
						tabExplorer[3] = objetCourant.getDistance();
					}
					else
						if(objetCourant.getEnergy()==tabExplorer[2] && objetCourant.getDistance() < tabExplorer[3])
						{
							tabExplorer[0] = objetCourant.getX();
							tabExplorer[1] = objetCourant.getY();
							tabExplorer[2] = objetCourant.getEnergy();
							tabExplorer[3] = objetCourant.getDistance();	
						}
				}
				// ennemi de type launcher
				if(objetCourant.getPerceptType().equals("RocketLauncher"))	// objet "ennemi" de type launcher
				{
					if(objetCourant.getEnergy()<tabLauncher[2] || tabLauncher[2] == 0)
					{
						tabLauncher[0] = objetCourant.getX();
						tabLauncher[1] = objetCourant.getY();
						tabLauncher[2] = objetCourant.getEnergy();
						tabLauncher[3] = objetCourant.getDistance();
					}
					else
						if(objetCourant.getEnergy()==tabLauncher[2] && objetCourant.getDistance() < tabLauncher[3])
						{
							tabLauncher[0] = objetCourant.getX();
							tabLauncher[1] = objetCourant.getY();
							tabLauncher[2] = objetCourant.getEnergy();
							tabLauncher[3] = objetCourant.getDistance();	
						}
				}
			}			
		}
		tailleMyTeam 	= comptMyTeam;
		comptMyTeam 	= 0;
		// fin r�cup�ration et classement des objets per�us
		
		// on ordonne les actions principalement en fonction des objets per�us et des messages re�us
		// A : une base ennemie est � port�e et le launcher est en position de tirer et/ou de se sacrifier
		if(tabBase[2]!=0 && tabBase[3]!=0)
		{
			// pr�vient les autres
			argMessageX = Double.toString(tabBase[0]);	
			argMessageY = Double.toString(tabBase[1]);
			broadcast (groupName,"Launcher",actMessageAttaque,argMessageX,argMessageY);
			if((!getShot()) || (getShot() && tabBase[2] < seuilEnergieBase))	
			{
				// tire
				gestionTir(towards(tabBase[0],tabBase[1]),tailleMyTeam,tabMyTeam);
				//launchRocket(towards(tabBase[0],tabBase[1]));
				return;
			}	
		}
		
		// B : un launcher ennemi est � port�e
		if(tabLauncher[2]!=0 && tabLauncher[3]!=0)
		{
			// demande aide
			argMessageX = Double.toString(tabLauncher[0]);	
			argMessageY = Double.toString(tabLauncher[1]);
			broadcast (groupName,"Launcher",actMessageAide,argMessageX,argMessageY);
			// tire
			gestionTir(towards(tabLauncher[0],tabLauncher[1]),tailleMyTeam,tabMyTeam);
			//launchRocket(towards(tabLauncher[0],tabLauncher[1]));
			return;
		}
		
		// C : r�ception de demande d'aide de la base car launchers ennemis rep�r�s dans zone de s�curit�
		if (tailleHelpBL > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			/* il faudrait s�lectionner la base la plus rapproch�e */
			// arbitrairement on va vers le dernier launcher ayant envoy� un message
			directionX = tabHelpBL[0][tailleHelpBL-1] + tabHelpBL[2][tailleHelpBL-1];
			directionY = tabHelpBL[1][tailleHelpBL-1] + tabHelpBL[3][tailleHelpBL-1];
			setHeading(towards(directionX,directionY));
			move();
			return;	
		}
			
		// D : r�ception de demande d'aide d'un launcher attaqu�
		if (tailleHelpL > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			/* il faudrait s�lectionner le launcher le plus rapproch� */
			// arbitrairement on va vers le dernier launcher ayant envoy� un message
			directionX = tabHelpL[0][tailleHelpL-1] + tabHelpL[2][tailleHelpL-1];
			directionY = tabHelpL[1][tailleHelpL-1] + tabHelpL[3][tailleHelpL-1];
			setHeading(towards(directionX,directionY));
			move();
			return;	
		}
		
		// E : r�ception d'un message d'attaque de la base ennemie
		if (tailleAtaq > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			directionX = tabAtaq[0][tailleAtaq-1] + tabAtaq[2][tailleAtaq-1];
			directionY = tabAtaq[1][tailleAtaq-1] + tabAtaq[3][tailleAtaq-1];
			/* a voir : on ne bouge vers la base uniquement si on n'est pas d�j� dessus */
			setHeading(towards(directionX,directionY));
			move();
			return;
		}
		
		// W : on a pour objet per�u un explorer
		if(tabExplorer[2]!=0 && tabExplorer[3]!=0)
		{
			// tire
			gestionTir(towards(tabExplorer[0],tabExplorer[1]),tailleMyTeam,tabMyTeam);
			//launchRocket(towards(tabExplorer[0],tabExplorer[1]));
			return;
		}
		
		// X : r�ception de demande d'aide de la base car explorers ennemis rep�r�s dans zone de s�curit�
		if (tailleHelpBE > 0)
		{
			desobstination();
			if(temps != tempsMax ) return;
			/* il faudrait s�lectionner la base la plus rapproch�e */
			// arbitrairement on va vers le dernier launcher ayant envoy� un message
			directionX = tabHelpBE[0][tailleHelpBE-1] + tabHelpBE[2][tailleHelpBE-1];
			directionY = tabHelpBE[1][tailleHelpBE-1] + tabHelpBE[3][tailleHelpBE-1];
			setHeading(towards(directionX,directionY));
			move();
			return;	
		}
			
		// Y : rejoindre ami per�u
		for(int i=0;i<objetsPercus.length;i++)  // pour toutes les entit�s per�ues...
		{
			Percept objetCourant = objetsPercus[i];
			if (objetCourant.getTeam().equals(getTeam()) && objetCourant.getPerceptType().equals("RocketLauncher") && distanceTo(objetCourant) > distanceAmis && distanceTo(objetCourant) < distanceMinAmis)
			{
				if (!isMoving())	// cas o� il a blocage : changement de direction
				{
					randomHeading();
				}
				else	// on va vers l'autre
				{
					setHeading(towards(objetCourant.getX(),objetCourant.getY())); //  r�cup�re la direction de l'ami
				}
				move();
				return;
			}
		}
		// Z : d�placement al�atoire
		if (!isMoving())
		{
			randomHeading();
		}	
		move();
		return;	
	}
}