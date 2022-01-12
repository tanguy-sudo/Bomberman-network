package models.strategy;

import java.util.Random;

import models.BombermanGame;
import models.agent.Agent;
import utils.AgentAction;
/**
 * Stratégie aléatoire
 * @author tanguy
 *
 */
public class SimpleStrategy extends Strategy{

	public AgentAction generateAction(Agent agent, BombermanGame game) {
		if(agent.getAgent().getType() != 'B') {
			Random r = new Random();
			switch(r.nextInt(5)) {
				case 0:
					return AgentAction.MOVE_DOWN;
				case 1:
					return AgentAction.MOVE_LEFT;
				case 2:
					return AgentAction.MOVE_RIGHT;
				case 3:
					return AgentAction.MOVE_UP;
				case 4:
					return AgentAction.STOP;
			}	
		}else {
			Random r = new Random();
			switch(r.nextInt(6)) {
				case 0:
					return AgentAction.MOVE_DOWN;
				case 1:
					return AgentAction.MOVE_LEFT;
				case 2:
					return AgentAction.PUT_BOMB;
				case 3:
					return AgentAction.MOVE_UP;
				case 4:
					return AgentAction.STOP;
				case 5:
					return AgentAction.MOVE_RIGHT;
			}
		}
		return null;
	}
}
