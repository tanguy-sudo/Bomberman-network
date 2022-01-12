package models.strategy;

import models.BombermanGame;
import models.agent.Agent;
import models.agent.BombermanAgent;
import utils.AgentAction;
/**
 * Mode manuel, l'utilisateur bouge avec les claviers la Bomberman
 * @author tanguy
 *
 */
public class BombermanManualStrategy extends Strategy{

	@Override
	public AgentAction generateAction(Agent agent, BombermanGame game) {
		return ((BombermanAgent)agent).getAtion(); 
	}

}
