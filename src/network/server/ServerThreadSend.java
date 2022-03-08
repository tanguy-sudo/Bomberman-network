package network.server;

import models.BombermanGame;
import models.Game;
import org.json.JSONObject;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThreadSend extends Thread{
    private Socket socket;
    private ArrayList<ServerThreadSend> threadList;
    private PrintWriter output;
    private BombermanGame bombermanGame;

    public ServerThreadSend(Socket socket, ArrayList<ServerThreadSend> threads, Game bombermanGame){
        this.socket = socket;
        this.threadList = threads;
        this.bombermanGame = (BombermanGame) bombermanGame;
        this.threadList.add(this);
    }

    @Override
    public synchronized void run() {
        try {
            output = new PrintWriter(socket.getOutputStream(),true);

            JSONObject obj = new JSONObject();
            obj.put("size_x", bombermanGame.getpInputMap().getSize_x());
            obj.put("size_y", bombermanGame.getpInputMap().getSize_y());
            obj.put("listInfoAgents", bombermanGame.fusionListAgent());
            obj.put("walls", bombermanGame.getpInputMap().getWalls());
            obj.put("breakablewalls", bombermanGame.getpBreakable_walls());
            obj.put("start", true);
            obj.put("gameContinue", bombermanGame.gameContinue());
            output.println(obj);

            while(true) {
                    obj.clear();

                    obj.put("start", true);
                    obj.put("listInfoAgents", bombermanGame.fusionListAgent());
                    obj.put("breakablewalls", bombermanGame.getpBreakable_walls());
                    obj.put("listItems", bombermanGame.getpListItems());
                    obj.put("listBombs", bombermanGame.getpListBomb());
                    obj.put("gameContinue", bombermanGame.gameContinue());

                    sendToALlClients(obj);

                    if(!this.bombermanGame.gameContinue()){
                        break;
                    }

                    Thread.sleep(200);
            }
        } catch (Exception e) {
            System.out.println("Error occured " +e.getMessage());
        }
    }

    /**
     * Envoie a tous les clients le jeu, qui a été mis à jour par le serveur
     * @param obj : Objet JSON sous la forme d'une string
     */
    private void sendToALlClients(JSONObject obj) {
        for(ServerThreadSend sT: threadList) {
            sT.output.println(obj);
        }
    }
}
