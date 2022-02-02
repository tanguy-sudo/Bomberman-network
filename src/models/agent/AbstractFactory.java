package models.agent;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import utils.InfoAgent;
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
		@JsonSubTypes.Type(value = FabriqueBomberman.class, name = "FabriqueBomberman"),
		@JsonSubTypes.Type(value = FabriqueEnemy.class, name = "FabriqueEnemy")
})
/**
 * Fabrique abstraite
 * @author tanguy
 */
public interface AbstractFactory {
	public Agent createAgent(InfoAgent infoAgent, int niveau, boolean manual);
}
