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
 * Initialise une partie et lance le jeu
 */
public class MainServer {

	public static void main(String[] args) {
		ArrayList<Game> bombermanList = new ArrayList<Game>();
		ArrayList<ArrayList<ServerThreadSend>> threadList = new ArrayList<ArrayList<ServerThreadSend>>();

		for(int i = 0 ; i < 5 ; i++) {
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
				if(serverThreadSendArrayList.size() == bombermanGame.getpListBombermanAgent().size()){
					numeroGame++;
					ServerThreadListen.compteur = 0;
					System.out.println("Game : " + numeroGame);
				}
				ServerThreadListen serverThreadListen = new ServerThreadListen(socket, bombermanList.get(numeroGame), threadList.get(numeroGame));
				serverThreadListen.start();
			}
		} catch (Exception e) {
			System.out.println("Error occured in main: " + e.getMessage());
			e.printStackTrace();
		}
	}

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
