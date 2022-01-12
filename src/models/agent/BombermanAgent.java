package models.agent;

import models.strategy.Strategy;
import utils.AgentAction;
import utils.InfoAgent;
/**
 * Classe représentant un bomberman
 * @author tanguy
 *
 */
public class BombermanAgent extends Agent{
	
	private AgentAction pActionUtilisateur;
	
	public BombermanAgent(InfoAgent infoAgent, Strategy strategy) {
		super(infoAgent, strategy);
		this.pActionUtilisateur = AgentAction.STOP;
	}
	
	/**
	 * 
	 * @return Une action
	 */
	public AgentAction getAtion() {
		return this.pActionUtilisateur;
	}
	
	/**
	 * Mets à jour l'action de l'utilisateur
	 * @param action
	 */
	public void setAtion(AgentAction action) {
		this.pActionUtilisateur = action;
	}
}
