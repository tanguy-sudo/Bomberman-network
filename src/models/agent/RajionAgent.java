package models.agent;

import models.strategy.Strategy;
import utils.InfoAgent;
/**
 * Classe représentant un ennemi raijon (il attaque ces propres s'allier
 * @author tanguy
 *
 */
public class RajionAgent extends Agent{

	public RajionAgent(InfoAgent infoAgent, Strategy strategy) {
		super(infoAgent, strategy);
	}
}
