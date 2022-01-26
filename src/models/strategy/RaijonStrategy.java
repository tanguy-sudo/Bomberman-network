package models.strategy;

import java.util.Random;
import models.BombermanGame;
import models.agent.Agent;
import utils.AgentAction;
/**
 * 
 * @author tanguy
 * S'il est invincible fonce vers le bomberman
 * Il peut manger ses alliées
 */
public class RaijonStrategy extends Strategy{
	@Override
	public AgentAction generateAction(Agent agent, BombermanGame game) {
		if(agent.getpInvincibleFor() >= 0) {
			for(Agent bombermanAgent : game.getpListBombermanAgent()) {
				if(bombermanAgent.getpLiving()) {
					if (agent.getpInfoAgent().getX() > bombermanAgent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
					else if (agent.getpInfoAgent().getX() < bombermanAgent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
					else if (agent.getpInfoAgent().getY() > bombermanAgent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
					else if (agent.getpInfoAgent().getY() < bombermanAgent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
					else return AgentAction.STOP;					
				}
			}			
		}else {
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
		}
		return AgentAction.STOP;
	}
}
