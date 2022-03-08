package network.server;

import controller.ControllerBombermanGame;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author tanguy, guillaume
 * Initialise un serverSocket sur le port 5000 et crée des threads pour chacun des clients qui vont se connecter dessus
 * Initialise une partie et lance le jeu
 */
public class MainServer {

	public static void main(String[] args) {
		ArrayList<ServerThreadSend> threadList = new ArrayList<ServerThreadSend>();
		try (ServerSocket serversocket = new ServerSocket(5000)){
			System.out.println("Serveur lancé...");

			ControllerBombermanGame controllerBombermanGame = new ControllerBombermanGame();
			controllerBombermanGame.initGame("layouts/test.lay", "1", true);

			while(true) {
				Socket socket = serversocket.accept();
				ServerThreadListen serverThreadListen = new ServerThreadListen(socket, controllerBombermanGame.getpGame(), threadList);
				serverThreadListen.start();
			}
		} catch (Exception e) {
			System.out.println("Error occured in main: " + e.getStackTrace());
		}
	}
}
