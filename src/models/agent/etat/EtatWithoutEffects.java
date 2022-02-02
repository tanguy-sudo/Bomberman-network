package models.agent.etat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.agent.Agent;
@JsonIgnoreProperties(value = { "pAgent" })
/**
 * Classe indiquant l'état sans effet
 * @author tanguy
 */
public class EtatWithoutEffects implements EtatAgent{
	private Agent pAgent;
	
	public EtatWithoutEffects(Agent agent) {
		this.pAgent = agent;
		this.pAgent.getpInfoAgent().setInvincible(false);
		this.pAgent.getpInfoAgent().setSick(false);
	}

	@JsonCreator
	public EtatWithoutEffects(){}

	@Override
	public void invincible() {
		this.pAgent.setpEtat(new EtatInvincible(this.pAgent));
	}

	@Override
	public void skull() {
		this.pAgent.setpEtat(new EtatSkull(this.pAgent));
	}

	@Override
	public void withoutEffects() {}

}
