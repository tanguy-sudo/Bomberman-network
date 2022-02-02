package network.server;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import controller.ControllerBombermanGame;
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
    private Socket socket;
    private ArrayList<ServerThread> threadList;
    private PrintWriter output;
    private ControllerBombermanGame controllerBombermanGame;
    private ObjectMapper objectMapper;

    public ServerThread(Socket socket, ArrayList<ServerThread> threads, ControllerBombermanGame pcontrollerBombermanGame) {
        this.socket = socket;
        this.threadList = threads;
        this.controllerBombermanGame = pcontrollerBombermanGame;
        this.objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
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

            String objectString = objectMapper.writeValueAsString(controllerBombermanGame.getpGame());
            output.println(objectString);

            String outputString;
            AgentAction action;
            while(true) {

                outputString = input.readLine();
                if(outputString != null){
                    action = objectMapper.readValue(outputString, AgentAction.class);
                    this.controllerBombermanGame.getpGame().updateActionUser(action);
                    //System.out.println(action);
                    objectString = this.objectMapper.writeValueAsString(this.controllerBombermanGame.getpGame());
                    output.println(objectString);
                }
                Thread.sleep(400);
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
