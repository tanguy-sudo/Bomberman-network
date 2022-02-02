package models.agent;

import com.fasterxml.jackson.annotation.JsonCreator;
import models.strategy.Strategy;
import utils.InfoAgent;
/**
 * Classe représentant un ennemi basique
 * @author tanguy
 */
public class BasicEnemyAgent extends Agent{

	@JsonCreator
	public BasicEnemyAgent(){}

	public BasicEnemyAgent(InfoAgent infoAgent, Strategy strategy) {
		super(infoAgent, strategy);
	}
}
