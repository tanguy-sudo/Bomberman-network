package network.server;

import models.BombermanGame;
import models.Game;
import models.strategy.BombermanManualStrategy;
import models.strategy.BombermanStrategy;
import org.json.JSONObject;
import utils.AgentAction;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author tanguy, guillaume
 * Chaque client a un thread
 * ServerThread gère l'envoie d'un objet JSON sous la forme d'une string a son client;
 */
public class ServerThread extends Thread {
    private static int compteur = 0;
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private BombermanGame bombermanGame;
    private int playerNumber;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads, Game bombermanGame) {
        this.socket = socket;
        this.threadList = threads;
        this.bombermanGame = (BombermanGame) bombermanGame;
        this.playerNumber = ServerThread.compteur;
        this.bombermanGame.getpListBombermanAgent().get(this.playerNumber).setpStrategy(new BombermanManualStrategy());
        ServerThread.compteur++;
    }

    /**
     * crée une entrée et une sortie sur un socket, pour lire et écrire dessus
     * li l'action que le client envoie et lui renvoie le jeu modifié
     */
    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(),true);

            JSONObject obj = new JSONObject();
            obj.put("size_x", bombermanGame.getpInputMap().getSize_x());
            obj.put("size_y", bombermanGame.getpInputMap().getSize_y());
            obj.put("listInfoAgents", bombermanGame.fusionListAgent());
            obj.put("walls", bombermanGame.getpInputMap().getWalls());
            obj.put("breakablewalls", bombermanGame.getpBreakable_walls());
            output.println(obj);

            String outputString;
            AgentAction action;
            while(true) {

                outputString = input.readLine();
                JSONObject j = new JSONObject(outputString);

                if(j != null){
                    obj.clear();

                    action = (AgentAction) j.getEnum(AgentAction.class, "action");
                    this.bombermanGame.updateActionUser(action, this.playerNumber);

                    obj.put("listInfoAgents", bombermanGame.fusionListAgent());
                    obj.put("breakablewalls", bombermanGame.getpBreakable_walls());
                    obj.put("listItems", bombermanGame.getpListItems());
                    obj.put("listBombs", bombermanGame.getpListBomb());
                    obj.put("gameContinue", bombermanGame.gameContinue());

                    output.println(obj);
                }
                //Thread.sleep(400);
            }
        } catch (Exception e) {
            System.out.println("Error occured " +e.getMessage());
        }
    }

    /**
     * Envoie a tous les clients le jeu, qui a été mis à jour par le serveur
     * @param outputString : Objet JSON sous la forme d'une string
     */
    private void sendToALlClients(String outputString) {
        for( ServerThread sT: threadList) {
            sT.output.println(outputString);
        }
    }
}
