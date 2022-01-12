package models.agent.etat;

import models.agent.Agent;
/**
 * Classe indiquant l'état skull
 * @author tanguy
 *
 */
public class EtatSkull implements EtatAgent{
	
	private Agent pAgent;
	
	public EtatSkull(Agent agent) {
		this.pAgent = agent;
		this.pAgent.setSkullFor(4);
		this.pAgent.getAgent().setSick(true);
		
	}

	@Override
	public void invincible() {
		this.pAgent.getAgent().setSick(false);
		this.pAgent.setEtat(new EtatInvincible(this.pAgent));	
	}

	@Override
	public void skull() {	
		this.pAgent.setSkullFor(4);
	}

	@Override
	public void withoutEffects() {
		this.pAgent.setEtat(new EtatWithoutEffects(this.pAgent));	
	}

}
