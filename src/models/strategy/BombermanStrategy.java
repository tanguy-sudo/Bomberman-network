package models.strategy;

import java.util.Random;
import models.BombermanGame;
import models.agent.Agent;
import utils.AgentAction;
/**
 * le Bomberman recule quand l'ennemi est trop proche, pose des bombes s'il est proche d'un ennemi ou d'un mur cassable
 * Si le bomberman est invincible il fonce vers l'ennemie
 * Si le Bomberman n'a personne dans un rayon de 5 il fonc vers le premier ennemi de la liste
 * @author tanguy
 *
 */
public class BombermanStrategy extends Strategy{

	@Override
	public AgentAction generateAction(Agent agent, BombermanGame game) {
		Agent enemy = searchEnemy(agent, game, 3);
		if(searchEnemy(agent, game, 4) == null || agent.getInvincibleFor() > 0) {
			return runToTheEnemy(agent, game);
		} else if(enemy != null) {
			if(!game.BombHere(agent.getAgent().getX(), agent.getAgent().getY()) && enemyIsTooClose(agent, game) == null && agent.getSkullFor() <= 0) return AgentAction.PUT_BOMB;
			else return nextAction(agent, game);
		} else if(searchBreakable_walls(agent, game.getBreakable_walls(), game)) {
			if(!game.BombHere(agent.getAgent().getX(), agent.getAgent().getY()) && agent.getSkullFor() <= 0) return AgentAction.PUT_BOMB;
			else return randomAction();
		} else return randomAction();
	}
	
	private Agent searchEnemy(Agent agent, BombermanGame game, int rayon) {
		for(Agent enemy : game.getListAgentEnemy()) {
			if(enemy.getLiving()) {
				if(enemy.getAgent().getX() <= agent.getAgent().getX() + rayon 
						&& enemy.getAgent().getY() >= agent.getAgent().getY() - rayon 
						&& enemy.getAgent().getX() >= agent.getAgent().getX() - rayon
						&& enemy.getAgent().getY() <= agent.getAgent().getY() + rayon) {
					return enemy;
				}
			}
		}
		return null;
	}
	
	private boolean searchBreakable_walls(Agent agent, boolean[][] wall, BombermanGame game) {
		int beginx = agent.getAgent().getX() - 1;
		int endx = agent.getAgent().getX() + 1;
		if(beginx < 0) beginx = agent.getAgent().getX();
		if(endx > game.getInputMap().getSizeX()) endx = agent.getAgent().getX();
		int beginy = agent.getAgent().getY() - 1;
		int endy = agent.getAgent().getY() + 1;
		if(beginy < 0) beginy = agent.getAgent().getY();
		if(endy > game.getInputMap().getSizeY()) endy = agent.getAgent().getY();
		
		for(int x = beginx; x <= endx ; x++) {
			if(wall[x][beginy]) return true;
			if(wall[x][endy]) return true;
		}
		if(wall[beginx][agent.getAgent().getY()]) return true;
		else if(wall[endx][agent.getAgent().getY()]) return true;
		else return false;
		
	}
	
	private AgentAction nextAction(Agent agent, BombermanGame game) {
		enemyIsTooClose(agent, game);
		// on regarde les ennemies dans un rayon de 2 puis de 3
		for(int i = 2 ; i <= 3 ; i++) {
			for(Agent enemy : game.getListAgentEnemy()) {
				if(enemy.getLiving()) {
					if(enemy.getAgent().getX() <= agent.getAgent().getX() + i 
							&& enemy.getAgent().getY() >= agent.getAgent().getY() - i 
							&& enemy.getAgent().getX() >= agent.getAgent().getX() - i
							&& enemy.getAgent().getY() <= agent.getAgent().getY() + i) {
						if (enemy.getAgent().getX() < agent.getAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
						else if (enemy.getAgent().getY() > agent.getAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
						else if (enemy.getAgent().getX() > agent.getAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
						else if (enemy.getAgent().getY() < agent.getAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
					}
				}
			}		
		}
		
		return AgentAction.STOP;
	}
	
	private AgentAction enemyIsTooClose(Agent agent, BombermanGame game) {
		for(Agent enemy : game.getListAgentEnemy()) {
			if(enemy.getLiving()) {
				if(enemy.getAgent().getX() <= agent.getAgent().getX() + 1 
						&& enemy.getAgent().getY() >= agent.getAgent().getY() - 1 
						&& enemy.getAgent().getX() >= agent.getAgent().getX() - 1
						&& enemy.getAgent().getY() <= agent.getAgent().getY() + 1) {
					if (enemy.getAgent().getX() < agent.getAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
					else if (enemy.getAgent().getY() > agent.getAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
					else if (enemy.getAgent().getX() > agent.getAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
					else if (enemy.getAgent().getY() < agent.getAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
				}
			}
		}
		return null;
	}
	
	private AgentAction randomAction() {
		Random r = new Random();
		switch(r.nextInt(4)) {
			case 0:
				return AgentAction.MOVE_DOWN;
			case 1:
				return AgentAction.MOVE_LEFT;
			case 2:
				return AgentAction.MOVE_RIGHT;
			case 3:
				return AgentAction.MOVE_UP;
		}
		return AgentAction.STOP;
	}
	
	private AgentAction runToTheEnemy(Agent agent, BombermanGame game) {
		for(Agent enemyAgent : game.getListAgentEnemy()) {
			if(enemyAgent.getLiving()) {
				if (agent.getAgent().getX() > enemyAgent.getAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
				else if (agent.getAgent().getX() < enemyAgent.getAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
				else if (agent.getAgent().getY() > enemyAgent.getAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
				else if (agent.getAgent().getY() < enemyAgent.getAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
				else return randomAction();			
			}
		}
		return AgentAction.STOP;
	}
}
