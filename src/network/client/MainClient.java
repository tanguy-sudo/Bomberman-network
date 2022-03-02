package network.client;

import org.json.JSONObject;
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
            JSONObject obj = new JSONObject();
            AgentAction agentAction;

            while(true){
                System.out.println(clientRun.getControllerClient().isStart());
                if(clientRun.getControllerClient().isStart()) {
                    obj.clear();

                    obj.put("exit", clientRun.getControllerClient().isExit());
                    agentAction = clientRun.getControllerClient().getAction();
                    obj.put("action", agentAction);

                    output.println(obj);

                    clientRun.getControllerClient().setAction(AgentAction.STOP);
                    Thread.sleep(400);
                }
                if (clientRun.getControllerClient().isExit()) {
                    break;
                }
            }

        } catch (Exception e) {
            System.out.println("Exception occured in client main: " + e.getStackTrace());
        }
    }
}

