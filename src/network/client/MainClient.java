package network.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import utils.AgentAction;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author tanguy, guillaume
 * Crée un client qui va se connecter au serverSocket qui est sur le port 5000
 * envoie l'action que l'utilisateur a effectuée
 */
public class MainClient {

    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)){
            BufferedReader input = new BufferedReader( new InputStreamReader(socket.getInputStream()));
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

            ClientRunnable clientRun = new ClientRunnable(socket);
            new Thread(clientRun).start();
            ObjectMapper objectMapper = new ObjectMapper();
            String objectString;
            AgentAction agentAction;

            while(true){
                agentAction = clientRun.getControllerClient().getAction();
                objectString = objectMapper.writeValueAsString(agentAction);
                output.println(objectString);
                Thread.sleep(400);
            }

        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }
    }
}

