package network.server;

import models.BombermanGame;
import models.Game;
import models.strategy.BombermanManualStrategy;
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
public class ServerThreadListen extends Thread {
    private static int compteur = 0;
    private Socket socket;
    private PrintWriter output;
    private BombermanGame bombermanGame;
    private int playerNumber;
    private ArrayList<ServerThreadSend> threadList;

    public ServerThreadListen(Socket socket, Game bombermanGame, ArrayList<ServerThreadSend> threads) {
        this.socket = socket;
        this.bombermanGame = (BombermanGame) bombermanGame;
        this.playerNumber = ServerThreadListen.compteur;
        this.bombermanGame.getpListBombermanAgent().get(this.playerNumber).setpStrategy(new BombermanManualStrategy());
        ServerThreadListen.compteur++;
        this.threadList = threads;
    }

    /**
     * crée une entrée et une sortie sur un socket, pour lire et écrire dessus
     * li l'action que le client envoie et lui renvoie le jeu modifié
     */
    @Override
    public synchronized void run() {
        try {
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            //output = new PrintWriter(socket.getOutputStream(),true);

            String outputString;
            AgentAction action;
            Boolean exit;
            Boolean start;
            Boolean firstStart = true;
            while(true) {

                outputString = input.readLine();
                JSONObject j = new JSONObject(outputString);

                if(j != null){

                    exit = j.getBoolean("exit");
                    if(exit){
                        System.out.println("fin de partie");
                        break;
                    }

                    action = (AgentAction) j.getEnum(AgentAction.class, "action");
                    this.bombermanGame.updateActionUser(action, this.playerNumber);

                    start = j.getBoolean("start");
                    if(start && firstStart && (this.threadList.size() == this.bombermanGame.getpListBombermanAgent().size())){
                        this.bombermanGame.launch();
                        firstStart = false;
                    }

                }
            }
        } catch (Exception e) {
            System.out.println("Error occured " +e.getMessage());
        }
    }
}
