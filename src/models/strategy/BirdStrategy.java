package models.strategy;
import models.BombermanGame;
import models.agent.Agent;
import utils.AgentAction;
/**
 * Le Bird fonce vers l'ennemie quand il rentre dans sa zone 
 * @author tanguy
 *
 */
public class BirdStrategy extends Strategy{

	@Override
	public AgentAction generateAction(Agent agent, BombermanGame game) {
		for(Agent bombermanAgent : game.getpListBombermanAgent()) {
			if(bombermanAgent.getpLiving()) {
				if(bombermanAgent.getpInfoAgent().getX() <= agent.getpInfoAgent().getX() + 3
						&& bombermanAgent.getpInfoAgent().getY() >= agent.getpInfoAgent().getY() - 3
						&& bombermanAgent.getpInfoAgent().getX() >= agent.getpInfoAgent().getX() - 3
						&& bombermanAgent.getpInfoAgent().getY() <= agent.getpInfoAgent().getY() + 3) {
					if (agent.getpInfoAgent().getX() > bombermanAgent.getpInfoAgent().getX()) return AgentAction.MOVE_LEFT;
					if (agent.getpInfoAgent().getX() < bombermanAgent.getpInfoAgent().getX()) return AgentAction.MOVE_RIGHT;
					if (agent.getpInfoAgent().getY() > bombermanAgent.getpInfoAgent().getY()) return AgentAction.MOVE_UP;
					if (agent.getpInfoAgent().getY() < bombermanAgent.getpInfoAgent().getY()) return AgentAction.MOVE_DOWN;
					break;					
				}
			}
		}
		return AgentAction.STOP;
	}

}
