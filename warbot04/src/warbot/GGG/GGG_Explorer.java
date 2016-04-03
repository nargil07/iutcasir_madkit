/*
* Warbot: robots battles in MadKit
* Copyright (C) 2000-2002 Fabien Michel, Jacques Ferber
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
package warbot.GGG;

import warbot.kernel.*;

/* Detecteur */
public class GGG_Explorer extends GGG_WB_Mobile {

	// Parametres de la spirale de deplacement
	final static double BORNE_MAX_SPIRALE = 3000;
	final static double AUGMENTATION_DISTANCE_SPIRALE= 0;
	final static double BORNE_MIN_SPIRALE = 3000;
	final static double AUGMENTATION_ANGLE_SPIRALE = -1;

	// Param�tre de la rotation autour d'une escouade
	final static int TAILLE_MIN_CERCLE_ECLAIREUR = 200;
	final static int TAILLE_MAX_CERCLE_ECLAIREUR = 250;

	// Sens de la rotation en spirale
	int sens = 1;

	// Etat interne de l'agent
	public GGG_Explorer() {
    super ();
    myType = BOT_TYPE_Ex;
		nbrMaxEx = 1;
		nbrEx = 1;
  }

	// ----------------------------------------------
	// Activation
	// ----------------------------------------------
	public void activate() {
		super.activate();
	}
	
	// ----------------------------------------------
	// A toi de joueur
	// ----------------------------------------------
  public void doIt() {	
    super.doIt();
	}
	
	// ----------------------------------------------
	// Mise � jour des �tats interne
	// ----------------------------------------------
	protected void introspecter () {
		super.introspecter();
	}

	// ----------------------------------------------
	//	Recalcul du r�le de l'agent
  //		Gestion des changement de r�le
  //		et des actions � faire en cas de changement de r�le
	// ----------------------------------------------
	protected void actualiserRole () {
    super.actualiserRole();
		boolean placeLibre;
		int affectation = -1;

		if (role == ROLE_INCONNU)	{
			bAL.annoncer (ALL_ROLES, ALL_GROUPS, GGG_Msg.MSG_PRES_EX, 0.0, 0.0);
			requestRole(TEAM, ROLE_STR[ROLE_EXPLORATEUR], null);
			role = ROLE_EXPLORATEUR;
		}

		// Demande d'un �claireur par un sergent
		for (int i=0; i < bAL.nbrRequete; i++) {
			// Si je recoit un message d'affectation
			if (bAL.requete[i].msgId == GGG_Msg.MSG_AFFECTATION ) {
				//System.out.println("Ex: Message d'affectation re�u" );
				placeLibre = true;
				for (int j=0; j < bAL.nbrAnnonce; j++) {
					// Si personne n'a pris la place
					if (bAL.annonce[j].msgId == GGG_Msg.MSG_AFFECTE) {
						if (bAL.annonce[j].arg2 == bAL.requete[i].arg2) {
							//System.out.println("Ex: La place est d�j� prise" );
							placeLibre = false;
						}
					}
				}
				if (placeLibre) {
					//System.out.println("Ex: Je prends la place" );
					affectation = i;
				}
			}
		}

		if (affectation >= 0)	{
			// On quitte son r�le pr�c�dent
			leaveRole(TEAM, ROLE_STR[role], null);
			role = (int)bAL.requete[affectation].arg1;
			groupe = (int) bAL.requete[affectation].arg2;
			if (groupe != -1)	{
				//System.out.println("Ex: Je change de r�le et de groupe" );
				// On annonce aux autres son affectation
				createGroup(true, GGG_Msg.GROUPE_ESCOUADE_x + groupe, null, null);
				requestRole(GGG_Msg.GROUPE_ESCOUADE_x + groupe, ROLE_STR[role], null);

				bAL.annoncer (ROLE_EXPLORATEUR, ALL_GROUPS, GGG_Msg.MSG_AFFECTE, 0.0, groupe);
				sergent.id = bAL.requete[affectation].sender;
				sergent.type = BOT_TYPE_RL;
			}
			else {
				//System.out.println("Ex: Je change de r�le dans le m�me groupe" );
				requestRole(TEAM, ROLE_STR[role], null);
			}
		}
	}

  // ----------------------------------------------
  //  R�alisation du r�le
  //		Traitement propre au r�le
  // ----------------------------------------------
	protected void effectuerRole () {
		super.effectuerRole();

		switch (role) {
		case ROLE_ECLAIREUR:
			break;

		case ROLE_INCONNU:
			break;
		}
	}

	// ----------------------------------------------
	// Recalcul de la tactique de l'agent
  //		Gestion des changement de tactique
  //		et des actions � faire en cas de changement de tactique
	// ----------------------------------------------
	protected void actualiserTactique () {
		super.actualiserTactique();

		switch (role)	{
		case ROLE_ECLAIREUR:
			tactique = TACT_ECLAIRER;
			tactTgt = sergent;
			break;

		case ROLE_EXPLORATEUR:
			tactique = TACT_PATROUILLER;
			//tactTgt = butTgt;
			break;

		case ROLE_INCONNU:
			tactique = TACT_INCONNU;
		}
	}

  // ----------------------------------------------
  //  R�alisation de la tactique
  //		Traitement propre � la tactique
	// ----------------------------------------------
	protected void effectuerTactique () {
		super.effectuerTactique();
	}

	// ----------------------------------------------
	// Recalcul du but de l'agent
  //		Gestion des changement de but
  //		et des actions � faire en cas de changement de but
	// ----------------------------------------------
	protected void actualiserBut () {
		super.actualiserBut();

		switch (tactique)	{
		case TACT_PATROUILLER:
			but = BUT_DEPLACEMENT;
			if (cptDureeBut < 0) {
				sens = -sens;
				// Dur�e al�atoire de la rotation dans ce sens
				cptDureeBut = (int) (Math.random() * 100);
			}
			butTgt = actionTgt;
			break;

		case TACT_ECLAIRER:
			but = BUT_TOURNER_AUTOUR;
			butTgt = tactTgt;
			break;

		case TACT_INCONNU:
			but = BUT_INCONNU;
			break;
		}
	}

  // ----------------------------------------------
  //  R�alisation du but
  //		Traitement propre au but
  // ----------------------------------------------
	protected void effectuerBut () {
		super.effectuerBut();
		GGG_Target v = new GGG_Target();

		switch ( but ) {
		case BUT_DEPLACEMENT :
			spirale(butTgt, BORNE_MIN_SPIRALE, BORNE_MAX_SPIRALE, AUGMENTATION_ANGLE_SPIRALE * sens, AUGMENTATION_DISTANCE_SPIRALE);
			GGG_Target evit = new GGG_Target();
			calculerEvitement(evit);
			actionTgt.x = butTgt.x - evit.x;
			actionTgt.y = butTgt.y - evit.y;
			actionTgt.type = butTgt.type;
			setHeading(towards(actionTgt.x,actionTgt.y));
			move();
			break;
		
		case BUT_TOURNER_AUTOUR:
			// Si un eclaireur perds de vue son sergent il l'appelle
			if (!localiserAmi(butTgt)) {
				bAL.annoncer (butTgt.id, GGG_Msg.MSG_PERDU, 0.0, 0.0);
				// Si on a re�u un message du sergent on actualise sa position
				bAL.localiserSender(butTgt);
			}
			actionTgt = suivre (butTgt, TAILLE_MAX_CERCLE_ECLAIREUR);
			// actionTgt = tournerAutour (butTgt, TAILLE_MIN_CERCLE_ECLAIREUR, TAILLE_MAX_CERCLE_ECLAIREUR);
			actionTgt = orienterAvecCorrection(actionTgt, 1);
			move();
			break;
		}

 		bAL.sendAll(this);
	}


	// ----------------------------------------------
	// ----------------------------------------------
	//
	//  Fonctions utilitaires pour les explorateurs
	//
	// ----------------------------------------------
	// ----------------------------------------------



}