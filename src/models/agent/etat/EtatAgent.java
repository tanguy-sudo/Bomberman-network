package models.agent.etat;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
		@JsonSubTypes.Type(value = EtatSkull.class, name = "EtatSkull"),
		@JsonSubTypes.Type(value = EtatWithoutEffects.class, name = "EtatWithoutEffects"),
		@JsonSubTypes.Type(value = EtatInvincible.class, name = "EtatInvincible")
})
/**
 * interface qui indique l'état d'un agent
 * @author tanguy
 *
 */
public interface EtatAgent {
	public void invincible();
	public void skull();
	public void withoutEffects();
}
