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
		ArrayList<ServerThread> threadList = new ArrayList<ServerThread>();
		try (ServerSocket serversocket = new ServerSocket(5000)){
			System.out.println("Serveur lancé...");

			ControllerBombermanGame controllerBombermanGame = new ControllerBombermanGame();
			controllerBombermanGame.initGame("/home/etud/Documents/s8/reseau/Bomberman-network/layouts/test.lay", "1", true);
			controllerBombermanGame.getpGame().launch();

			while(true) {
				Socket socket = serversocket.accept();
				ServerThread serverThread = new ServerThread(socket, threadList, controllerBombermanGame);
				threadList.add(serverThread);
				serverThread.start();
			}
		} catch (Exception e) {
			System.out.println("Error occured in main: " + e.getStackTrace());
		}
	}
}
