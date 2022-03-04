package network.client;

import org.json.JSONObject;
import utils.AgentAction;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * @author tanguy, guillaume
 * stocke l'action de l'utilisateur
 */
public class ControllerClient {
    private AgentAction action;
    private boolean exit;
    private ClientRunnable clientRunnable;
    private boolean start;

    public ControllerClient(ClientRunnable client) {
        this.action = AgentAction.STOP;
        this.exit = false;
        this.clientRunnable = client;
        this.start = false;
    }

    /**
     * Mets à jour l'action que l'utilisateur souhaite effectuer avec le bomberman
     *
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

    public boolean isStart() { return start; }
    public void setStart(boolean start) { this.start = start; }

    public void login(String username, String password, JFrame window ) {
        try {
            HttpClient client = HttpClient.newHttpClient();

            JSONObject body = new JSONObject();
            body.put("username", username);
            body.put("password", password);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://192.168.1.70:8080/BombermanWeb/api/user"))
                    .POST(HttpRequest.BodyPublishers.ofString(String.valueOf(body)))
                    .header("Accept", "application.json")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JSONObject json = new JSONObject(response.body());
            Integer status = json.getInt("status");
            if (status == 201) {
                System.out.println("success");
                this.start = true;
                this.clientRunnable.lunch();
                window.dispose();
            } else if (status == 404) {
                System.out.println("error");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
