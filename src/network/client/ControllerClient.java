package network.client;

import utils.AgentAction;
import javax.swing.*;

/**
 * @author tanguy, guillaume
 * stocke l'action de l'utilisateur
 */
public class ControllerClient {
    private AgentAction action;
    private boolean exit;
    private ClientRunnable clientRunnable;
    private String username;
    private String password;

    public ControllerClient(ClientRunnable client) {
        this.action = AgentAction.STOP;
        this.exit = false;
        this.clientRunnable = client;
        this.username = "";
        this.password = "";
    }

    /**
     * Mets à jour l'action que l'utilisateur souhaite effectuer avec le bomberman
     * @param pAction : Action que l'utilisateur a saisie au clavier
     */
    public void setAction(AgentAction pAction) {
        this.action = pAction;
    }
    public AgentAction getAction() {
        return action;
    }

    public boolean isExit() {
        return this.exit;
    }
    public void setExit(boolean restart) {
        this.exit = restart;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public void login(String username, String password, JFrame window) {
        this.username = username;
        this.password = password;
        this.clientRunnable.setSendLoginPassword(true);
        new Thread(this.clientRunnable).start();
    }
}
