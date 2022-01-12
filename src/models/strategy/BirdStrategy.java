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
		for(Agent bombermanAgent : game.getListAgentBomberman()) {
			if(bombermanAgent.getLiving()) {
				if(bombermanAgent.getAgent().getX() <= agent.getAgent().getX() + 3 
						&& bombermanAgent.getAgent().getY() >= agent.getAgent().getY() - 3 
						&& bombermanAgent.getAgent().getX() >= agent.getAgent().getX() - 3
						&& bombermanAgent.getAgent().getY() <= agent.getAgent().getY() + 3) {
					if (agent.getAgent().getX() > bombermanAgent.getAgent().getX()) return AgentAction.MOVE_LEFT;
					if (agent.getAgent().getX() < bombermanAgent.getAgent().getX()) return AgentAction.MOVE_RIGHT;
					if (agent.getAgent().getY() > bombermanAgent.getAgent().getY()) return AgentAction.MOVE_UP;
					if (agent.getAgent().getY() < bombermanAgent.getAgent().getY()) return AgentAction.MOVE_DOWN;
					break;					
				}
			}
		}
		return AgentAction.STOP;
	}

}
