package network.client;

import org.json.JSONArray;
import org.json.JSONObject;
import utils.InfoAgent;
import utils.InfoBomb;
import utils.InfoItem;
import view.PanelBomberman;
import view.ViewBombermanGame;
import view.ViewEnd;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author tanguy, guillaume
 * écoute ce que le serveur envoie, puis créé une vue, et la mets à jour à chaque fois que le serveur envoie une information
 */
public class ClientRunnable implements Runnable {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;
    public ControllerClient controllerClient;

    public ClientRunnable(Socket s) throws IOException {
        this.controllerClient = new ControllerClient();
        this.socket = s;
        this.input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        //this.output = new PrintWriter(socket.getOutputStream(),true);
    }
    @Override
    public void run() {
        
            try {
                String response = input.readLine();

                JSONObject j = new JSONObject(response);
                JSONArray JSONListAgents = j.getJSONArray("listInfoAgents");
                JSONArray JSONwalls = j.getJSONArray("walls");
                JSONArray JSONbreakablewalls = j.getJSONArray("breakablewalls");

                ArrayList<InfoAgent> listAgent = JsonConvert.ToListInfoAgent(JSONListAgents); // liste des agents
                int size_x = j.getInt("size_x"); // taille de la map
                int size_y = j.getInt("size_y");// taille de la map
                boolean[][] walls = JsonConvert.ToListWalls(JSONwalls); // murs
                boolean[][] breakablewalls = JsonConvert.ToListWalls(JSONbreakablewalls); // murs cassables

                PanelBomberman panelBomberman = new PanelBomberman(size_x, size_y, walls, breakablewalls, listAgent);
                ViewBombermanGame viewBombermanGame = new ViewBombermanGame(panelBomberman, controllerClient);

                ViewEnd viewEnd = null;
                JSONArray JSONListItems;
                JSONArray JSONListbombs;
                ArrayList<InfoItem> listItems;
                ArrayList<InfoBomb> listBombs;

                while(true) {
                    response = input.readLine();

                    j.clear();
                    j = new JSONObject(response);
                    JSONListAgents = j.getJSONArray("listInfoAgents");
                    JSONbreakablewalls = j.getJSONArray("breakablewalls");
                    JSONListItems = j.getJSONArray("listItems");
                    JSONListbombs = j.getJSONArray("listBombs");

                    listAgent = JsonConvert.ToListInfoAgent(JSONListAgents); // liste des agents
                    breakablewalls = JsonConvert.ToListWalls(JSONbreakablewalls); // murs cassables
                    listItems = JsonConvert.ToListInfoItem(JSONListItems); // liste des items
                    listBombs = JsonConvert.ToListInfoBomb(JSONListbombs); // listes des bombes


                    if(viewEnd == null && !j.getBoolean("gameContinue")){
                        int countAgent = 0;
                        int countEnemy = 0;

                        for(InfoAgent agent : listAgent){
                            if(agent.getType() == 'B') countAgent++;
                            else countEnemy++;
                        }

                        int result = 0;
                        if(countAgent == 0 && countEnemy == 0) {
                            // égalité
                            result = 0;
                        }else if(countAgent == 0) {
                            // perdu
                            result = 1;
                        }else if(countEnemy == 0) {
                            // gagné
                            result = 2;
                        }

                        viewEnd = new ViewEnd(result, 0, countEnemy, 0, countAgent, controllerClient);
                    }
                    viewBombermanGame.updatePanel(breakablewalls, listAgent, listItems, listBombs);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    input.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
    }

    public ControllerClient getControllerClient(){
        return this.controllerClient;
    }
    
}
