package models.agent;

import models.strategy.BombermanManualStrategy;
import models.strategy.BombermanStrategy;
import models.strategy.SimpleStrategy;
import utils.InfoAgent;
/**
 * Fabrique un Bomberman et applique une stratégie en fonction du niveau de si l'utilisateur joue manuellement
 * @author tanguy
 *
 */
public class FabriqueBomberman implements AbstractFactory{

	public Agent createAgent(InfoAgent infoAgent, int niveau, boolean manual) {
		if(manual) {
			return new BombermanAgent(infoAgent, new BombermanManualStrategy());	
		}else {
			if(niveau == 1) {
				return new BombermanAgent(infoAgent, new SimpleStrategy());		
			} else if(niveau == 2) {
				return new BombermanAgent(infoAgent, new BombermanStrategy());	
			}
		}
		// On tombe jamais dans ce cas
		return null;
	}
}
