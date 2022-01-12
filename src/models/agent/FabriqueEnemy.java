package models.agent;

import models.strategy.BasicEnemyStrategy;
import models.strategy.BirdStrategy;
import models.strategy.RaijonStrategy;
import models.strategy.SimpleStrategy;
import utils.InfoAgent;
/**
 * Fabrique un ennemi et applique une stratégie en fonction du niveau
 * @author tanguy
 *
 */
public class FabriqueEnemy implements AbstractFactory{

	@Override
	public Agent createAgent(InfoAgent infoAgent, int niveau, boolean manual) {
		if(niveau == 1) {
			switch(infoAgent.getType()) {
				case 'R':
					return new RajionAgent(infoAgent, new SimpleStrategy());
				case 'V':
					return new BirdAgent(infoAgent, new SimpleStrategy());
				case 'E':
					return new BasicEnemyAgent(infoAgent, new SimpleStrategy());
			}
		} else if(niveau == 2) {
			switch(infoAgent.getType()) {
				case 'R':
					return new RajionAgent(infoAgent, new RaijonStrategy());
				case 'V':
					return new BirdAgent(infoAgent, new BirdStrategy());
				case 'E':
					return new BasicEnemyAgent(infoAgent, new BasicEnemyStrategy());
			}
		}
		// on tombe jamais dans ce cas
		return null;
	}
}