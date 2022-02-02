package models.agent;

import com.fasterxml.jackson.annotation.JsonCreator;
import models.strategy.Strategy;
import utils.InfoAgent;
/**
 * Classe représentant un ennemi volant
 * @author tanguy
 */
public class BirdAgent extends Agent{
	@JsonCreator
	public BirdAgent(){}

	public BirdAgent(InfoAgent infoAgent, Strategy strategy) {
		super(infoAgent, strategy);
	}
}
