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
            // Charge le fichier de configuration
            pros.load(ip);
            url = pros.getProperty("serverAddress");
            // Crée un socket
            try (Socket socket = new Socket(url, 5000)){
                PrintWriter output = new PrintWriter(socket.getOutputStream(),true);

                ClientRunnable clientRun = new ClientRunnable(socket);
                JSONObject obj = new JSONObject();

                while(true){
                    obj.clear();
                    boolean sendLoginPassword = clientRun.isSendLoginPassword();
                    // Si l'utilisateur à clicker sur Login on envoie les informations de connexion
                    if(sendLoginPassword){
                        obj.put("username", clientRun.getControllerClient().getUsername());
                        obj.put("password", clientRun.getControllerClient().getPassword());
                    }

                    // Si l'utilisateur quitte la partie on envoie l'information au serveur
                    if (clientRun.getControllerClient().isExit()) {
                        obj.put("exit", clientRun.getControllerClient().isExit());
                    }

                    // Envoi de l'action de l'utilisateur
                    obj.put("action", clientRun.getControllerClient().getAction());
                    output.println(obj);

                    // la prochaine action est un stop, pour empêcher le déplacement automatique
                    clientRun.getControllerClient().setAction(AgentAction.STOP);
                    Thread.sleep(400);
                    // Si le client quitte la partie, on stope l'envoi d'informations
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
            System.out.println("Fin de partie");
        }
    }
}

