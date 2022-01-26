package models.strategy;
import models.BombermanGame;
import models.agent.Agent;
import utils.AgentAction;
/**
 * le BasicEnemy fonce vers la cible en permanence
 * @author tanguy
 *
 */
public class BasicEnemyStrategy extends Strategy{

	@Override
	public AgentAction generateAction(Agent agent, BombermanGame game) {
		for(Agent bombermanAgent : game.getpListBombermanAgent()) {
			if(bombermanAgent.getpLiving()) {
				if (agent.getpInfoAgent().getX() > bombermanAgent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
				else if (agent.getpInfoAgent().getX() < bombermanAgent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
				else if (agent.getpInfoAgent().getY() > bombermanAgent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
				else if (agent.getpInfoAgent().getY() < bombermanAgent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
				else return AgentAction.STOP;				
			}
		}
		return AgentAction.STOP;
	}

}
