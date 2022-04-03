package network.server;

import controller.ControllerBombermanGame;
import models.BombermanGame;
import models.Game;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

/**
 * @author tanguy, guillaume
 * Initialise un serverSocket sur le port 5000 et crée des threads pour chacun des clients qui vont se connecter dessus
 * Initialise des parties
 */
public class MainServer {

	public static void main(String[] args) {
		ArrayList<Game> bombermanList = new ArrayList<Game>();
		ArrayList<ArrayList<ServerThreadSend>> threadList = new ArrayList<ArrayList<ServerThreadSend>>();

		// Crée une liste de partie
		for(int i = 0 ; i < 10 ; i++) {
			ControllerBombermanGame controllerBombermanGame = new ControllerBombermanGame();
			controllerBombermanGame.initGame("layouts/" + randomMap() + ".lay", "1", true);
			bombermanList.add(controllerBombermanGame.getpGame());

			threadList.add(new ArrayList<ServerThreadSend>());
		}

		try (ServerSocket serversocket = new ServerSocket(5000)){
			System.out.println("Serveur lancé...");
			int numeroGame = 0;
			System.out.println("Game : " + numeroGame);
			while(true) {
				Socket socket = serversocket.accept();
				BombermanGame bombermanGame = (BombermanGame) bombermanList.get(numeroGame);
				ArrayList<ServerThreadSend> serverThreadSendArrayList = threadList.get(numeroGame);
				// Si le nombre de joueurs connectés est suffisant on incrémente un compteur, pour la prochaine partie
				if(serverThreadSendArrayList.size() == bombermanGame.getpListBombermanAgent().size()){
					numeroGame++;
					// On remet à zéro le compteur de joueur
					ServerThreadListen.compteur = 0;
					System.out.println("Game : " + numeroGame);
				}
				if(numeroGame == 5) {
					break;
				}
				// On crée un thread qui écoute ce que le client envoie
				ServerThreadListen serverThreadListen = new ServerThreadListen(socket, bombermanList.get(numeroGame), threadList.get(numeroGame));
				serverThreadListen.start();
			}
		} catch (Exception e) {
			System.out.println("Error occured in main: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Choisie une map aléatoirement
	 * @return le nom de la map
	 */
	public static String randomMap() {
		Random r = new Random();
		switch(r.nextInt(5)) {
			case 0:
				return "exemple";
			case 1:
				return "niveau1";
			case 2:
				return "niveau2";
			case 3:
				return "niveau3";
			case 4:
				return "test";
		}
		return "exemple";
	}
}
