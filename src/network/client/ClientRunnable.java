package network.client;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import models.BombermanGame;
import models.agent.BombermanAgent;
import view.PanelBomberman;
import view.ViewBombermanGame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRunnable implements Runnable {

    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    public ClientRunnable(Socket s) throws IOException {
        this.socket = s;
        this.input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
        //this.output = new PrintWriter(socket.getOutputStream(),true);
    }
    @Override
    public void run() {
        
            try {               
                while(true) {
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
                    ControllerClient controllerClient = new ControllerClient();
                    ViewBombermanGame viewBombermanGame = new ViewBombermanGame(panelBomberman, controllerClient);
                    System.out.println(response);
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
    
}
