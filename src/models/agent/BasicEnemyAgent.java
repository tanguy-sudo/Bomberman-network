package models.agent;

import models.strategy.Strategy;
import utils.InfoAgent;
/**
 * Classe repr�sentant un ennemi basique
 * @author tanguy
 *
 */
public class BasicEnemyAgent extends Agent{

	public BasicEnemyAgent(InfoAgent infoAgent, Strategy strategy) {
		super(infoAgent, strategy);
	}
}
