package network.client;

import utils.AgentAction;

/**
 * @author tanguy, guillaume
 * stocke l'action de l'utilisateur
 */
public class ControllerClient {
    private AgentAction action;
    private boolean restart;

    public ControllerClient(){
        this.action = AgentAction.STOP;
    }

    /**
     * Mets à jour l'action que l'utilisateur souhaite effectuer avec le bomberman
     * @param pAction : Action que l'utilisateur a saisie au clavier
     */
    public void setAction(AgentAction pAction) {this.action = pAction;}
    public AgentAction getAction() {
        return action;
    }

    public boolean isRestart() {return restart;}
    public void setRestart(boolean restart) {this.restart = restart;}
}
