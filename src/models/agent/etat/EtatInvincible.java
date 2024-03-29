package models.agent.etat;

import models.agent.Agent;

/**
 * Classe indiquant l'état invincible
 * @author tanguy
 */
public class EtatInvincible implements EtatAgent{
	private Agent pAgent;
	
	public EtatInvincible(Agent agent) {
		this.pAgent = agent;
		this.pAgent.setpInvincibleFor(5);
		this.pAgent.setpSkullFor(0);
		this.pAgent.getpInfoAgent().setInvincible(true);
	}

	@Override
	public void invincible() {
		this.pAgent.setpInvincibleFor(5);
	}

	@Override
	public void skull() {}

	@Override
	public void withoutEffects() {
		this.pAgent.setpEtat(new EtatWithoutEffects(this.pAgent));
	}

}
