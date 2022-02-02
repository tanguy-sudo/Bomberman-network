package controller;

import models.BombermanGame;
import models.Game;
import models.InputMap;
import utils.AgentAction;

/**
 * @author tanguy
 * Classe qui gère les différentes vues ainsi que les différents modules
 */
public class ControllerBombermanGame extends AbstractController{
	private InputMap pInputMap;
	
	/**
	 * Initialise les différents attributs et lance la vue ViewStart
	 */
	public ControllerBombermanGame() {
		this.pMapName = "";
		this.pInputMap = null;
		this.pGame = null;
	}	
	
	/**
	 * Fonction qui initialise une map et une game et instancie une vue qui va contenir cette map
	 * @param pathLayout : Chemin du layout
	 * @param niveau : Niveau de la partie
	 * @param manual : Indique si la partie se joue manuellement
	 */
	public void initGame(String pathLayout, String niveau, boolean manual) {
		try {
			this.pInputMap = new InputMap(pathLayout);
			this.pGame = new BombermanGame(10000, this.pInputMap, Integer.parseInt(niveau), this, manual);
			this.pGame.init();
		} catch (Exception e) {
			e.getMessage();
		}
	}

	/**
	 * Mets à jour l'action que l'utilisateur souhaite effectuer avec le bomberman
	 * @param action : Action que l'utilisateur a saisie au clavier
	 */
	public void updateActionBomberman(AgentAction action) {
		this.pGame.updateActionUser(action);
	}

	public Game getpGame() {return this.pGame;}

}
