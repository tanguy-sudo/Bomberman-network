package controller;
import models.*;
import models.agent.Agent;
import utils.AgentAction;
import view.PanelBomberman;
import view.ViewBombermanGame;

import java.util.ArrayList;

/**
 * 
 * @author tanguy
 * Classe qui gère les différentes vues ainsi que les différents modules
 */
public class ControllerBombermanGame extends AbstractController{
	public PanelBomberman getpPanelBomberman() {
		return pPanelBomberman;
	}

	public void setpPanelBomberman(PanelBomberman pPanelBomberman) {
		this.pPanelBomberman = pPanelBomberman;
	}

	public ViewBombermanGame getpViewBombermanGame() {
		return pViewBombermanGame;
	}

	public void setpViewBombermanGame(ViewBombermanGame pViewBombermanGame) {
		this.pViewBombermanGame = pViewBombermanGame;
	}

	public InputMap getpInputMap() {
		return pInputMap;
	}

	public void setpInputMap(InputMap pInputMap) {
		this.pInputMap = pInputMap;
	}
	public Game getpGame() {
		return this.pGame;
	}


	private PanelBomberman pPanelBomberman;
	private ViewBombermanGame pViewBombermanGame;
	private InputMap pInputMap;
	
	/**
	 * Initialise les différents attributs et lance la vue ViewStart
	 */
	public ControllerBombermanGame() {
		this.pMapName = "";
		this.pInputMap = null;
		this.pPanelBomberman = null;
		this.pViewBombermanGame = null;
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
			this.pMapName = pathLayout;
			this.pInputMap = new InputMap(pathLayout);
			/*this.pPanelBomberman = new PanelBomberman(this.pInputMap.getSizeX(),
													  this.pInputMap.getSizeY(), 
													  this.pInputMap.get_walls(), 
													  this.pInputMap.getStart_breakable_walls(), 
													  this.pInputMap.getStart_agents());
			this.pViewBombermanGame = new ViewBombermanGame(this.pPanelBomberman, this);*/

			
			this.pGame = new BombermanGame(10000, this.pInputMap, Integer.parseInt(niveau), this, manual);
			//this.pGame.addPropertyChangeListener(this.pViewBombermanGame);
			this.pGame.init();
			this.setSpeed(3);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}

	public void lunchGame(){
		this.play();
	}

	
	/**
	 * Mets à jour l'action que l'utilisateur souhaite effectuer avec le bomberman
	 * @param action : Action que l'utilisateur a saisie au clavier
	 */
	public void updateActionBomberman(AgentAction action) {
		this.pGame.updateActionUser(action);
	}
}
