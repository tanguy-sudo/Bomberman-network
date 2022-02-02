package network.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.BombermanGame;
import models.agent.Agent;
import view.PanelBomberman;
import view.ViewBombermanGame;
import view.ViewEnd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

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

                ObjectMapper objectMapper = new ObjectMapper();

                BombermanGame bombermanGame = objectMapper.readValue(response, BombermanGame.class);

                PanelBomberman panelBomberman = new PanelBomberman(
                        bombermanGame.getpInputMap().getSize_x(),
                        bombermanGame.getpInputMap().getSize_y(),
                        bombermanGame.getpInputMap().getWalls(),
                        bombermanGame.getpBreakable_walls(),
                        bombermanGame.fusionListAgent()
                );
                ViewBombermanGame viewBombermanGame = new ViewBombermanGame(panelBomberman, controllerClient);

                ViewEnd viewEnd = null;
                while(true) {
                    response = input.readLine();
                    bombermanGame = objectMapper.readValue(response, BombermanGame.class);
                    if(viewEnd == null && (!bombermanGame.gameContinue())){
                        int countAgent = 0;
                        int countEnemy = 0;

                        for(Agent agent : bombermanGame.getpListBombermanAgent()) {
                            if(agent.getpLiving()) countAgent = countAgent + 1;
                        }
                        for(Agent agent : bombermanGame.getpListBombermanEnemy()) {
                            if(agent.getpLiving()) countEnemy = countEnemy + 1;
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

                        viewEnd = new ViewEnd(
                                    result,
                                    bombermanGame.getpListBombermanEnemy().size() - countEnemy,
                                    countEnemy,
                                    bombermanGame.getpListBombermanAgent().size() - countAgent,
                                    countAgent,
                                    controllerClient);
                    }
                    viewBombermanGame.updatePanel(bombermanGame);
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
