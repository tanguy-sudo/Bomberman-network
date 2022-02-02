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
 */
public class BombermanStrategy extends Strategy{

	@Override
	public AgentAction generateAction(Agent agent, BombermanGame game) {
		Agent enemy = searchEnemy(agent, game, 3);
		if(searchEnemy(agent, game, 4) == null || agent.getpInvincibleFor() > 0) {
			return runToTheEnemy(agent, game);
		} else if(enemy != null) {
			if(!game.BombHere(agent.getpInfoAgent().getX(), agent.getpInfoAgent().getY()) && enemyIsTooClose(agent, game) == null && agent.getpSkullFor() <= 0) return AgentAction.PUT_BOMB;
			else return nextAction(agent, game);
		} else if(searchBreakable_walls(agent, game.getpBreakable_walls(), game)) {
			if(!game.BombHere(agent.getpInfoAgent().getX(), agent.getpInfoAgent().getY()) && agent.getpSkullFor() <= 0) return AgentAction.PUT_BOMB;
			else return randomAction();
		} else return randomAction();
	}
	
	private Agent searchEnemy(Agent agent, BombermanGame game, int rayon) {
		for(Agent enemy : game.getpListBombermanEnemy()) {
			if(enemy.getpLiving()) {
				if(enemy.getpInfoAgent().getX() <= agent.getpInfoAgent().getX() + rayon
						&& enemy.getpInfoAgent().getY() >= agent.getpInfoAgent().getY() - rayon
						&& enemy.getpInfoAgent().getX() >= agent.getpInfoAgent().getX() - rayon
						&& enemy.getpInfoAgent().getY() <= agent.getpInfoAgent().getY() + rayon) {
					return enemy;
				}
			}
		}
		return null;
	}
	
	private boolean searchBreakable_walls(Agent agent, boolean[][] wall, BombermanGame game) {
		int beginx = agent.getpInfoAgent().getX() - 1;
		int endx = agent.getpInfoAgent().getX() + 1;
		if(beginx < 0) beginx = agent.getpInfoAgent().getX();
		if(endx > game.getpInputMap().getSize_x()) endx = agent.getpInfoAgent().getX();
		int beginy = agent.getpInfoAgent().getY() - 1;
		int endy = agent.getpInfoAgent().getY() + 1;
		if(beginy < 0) beginy = agent.getpInfoAgent().getY();
		if(endy > game.getpInputMap().getSize_y()) endy = agent.getpInfoAgent().getY();
		
		for(int x = beginx; x <= endx ; x++) {
			if(wall[x][beginy]) return true;
			if(wall[x][endy]) return true;
		}
		if(wall[beginx][agent.getpInfoAgent().getY()]) return true;
		else if(wall[endx][agent.getpInfoAgent().getY()]) return true;
		else return false;
		
	}
	
	private AgentAction nextAction(Agent agent, BombermanGame game) {
		enemyIsTooClose(agent, game);
		// on regarde les ennemies dans un rayon de 2 puis de 3
		for(int i = 2 ; i <= 3 ; i++) {
			for(Agent enemy : game.getpListBombermanEnemy()) {
				if(enemy.getpLiving()) {
					if(enemy.getpInfoAgent().getX() <= agent.getpInfoAgent().getX() + i
							&& enemy.getpInfoAgent().getY() >= agent.getpInfoAgent().getY() - i
							&& enemy.getpInfoAgent().getX() >= agent.getpInfoAgent().getX() - i
							&& enemy.getpInfoAgent().getY() <= agent.getpInfoAgent().getY() + i) {
						if (enemy.getpInfoAgent().getX() < agent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
						else if (enemy.getpInfoAgent().getY() > agent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
						else if (enemy.getpInfoAgent().getX() > agent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
						else if (enemy.getpInfoAgent().getY() < agent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
					}
				}
			}		
		}
		
		return AgentAction.STOP;
	}
	
	private AgentAction enemyIsTooClose(Agent agent, BombermanGame game) {
		for(Agent enemy : game.getpListBombermanEnemy()) {
			if(enemy.getpLiving()) {
				if(enemy.getpInfoAgent().getX() <= agent.getpInfoAgent().getX() + 1
						&& enemy.getpInfoAgent().getY() >= agent.getpInfoAgent().getY() - 1
						&& enemy.getpInfoAgent().getX() >= agent.getpInfoAgent().getX() - 1
						&& enemy.getpInfoAgent().getY() <= agent.getpInfoAgent().getY() + 1) {
					if (enemy.getpInfoAgent().getX() < agent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
					else if (enemy.getpInfoAgent().getY() > agent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
					else if (enemy.getpInfoAgent().getX() > agent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
					else if (enemy.getpInfoAgent().getY() < agent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
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
		for(Agent enemyAgent : game.getpListBombermanEnemy()) {
			if(enemyAgent.getpLiving()) {
				if (agent.getpInfoAgent().getX() > enemyAgent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_LEFT)) return AgentAction.MOVE_LEFT;
				else if (agent.getpInfoAgent().getX() < enemyAgent.getpInfoAgent().getX() && game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return AgentAction.MOVE_RIGHT;
				else if (agent.getpInfoAgent().getY() > enemyAgent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_UP)) return AgentAction.MOVE_UP;
				else if (agent.getpInfoAgent().getY() < enemyAgent.getpInfoAgent().getY() && game.isLegalMove(agent, AgentAction.MOVE_DOWN)) return AgentAction.MOVE_DOWN;
				else return randomAction();			
			}
		}
		return AgentAction.STOP;
	}
}
