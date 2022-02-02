package network.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.BombermanGame;
import view.PanelBomberman;
import view.ViewBombermanGame;
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
                while(true) {
                    response = input.readLine();
                    bombermanGame = objectMapper.readValue(response, BombermanGame.class);
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
