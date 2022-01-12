package controller;
import models.*;
import models.agent.Agent;
import utils.AgentAction;
import view.PanelBomberman;
import view.ViewBombermanGame;
import view.ViewCommand;
import view.ViewEnd;
import view.ViewStart;
import java.util.ArrayList;

/**
 * 
 * @author tanguy
 * Classe qui gère les différentes vues ainsi que les différents modèles
 */
public class ControllerBombermanGame extends AbstractController{
	
	private PanelBomberman pPanelBomberman;
	private ViewBombermanGame pViewBombermanGame;
	private InputMap pInputMap;
	private ViewEnd pViewEnd;
	
	/**
	 * Initialise les différents attributs et lance la vue ViewStart
	 */
	public ControllerBombermanGame() {
		this.pMapName = "";
		this.pInputMap = null;
		this.pPanelBomberman = null;
		this.pViewBombermanGame = null;
		this.pGame = null;
		this.pViewEnd = null;
		this.lunchViewStart();
	}	
	
	/**
	 * Fonction qui initialise une map et une game et instancie une vue qui va contenir cette map
	 * @param pathLayout : Chemin du layout
	 * @param niveau : Niveau de la partie
	 * @param fileName : Nom du du layout
	 * @param manual : Indique si la partie se joue manuellement
	 */
	public void lunchGame(String pathLayout, String niveau, String fileName, boolean manual) {
		try {
			this.pMapName = pathLayout;
			this.pInputMap = new InputMap(pathLayout);
			this.pPanelBomberman = new PanelBomberman(this.pInputMap.getSizeX(), 
													  this.pInputMap.getSizeY(), 
													  this.pInputMap.get_walls(), 
													  this.pInputMap.getStart_breakable_walls(), 
													  this.pInputMap.getStart_agents());
			this.pViewBombermanGame = new ViewBombermanGame(this.pPanelBomberman, this);
			
			ViewCommand viewCommand = new ViewCommand(this, fileName, niveau);
			
			this.pGame = new BombermanGame(10000, this.pInputMap, Integer.parseInt(niveau), this, manual);
			this.pGame.addPropertyChangeListener(viewCommand);
			this.pGame.init();
			this.pGame.addPropertyChangeListener(this.pViewBombermanGame);		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.getMessage();
		}
	}
	
	/**
	 * Affiche la vue de fin de partie
	 * @param result : Indique si la partie est gagné, perdu ou s'il y a égalité
	 * @param listAgentEnemy : Liste des agents ennemis
	 * @param listAgentAlly Liste des agents bombermans
	 */
	public void lunchViewEnd(int result, ArrayList<Agent> listAgentEnemy, ArrayList<Agent> listAgentAlly) {
		int countAgent = 0;
		int countEnemy = 0;
		
		for(Agent agent : listAgentAlly) {
			if(agent.getLiving()) countAgent = countAgent + 1;
		}
		for(Agent agent : listAgentEnemy) {
			if(agent.getLiving()) countEnemy = countEnemy + 1;
		}	
		if(pViewEnd == null) {
			this.pViewEnd = new ViewEnd(
					result, 
					listAgentEnemy.size() - countEnemy, 
					countEnemy,
					listAgentAlly.size() - countAgent, 
					countAgent, 
					this);
		}else {
			this.pViewEnd.setInformation(					
					result, 
					listAgentEnemy.size() - countEnemy, 
					countEnemy,
					listAgentAlly.size() - countAgent, 
					countAgent);
		}
	}
	
	/**
	 * Lance la vue viewStart
	 */
	@SuppressWarnings("unused")
	public void lunchViewStart() {
		ViewStart viewStart = new ViewStart(this);
	}
	
	/**
	 * Mets à jour l'action que l'utilisateur souhaite effectuer avec le bomberman
	 * @param action : Action que l'utilisateur a saisie au clavier
	 */
	public void updateActionBomberman(AgentAction action) {
		this.pGame.updateActionUser(action);
	}
}
