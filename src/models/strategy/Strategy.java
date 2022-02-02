package models.strategy;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import models.BombermanGame;
import models.agent.Agent;
import utils.AgentAction;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
		@JsonSubTypes.Type(value = BasicEnemyStrategy.class, name = "BasicEnemyStrategy"),
		@JsonSubTypes.Type(value = BirdStrategy.class, name = "BirdStrategy"),
		@JsonSubTypes.Type(value = BombermanManualStrategy.class, name = "BombermanManualStrategy"),
		@JsonSubTypes.Type(value = BombermanStrategy.class, name = "BombermanStrategy"),
		@JsonSubTypes.Type(value = RaijonStrategy.class, name = "RaijonStrategy"),
		@JsonSubTypes.Type(value = SimpleStrategy.class, name = "SimpleStrategy")
})
/**
 * Interface des stratégies
 * @author tanguy
 */

public abstract class Strategy {
	public abstract AgentAction generateAction(Agent agent, BombermanGame game);
	
	public boolean isBlockOff(Agent agent, BombermanGame game) {
		if(game.isLegalMove(agent, AgentAction.MOVE_DOWN)
				|| game.isLegalMove(agent, AgentAction.MOVE_LEFT)
				|| game.isLegalMove(agent, AgentAction.MOVE_UP)
				|| game.isLegalMove(agent, AgentAction.MOVE_RIGHT)) return false;
		else return true;
	}
}
