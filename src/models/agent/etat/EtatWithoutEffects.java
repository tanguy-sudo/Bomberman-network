package models.agent.etat;

import models.agent.Agent;
/**
 * Classe indiquant l'état sans effet
 * @author tanguy
 *
 */
public class EtatWithoutEffects implements EtatAgent{
	
	private Agent pAgent;
	
	public EtatWithoutEffects(Agent agent) {
		this.pAgent = agent;
		this.pAgent.getAgent().setInvincible(false);
		this.pAgent.getAgent().setSick(false);
	}

	@Override
	public void invincible() {
		this.pAgent.setEtat(new EtatInvincible(this.pAgent));
	}

	@Override
	public void skull() {
		this.pAgent.setEtat(new EtatSkull(this.pAgent));	
	}

	@Override
	public void withoutEffects() {	
	}

}
