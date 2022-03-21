package network.client;

import org.json.JSONObject;
import utils.AgentAction;

import java.io.*;
import java.net.Socket;
import java.util.Properties;

/**
 * @author tanguy, guillaume
 * Crée un client qui va se connecter au serverSocket qui est sur le port 5000
 * envoie l'action que l'utilisateur a effectuée
 */
public class MainClient {

    public static void main(String[] args) {
        Properties pros;
        String url;
        pros = new Properties();
        FileInputStream ip = null;
        try {
            ip = new FileInputStream("./src/resources/config.properties");
            pros.load(ip);
            url = pros.getProperty("serverAddress");
            try (Socket socket = new Socket(url, 5000)){
                PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

                ClientRunnable clientRun = new ClientRunnable(socket);
                JSONObject obj = new JSONObject();

                while(true){
                    obj.clear();
                    boolean sendLoginPassword = clientRun.isSendLoginPassword();
                    if(sendLoginPassword){
                        obj.put("username", clientRun.getControllerClient().getUsername());
                        obj.put("password", clientRun.getControllerClient().getPassword());
                    }

                    if (clientRun.getControllerClient().isExit()) {
                        obj.put("exit", clientRun.getControllerClient().isExit());
                    }

                    obj.put("action", clientRun.getControllerClient().getAction());
                    output.println(obj);

                    clientRun.getControllerClient().setAction(AgentAction.STOP);
                    Thread.sleep(400);
                    if (clientRun.getControllerClient().isExit()) {
                        break;
                    }
                }

            } catch (Exception e) {
                System.out.println("Exception occured in client main: " + e.getStackTrace());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

