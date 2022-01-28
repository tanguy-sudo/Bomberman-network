package network.server;

import controller.ControllerBombermanGame;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class MainServer {

	public static void main(String[] args) {
		ArrayList<ServerThread> threadList = new ArrayList<>();
		try (ServerSocket serversocket = new ServerSocket(5000)){
			System.out.println("Serveur lancé...");

			ControllerBombermanGame controllerBombermanGame = new ControllerBombermanGame();
			controllerBombermanGame.initGame("/home/etud/Documents/s8/reseau/Bomberman-network/layouts/niveau3.lay", "1", true);
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
