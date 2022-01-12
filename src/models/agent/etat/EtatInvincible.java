package models.agent.etat;

import models.agent.Agent;
/**
 * Classe indiquant l'état invincible
 * @author tanguy
 *
 */
public class EtatInvincible implements EtatAgent{
	
	private Agent pAgent;
	
	public EtatInvincible(Agent agent) {
		this.pAgent = agent;
		this.pAgent.setInvincibleFor(5);
		this.pAgent.setSkullFor(0);
		this.pAgent.getAgent().setInvincible(true);
	}

	@Override
	public void invincible() {
		this.pAgent.setInvincibleFor(5);
	}

	@Override
	public void skull() {
	}

	@Override
	public void withoutEffects() {
		this.pAgent.setEtat(new EtatWithoutEffects(this.pAgent));	
	}

}
