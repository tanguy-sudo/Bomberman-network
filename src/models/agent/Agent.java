package models.agent;

import models.strategy.Strategy;
import models.agent.etat.EtatAgent;
import models.agent.etat.EtatWithoutEffects;
import utils.AgentAction;
import utils.InfoAgent;
/**
 * Classe qui représente un agent
 * @author tanguy
 *
 */
public abstract class Agent{
	
	protected InfoAgent pInfoAgent;
	private EtatAgent pEtat;
	private int pRange;
	private int pInvincibleFor;
	private int pSkullFor;
	private Strategy pStrategy;
	private boolean pLiving;
	
	/**
	 * Créer un agent
	 * @param infoAgent : Informations sur l'agent
	 * @param strategy: Stratégie sur l'agent
	 */
	public Agent(InfoAgent infoAgent, Strategy strategy) {
		this.pInfoAgent = infoAgent;
		this.pEtat = new EtatWithoutEffects(this);
		this.pRange = 2;
		this.pInvincibleFor = 0;
		this.pSkullFor = 0;
		this.pStrategy = strategy;
		this.pLiving = true;
	}
	
	/**
	 * Bouge un agent
	 * @param action : Action que l'agent fait
	 */
	public void setMove(AgentAction action) {		
		switch (action) {
		case MOVE_DOWN:
			this.pInfoAgent.setY(this.pInfoAgent.getY() + 1);
			break;
		case MOVE_LEFT:
			this.pInfoAgent.setX(this.pInfoAgent.getX() - 1);
			break;
		case MOVE_RIGHT:
			this.pInfoAgent.setX(this.pInfoAgent.getX() + 1);
			break;
		case MOVE_UP:
			this.pInfoAgent.setY(this.pInfoAgent.getY() - 1);
			break;
		default:
			break;
		}
		
		this.pInfoAgent.setAgentAction(action);
	}
	
	/**
	 * 
	 * @return L'attribut pInfoAgent
	 */
	public InfoAgent getAgent() {
		return this.pInfoAgent;
	}	
	/**
	 * Mets à jour l'attribut etat
	 * @param etat : état de l'agent
	 */
	public void setEtat(EtatAgent etat) {
		this.pEtat = etat;
	}
	/**
	 * 
	 * @return L'attribut pEtat
	 */
	public EtatAgent getEtat() {
		return this.pEtat;
	}
	/**
	 * Mets à jour l'attribut pRange
	 * @param range
	 */
	public void setRange(int range) {
		this.pRange = range;
	}
	/**
	 * 
	 * @return L'attribut pRange
	 */
	public int getRange() {
		return this.pRange;
	}
	/**
	 * 
	 * @return  L'attribut pInvincibleFor
	 */
	public int getInvincibleFor() {
		return this.pInvincibleFor;
	}	
	/**
	 *  Mets à jour l'attribut pInvincibleFor
	 * @param nbTurn
	 */
	public void setInvincibleFor(int nbTurn) {
		this.pInvincibleFor = nbTurn;
	}
	/**
	 *  L'attribut pSkullFor
	 * @return
	 */
	public int getSkullFor() {
		return this.pSkullFor;
	}	
	/**
	 * Mets à jour l'attribut pSkullFor
	 * @param nbTurn
	 */
	public void setSkullFor(int nbTurn) {
		this.pSkullFor = nbTurn;
	}
	/**
	 * 
	 * @return L'attribut pStrategy
	 */
	public Strategy getStrategy() {
		return this.pStrategy;
	}
	/**
	 * Mets à jour l'attribut pLiving
	 * @param living
	 */
	public void setLiving(boolean living) {
		this.pLiving = living;
	}
	/**
	 *  
	 * @return L'attribut pSkullFor
	 */
	public boolean getLiving() {
		return this.pLiving;
	}
}
