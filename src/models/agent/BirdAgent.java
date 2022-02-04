package models.agent;

import models.strategy.Strategy;
import utils.InfoAgent;
/**
 * Classe représentant un ennemi volant
 * @author tanguy
 */
public class BirdAgent extends Agent{

	public BirdAgent(InfoAgent infoAgent, Strategy strategy) {
		super(infoAgent, strategy);
	}
}
