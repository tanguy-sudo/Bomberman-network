package models.agent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

	@JsonCreator
	public BombermanAgent(){}

	
	/**
	 * @return Une action
	 */
	public AgentAction getpActionUtilisateur() {
		return this.pActionUtilisateur;
	}
	
	/**
	 * Mets à jour l'action de l'utilisateur
	 * @param action
	 */
	public void setpActionUtilisateur(AgentAction action) {
		this.pActionUtilisateur = action;
	}
}
