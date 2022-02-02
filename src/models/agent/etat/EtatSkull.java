package models.agent.etat;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import models.agent.Agent;
@JsonIgnoreProperties(value = { "pAgent" })
/**
 * Classe indiquant l'état skull
 * @author tanguy
 */
public class EtatSkull implements EtatAgent{
	private Agent pAgent;
	
	public EtatSkull(Agent agent) {
		this.pAgent = agent;
		this.pAgent.setpSkullFor(4);
		this.pAgent.getpInfoAgent().setSick(true);
	}

	@JsonCreator
	public EtatSkull(){}

	@Override
	public void invincible() {
		this.pAgent.getpInfoAgent().setSick(false);
		this.pAgent.setpEtat(new EtatInvincible(this.pAgent));
	}

	@Override
	public void skull() {	
		this.pAgent.setpSkullFor(4);
	}

	@Override
	public void withoutEffects() {
		this.pAgent.setpEtat(new EtatWithoutEffects(this.pAgent));
	}

}
