package network.client;

import models.BombermanGame;
import utils.AgentAction;

import java.awt.event.KeyEvent;

public class ControllerClient {

    private AgentAction action;
    private AgentAction oldAction;

    public ControllerClient(){
        this.action = AgentAction.STOP;
    }

    /**
     * Mets à jour l'action que l'utilisateur souhaite effectuer avec le bomberman
     * @param pAction : Action que l'utilisateur a saisie au clavier
     */
    public void setAction(AgentAction pAction) {
        this.oldAction = this.action;
        this.action = pAction;
    }

    public AgentAction getAction() {
        return action;
    }

    public AgentAction getOldAction() {
        return oldAction;
    }

    public void setOldAction(AgentAction oldAction) {
        this.oldAction = oldAction;
    }
}
